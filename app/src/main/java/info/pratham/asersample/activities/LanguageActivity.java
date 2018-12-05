package info.pratham.asersample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
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
import info.pratham.asersample.interfaces.ProficiencyListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;

public class LanguageActivity extends BaseActivity implements WordsListListener, ProficiencyListener {
    @BindView(R.id.question)
    TextView tv_question;
    @BindView(R.id.testType)
    TextView testType;
    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.nextItem)
    Button nextItem;
    @BindView(R.id.prevItem)
    Button prevItem;
    @BindView(R.id.recordButtonSP)
    Button recordButton;

    String currentLevel, currentFilePath, currentFileName;
    boolean recording, playing;
    int wordCOunt;
    List selectedWordsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        currentFilePath = ASERApplication.getRootPath();
        currentFileName = "abc.mp3";
        if (nextItem.isShown()) {
            nextItem.setVisibility(View.INVISIBLE);
        }
        if (prevItem.isShown()) {
            prevItem.setVisibility(View.INVISIBLE);
        }
        testType.setText(AserSample_Constant.selectedLanguage + " Test");
        String question = databaseInstance.getQuestiondao().getLanguageQuestions(AserSample_Constant.selectedLanguage);
        try {
            JSONObject questionJson = new JSONObject(question);
            int randomNo = ASERApplication.getRandomNumber(0, questionJson.length());
            AserSample_Constant.sample = (JSONObject) questionJson.get("Sample" + (randomNo + 1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showParagraph();
    }


    @OnClick(R.id.markProficiency)
    public void markProficiency() {
        List optionList = new ArrayList();
        optionList.add(getString(R.string.Letter));
        optionList.add(getString(R.string.Word));
        optionList.add(getString(R.string.Paragraph));
        optionList.add(getString(R.string.Story));
        optionList.add(getString(R.string.Beginner));
        optionList.add(getString(R.string.TestWasNotComplete));

        ProficiencyDialog proficiencyDialog = new ProficiencyDialog(this, optionList);
        proficiencyDialog.show();
    }

    private void showParagraph() {
        setVisibilityForPrevNext();
        if (!next.isShown())
            next.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.Word), getString(R.string.Story));
        currentLevel = getString(R.string.Paragraph);
        String msg = AserSample_Constant.getPara(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            tv_question.setTextSize(1, 30);
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showStory() {
        setVisibilityForPrevNext();
        if (next.isShown())
            next.setVisibility(View.GONE);
        setNavigation(getString(R.string.Paragraph), "");
        currentLevel = getString(R.string.Story);
        String msg = AserSample_Constant.getStory(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            tv_question.setTextSize(1, 25);
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showLetters() {
        setVisibilityForPrevNext();
        if (previous.isShown()) {
            previous.setVisibility(View.GONE);
        }
        setNavigation("", getString(R.string.Word));
        currentLevel = getString(R.string.Letter);
        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showWords() {
        setVisibilityForPrevNext();
        if (!previous.isShown()) {
            previous.setVisibility(View.VISIBLE);
        }
        setNavigation(getString(R.string.Letter), getString(R.string.Paragraph));
        currentLevel = getString(R.string.Word);
        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showQue(String msg) {
        tv_question.setText(msg);
    }

    private void openNextActivity(String proficiency) {
        AserSampleUtility.showToast(this, AserSample_Constant.selectedLanguage + "_" + proficiency);
        Intent intent = new Intent(this, MathActivity.class);
        startActivity(intent);
    }

    @Override
    public void getSelectedwords(List list) {
        if (!list.isEmpty()) {
            tv_question.setTextSize(1, 60);
            wordCOunt = -1;
            selectedWordsList = list;
            if (!nextItem.isShown()) {
                nextItem.setVisibility(View.VISIBLE);
            }
            showNextItem();
        }
    }

    @OnClick(R.id.nextItem)
    public void showNextItem() {
        wordCOunt++;
        showQue(selectedWordsList.get(wordCOunt).toString());
        if (wordCOunt == 1) {
            if (!prevItem.isShown()) {
                prevItem.setVisibility(View.VISIBLE);
            }
        }
        if ((wordCOunt + 1) == selectedWordsList.size()) {
            if (nextItem.isShown()) {
                nextItem.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick(R.id.prevItem)
    public void showPrevItem() {
        wordCOunt--;
        showQue(selectedWordsList.get(wordCOunt).toString());
        if (wordCOunt == 0) {
            if (prevItem.isShown()) {
                prevItem.setVisibility(View.INVISIBLE);
                nextItem.setVisibility(View.VISIBLE);
            }
        }

        if (wordCOunt > -1) {
            if (prevItem.isShown()) {
                nextItem.setVisibility(View.VISIBLE);
            }
        }

    }

    @OnClick(R.id.next)
    public void next() {
        switch (currentLevel) {
            case "Paragraph":
                showStory();
                break;
            case "Word":
                showParagraph();
                break;
            case "Letter":
                showWords();
                // openNextActivity("Letter");
                break;
        }
    }

    @OnClick(R.id.previous)
    public void previous() {
        switch (currentLevel) {
            case "Story":
                showParagraph();
                break;
            case "Paragraph":
                showWords();
                break;
            case "Word":
                showLetters();
                break;
        }
    }

    @OnClick(R.id.recordButtonSP)
    public void startOrStopRecording() {
        if (playing && !recording) {
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mic_blue_round));
            playing = false;
        }
        if (recording && playing) {
            recording = false;
            AudioUtil.playRecording(currentFilePath + currentFileName);
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.playing_icon));
        } else if (recording && !playing) {
            AudioUtil.stopRecording();
            playing = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        } else {
            AudioUtil.startRecording(currentFilePath + currentFileName);
            recording = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recording));
        }
    }

    private void setVisibilityForPrevNext() {
        nextItem.setVisibility(View.INVISIBLE);
        prevItem.setVisibility(View.INVISIBLE);
    }

    private void setNavigation(String prevText, String nextText) {
        if (previous.isShown())
            previous.setText("< " + prevText);
        if (next.isShown())
            next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        openNextActivity(proficiency);
    }
}
