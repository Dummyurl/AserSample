package info.pratham.asersample.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.ASERApplication;
import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.dialog.ProficiencyDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

public class EnglishActivity extends BaseActivity implements WordsListListener {

    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.question)
    TextView tv_question;
    @BindView(R.id.testType)
    TextView testType;

    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        testType.setText("English" + "Test");

        String question = databaseInstance.getQuestiondao().getLanguageQuestions(AserSample_Constant.selectedLanguage);
        try {
            JSONObject questionJson = new JSONObject(question);
            int randomNo = ASERApplication.getRandomNumber(0, questionJson.length());
            AserSample_Constant.sample = (JSONObject) questionJson.get("Sample" + (randomNo + 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getData("Capital");
    }

    private void getData(String type) {
        if (type.equalsIgnoreCase("Capital")) {
            previous.setVisibility(View.GONE);
            setNavigation("", getString(R.string.Smallletter));
            currentLevel = getString(R.string.Capitalletter);
        } else if (type.equalsIgnoreCase("Small")) {
            if (!previous.isShown())
                previous.setVisibility(View.VISIBLE);
            setNavigation(getString(R.string.Capitalletter), getString(R.string.word));
            currentLevel = getString(R.string.Smallletter);
        } else {
            if (!next.isShown())
                next.setVisibility(View.VISIBLE);
            setNavigation(getString(R.string.Smallletter), getString(R.string.Sentence));
            currentLevel = getString(R.string.word);
        }

        JSONArray dataArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);
        if (dataArray != null) {
            List dataList = new ArrayList();
            try {
                for (int i = 0; i < dataArray.length(); i++) {
                    dataList.add(dataArray.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, dataList);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void getSentences() {
        if (next.isShown())
            next.setVisibility(View.GONE);
        setNavigation(getString(R.string.word), "");
        currentLevel = getString(R.string.Sentence);
        JSONArray dataArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);
        showQue(dataArray.toString());
    }


    @OnClick(R.id.markProficiency)
    public void markProficiency() {
        List optionList = new ArrayList();
        optionList.add(getString(R.string.Capitalletter));
        optionList.add(getString(R.string.Smallletter));
        optionList.add(getString(R.string.word));
        optionList.add(getString(R.string.Sentence));
        optionList.add(getString(R.string.Beginner));
        optionList.add(getString(R.string.TestWasNotComplete));

        ProficiencyDialog proficiencyDialog = new ProficiencyDialog(this, optionList);
        proficiencyDialog.show();
    }

    @OnClick(R.id.next)
    public void next() {
        switch (currentLevel) {
            case "Capital letter":
                getData("Small");
                break;
            case "Small letter":
                getData("Words");
                break;
            case "word":
                getSentences();
                break;
        }
    }

    @OnClick(R.id.previous)
    public void previous() {
        switch (currentLevel) {
            case "Small letter":
                getData("Capital");
                break;
            case "word":
                getData("Small");
                break;
            case "Sentence":
                getData("word");
                break;
        }
        terminationWorks();
    }

    private void terminationWorks() {
        // ASER test is completed now do the app termination things like sync and all
    }

    private void showQue(String msg) {
        tv_question.setText(msg);
    }

    @Override
    public void getSelectedwords(List list) {
        if (!list.isEmpty()) {
            showQue(list.toString());
        }
    }

    private void setNavigation(String prevText, String nextText) {
        if (previous.isShown())
            previous.setText("< " + prevText);
        if (next.isShown())
            next.setText(nextText + " >");
    }
}
