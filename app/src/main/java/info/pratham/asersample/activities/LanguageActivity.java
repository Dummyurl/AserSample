package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import info.pratham.asersample.fragments.StudentDetails;
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
    @BindView(R.id.refreshIV)
    ImageView refreshIcon;
    @BindView(R.id.displayLayout)
    RelativeLayout displayLayout;
    @BindView(R.id.mistakes)
    EditText mistakes;

    static String currentFilePath;
    String currentLevel, currentFileName;
    boolean recording, playing;
    int wordCOunt;
    List selectedWordsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        currentFilePath = ASERApplication.getRootPath() + AserSample_Constant.getCrlID() + "/" +
                AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/" +
                AserSample_Constant.selectedLanguage + "/";
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
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().getParagragh_mistake());
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
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().getStory_mistake());
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
                mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().getLetter_mistake());
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
                mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().getWord_mistake());
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
        initiateRecording();
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
        initiateRecording();
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
        assignMistakeCount(currentLevel, mistakes.getText().toString());
        initiateRecording();
        switch (currentLevel) {
            case "Paragraph":
                showStory();
                break;
            case "Word":
                showParagraph();
                break;
            case "Letter":
                showWords();
                break;
        }


    }

    @OnClick(R.id.previous)
    public void previous() {
        assignMistakeCount(currentLevel, mistakes.getText().toString());
        initiateRecording();
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

    public void audioStopped() {
        recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        recording = true;
        playing = true;
    }

    public void initiateRecording() {
        AudioUtil.stopRecording();
        AudioUtil.stopPlayingAudio();
        recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mic_blue_round));
        refreshIcon.setVisibility(View.INVISIBLE);
        tv_question.setAlpha(1f);
        playing = false;
        recording = false;
    }

    @OnClick(R.id.refreshIV)
    public void refreshRecording() {
        initiateRecording();
    }

    @OnClick(R.id.recordButtonSP)
    public void startOrStopRecording() {
        String fileStorePath = currentFilePath + "sample.mp3";
        switch (currentLevel) {
            case "Story":
                fileStorePath = currentFilePath;
                currentFileName = "story.mp3";
                break;
            case "Paragraph":
                fileStorePath = currentFilePath;
                currentFileName = "paragraph.mp3";
                break;
            case "Word":
                fileStorePath = currentFilePath + "Word/";
                currentFileName = selectedWordsList.get(wordCOunt).toString() + ".mp3";
                break;
            case "Letter":
                fileStorePath = currentFilePath + "Letter/";
                currentFileName = selectedWordsList.get(wordCOunt).toString() + ".mp3";
                break;
        }

        File file = new File(fileStorePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (playing && !recording) {
            //initiateRecording();
        } else if (recording && playing) {
//            recording = false;
            AudioUtil.playRecording(fileStorePath + currentFileName, this);
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.playing_icon));
        } else if (recording && !playing) {
            AudioUtil.stopRecording();
            refreshIcon.setVisibility(View.VISIBLE);
            tv_question.setAlpha(0.5f);
            playing = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        } else {
            AudioUtil.startRecording(fileStorePath + currentFileName);
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
        AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().setProficiency(proficiency);
        openNextActivity(proficiency);
    }

    private void assignMistakeCount(String level, String cnt) {
        switch (level) {
            case "Story":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().setStory_mistake(cnt);
                break;
            case "Paragraph":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().setParagragh_mistake(cnt);
                break;
            case "Word":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().setWord_mistake(cnt);
                break;
            case "Letter":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguageProficiency().setLetter_mistake(cnt);
                break;
        }
    }


    @Override
    public void onBackPressed() {
       /* super.onBackPressed();*/
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setMessage("Student progress can lost");
        builder.setCancelable(false);
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               finish();
            }
        });
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
