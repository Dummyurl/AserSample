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
import info.pratham.asersample.adapters.RecyclerViewAdapter;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

public class Words extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    JSONArray question_jsonArray;

    List<QuestionStructure> questionList;
    RecyclerViewAdapter recyclerViewAdapter;

    // String quelevel;
    String level;

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
            JSONObject englishJSOn;
            JSONObject mathematicsJSOn;
            // quelevel = "";
            question_jsonArray = null;
            switch (level) {
                //NATIVE LANGUAGE
                case "Words":
                    if (ListConstant.Words != null) {
                        questionList = ListConstant.Words;
                    } else {
                        question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.WordNative));
                    }
                    break;
                case "Letters":
                    if (ListConstant.Letters != null) {
                        questionList = ListConstant.Letters;
                    } else {
                        question_jsonArray = AserSample_Constant.sample.getJSONArray(getString(R.string.Letter));
                    }
                    break;
                //ENGLISH LANGUAGE
                case "Capital":
                    if (ListConstant.Capital != null) {
                        questionList = ListConstant.Capital;
                    } else {
                        englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                        question_jsonArray = englishJSOn.getJSONArray(getString(R.string.Capitalletter));
                    }
                    break;
                case "Small":
                    if (ListConstant.Small != null) {
                        questionList = ListConstant.Small;
                    } else {
                        englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                        question_jsonArray = englishJSOn.getJSONArray(getString(R.string.Smallletter));
                    }
                    break;
                case "word":
                    if (ListConstant.engWord != null) {
                        questionList = ListConstant.engWord;
                    } else {
                        englishJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.English));
                        question_jsonArray = englishJSOn.getJSONArray(getString(R.string.word));
                    }
                    break;
                //MATHEMATICS
                case "Single":
                    if (ListConstant.Single != null) {
                        questionList = ListConstant.Single;
                    } else {
                        mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math)).getJSONObject("Number Recognition");
                        question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.oneToNine));
                    }
                    break;
                case "Double":
                    if (ListConstant.Double != null) {
                        questionList = ListConstant.Double;
                    } else {
                        mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math)).getJSONObject("Number Recognition");
                        question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.tenToNinetyNine));
                    }
                    break;
                case "Subtraction":
                    if (ListConstant.Subtraction != null) {
                        questionList = ListConstant.Subtraction;
                    } else {
                        mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math));
                        question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.Subtraction));
                        // quelevel = getString(R.string.Subtraction);
                    }
                    break;
                case "Division":
                    if (ListConstant.Division != null) {
                        questionList = ListConstant.Division;
                    } else {
                        mathematicsJSOn = AserSample_Constant.sample.getJSONObject(getString(R.string.Math));
                        question_jsonArray = mathematicsJSOn.getJSONArray(getString(R.string.Division));
                        // quelevel = getString(R.string.Division);
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
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), questionList, level);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
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
            case "Words":
                ListConstant.Words = questionList;
                break;
            case "Letters":
                ListConstant.Letters = questionList;
                break;
            //ENGLISH
            case "Capital":
                ListConstant.Capital = questionList;
                break;
            case "Small":
                ListConstant.Small = questionList;
                break;
            case "word":
                ListConstant.engWord = questionList;
                break;
            //MATHEMATICS
            case "Single":
                ListConstant.Single = questionList;
                break;
            case "Double":
                ListConstant.Double = questionList;
                break;
            case "Subtraction":
                ListConstant.Subtraction = questionList;
                break;
            case "Division":
                ListConstant.Division = questionList;
                break;
        }
    }
}
