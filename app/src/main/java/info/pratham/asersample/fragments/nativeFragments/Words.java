package info.pratham.asersample.fragments.nativeFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import info.pratham.asersample.adapters.RecyclerViewAdapter;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.utility.AserSample_Constant;

public class Words extends Fragment {
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
            JSONObject englishJSOn;
            JSONObject mathematicsJSOn;

            switch (level) {
                //NATIVE LANGUAGE
                case "Words":
                    question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.WordNative));
                    break;
                case "Letters":
                    question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.Letter));
                    break;
                //ENGLISH LANGUAGE
                case "Capital":
                    englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                    question_jsonArray = englishJSOn.getJSONArray(getString(R.string.Capitalletter));
                    break;
                case "Small":
                    englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                    question_jsonArray = englishJSOn.getJSONArray(getString(R.string.Smallletter));
                    break;
                case "word":
                    englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                    question_jsonArray = englishJSOn.getJSONArray(getString(R.string.word));
                    break;
                //MATHEMATICS
                case "Single":
                    mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math)).getJSONObject("Number Recognition");
                    question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.oneToNine));
                    break;
                case "Double":
                    mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math)).getJSONObject("Number Recognition");
                    question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.tenToNinetyNine));
                    break;
                case "Subtraction":
                    mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math));
                    question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.Subtraction));
                    break;
                case "Division":
                    mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math));
                    question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.Division));
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
        if (getScreenWidthDp() >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else if (getScreenWidthDp() >= 400) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), questionList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }
}
