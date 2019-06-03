package info.pratham.asersample.fragments.nativeFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.R;
import info.pratham.asersample.adapters.RecyclerVerticalAdapter;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

public class Paragraph extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    JSONArray question_jsonArray;
    String level;

    List<QuestionStructure> questionList;
    RecyclerVerticalAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.word_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        try {
            Bundle args = getArguments();
            level = args.getString("level");
            questionList = new ArrayList();
            question_jsonArray = null;
            switch (level) {
                //NATIVE LANGUAGE
                case "Para":
                    if (ListConstant.Para != null) {
                        questionList = ListConstant.Para;
                    } else {
                        question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.Paragraph));
                    }
                    break;
                case "Story":
                    if (ListConstant.Story != null) {
                        questionList = ListConstant.Story;
                    } else {
                        question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.Story));
                    }

                    break;
                //ENGLISH LANGUAGE
                case "Sentence":
                    if (ListConstant.Sentence != null) {
                        questionList = ListConstant.Sentence;
                    } else {
                        JSONObject englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                        question_jsonArray = englishJSOn.getJSONArray(getString(R.string.Sentence));
                    }
                    break;
            }
            if (question_jsonArray != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<QuestionStructure>>() {
                }.getType();
                questionList = gson.fromJson(question_jsonArray.toString(), listType);
            }
            if (questionList.isEmpty()) {
                Toast.makeText(getActivity(), "Please pull data again", Toast.LENGTH_SHORT).show();
            } else {
                initRecycler();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initRecycler() {
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerViewAdapter = new RecyclerVerticalAdapter(getActivity(), questionList, level);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        backupList();
        recyclerViewAdapter.closeEnlargeView();
    }

    public void backupList() {
        switch (level) {
            //NATIVE LANGUAGE
            case "Para":
                ListConstant.Para = questionList;
                break;
            case "Story":
                ListConstant.Story = questionList;
                break;
            //ENGLISH
            case "Sentence":
                ListConstant.Sentence = questionList;
                break;
        }
    }
}
