package info.pratham.asersample.fragments.nativeFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import info.pratham.asersample.adapters.RecyclerViewAdapter;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.utility.AserSample_Constant;

public class Paragraph extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    JSONArray question_jsonArray;

    List<QuestionStructure> questionList;

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
            String level = args.getString("level");
            questionList = new ArrayList();
            switch (level) {
                //NATIVE LANGUAGE
                case "Para":
                    question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.Paragraph));
                    break;
                case "Story":
                    question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.Story));
                    break;
                //ENGLISH LANGUAGE
                case "Sentence":
                    JSONObject englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                    question_jsonArray = englishJSOn.getJSONArray(getString(R.string.Sentence));
                    break;
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<List<QuestionStructure>>() {
            }.getType();
            questionList = gson.fromJson(question_jsonArray.toString(), listType);
            initRecycler();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initRecycler() {
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(gridLayoutManager);

        RecyclerVerticalAdapter recyclerViewAdapter = new RecyclerVerticalAdapter(getActivity(), questionList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
