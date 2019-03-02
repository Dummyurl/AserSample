package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import info.pratham.asersample.database.modalClasses.QueLevel;
import info.pratham.asersample.database.modalClasses.SingleQustion;
import info.pratham.asersample.dialog.EndOfLevelDialog;
import info.pratham.asersample.dialog.MistakeCountDialog;
import info.pratham.asersample.dialog.ProficiencyDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.interfaces.LevelFinishListner;
import info.pratham.asersample.interfaces.MistakeCountListener;
import info.pratham.asersample.interfaces.ProficiencyListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;

import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;

public class LanguageActivity extends BaseActivity implements WordsListListener, ProficiencyListener, MistakeCountListener, LevelFinishListner {

    @BindView(R.id.question)
    TextView tv_question;
    @BindView(R.id.testType)
    TextView testType;
    @BindView(R.id.level)
    TextView tv_level;
    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.nextItem)
    ImageView nextItem;
    @BindView(R.id.prevItem)
    ImageView prevItem;
    @BindView(R.id.recordButtonSP)
    Button recordButton;
    @BindView(R.id.refreshIV)
    ImageView refreshIcon;
    @BindView(R.id.displayLayout)
    RelativeLayout displayLayout;
    @BindView(R.id.attemped)
    ImageView attemped;

    String currentLevel;
    boolean recording, playing, isNewQuestion;
    public static String currentFilePath, currentFileName;
    int wordCOunt;
    List<JSONObject> selectedWordsList;

    List<QueLevel> parentDataList;
    QueLevel queLevel;
    List tempSingleQue;
    MistakeCountDialog mistakCountDialog;

    String currentClick, attemptedQuePathCache;
    boolean isQueAttempt = false, prevAttempted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tv_question.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        } else {
            TextViewCompat.setAutoSizeTextTypeWithDefaults(tv_question, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }

        currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";

        nextItem.setVisibility(View.GONE);
        prevItem.setVisibility(View.GONE);
        testType.setText(AserSample_Constant.selectedLanguage + " Test");
        parentDataList = AserSample_Constant.getAserSample_Constant().getStudent().getSequenceList();
        String question = databaseInstance.getQuestiondao().getLanguageQuestions(AserSample_Constant.selectedLanguage);

        try {
            if (question != null) {
                JSONObject questionJson = new JSONObject(question);
                int randomNo = ASERApplication.getRandomNumber(0, questionJson.length());
                AserSample_Constant.sample = (JSONObject) questionJson.get("Sample" + (randomNo + 1));
                showParagraph();
            } else {
                AserSampleUtility.showToast(this, "No data available. Contact administrator!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.markProficiency)
    public void markProficiency() {
        currentClick = "PROFICIENCY";
        if (isQueAttempt)
            showMistakeCountDialog();

        else {
            List optionList = new ArrayList();
            optionList.add(getString(R.string.Letter));
            optionList.add(getString(R.string.WordNative));
            optionList.add(getString(R.string.Paragraph));
            optionList.add(getString(R.string.Story));
            optionList.add(getString(R.string.Beginner));
            optionList.add(getString(R.string.TestWasNotComplete));
            ProficiencyDialog proficiencyDialog = new ProficiencyDialog(this, optionList);
            proficiencyDialog.show();
        }
    }

    private void showParagraph() {
        next.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.WordNative), getString(R.string.Story));
        currentLevel = getString(R.string.Paragraph);
        tv_level.setText("Basic Reading - " + currentLevel);
        setVisibilityForPrevNext();
        JSONObject msg = AserSample_Constant.getPara(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void showStory() {
        next.setVisibility(View.GONE);
        setNavigation(getString(R.string.Paragraph), "");
        currentLevel = getString(R.string.Story);
        tv_level.setText("Basic Reading - " + currentLevel);
        setVisibilityForPrevNext();
        JSONObject msg = AserSample_Constant.getStory(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void showLetters() {
        previous.setVisibility(View.GONE);
        setNavigation("", getString(R.string.WordNative));
        currentLevel = getString(R.string.Letter);
        tv_level.setText("Basic Reading - " + currentLevel);
        setVisibilityForPrevNext();

        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5, currentLevel);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void showWords() {
        previous.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.Letter), getString(R.string.Paragraph));
        currentLevel = getString(R.string.WordNative);
        tv_level.setText("Basic Reading - " + currentLevel);
        setVisibilityForPrevNext();
        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.get(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5, currentLevel);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void showQue(JSONObject msg) {
        try {
            isNewQuestion = true;
            prevAttempted = false;
            for (QueLevel queLevel : parentDataList) {
                if (queLevel.getLevel().equals(currentLevel)) {
                    for (SingleQustion singleQustion : queLevel.getQuestions()) {
                        if (singleQustion.getQue_id().equals(msg.getString("id"))) {
                            prevAttempted = true;
                            attemptedQuePathCache = singleQustion.getRecordingName();
                        }
                    }
                    /*if (prevAttempted) {
                        break;
                    }*/

                }
            }
            if (!prevAttempted && (currentLevel.equals(getString(R.string.WordNative)) || currentLevel.equals(getString(R.string.Letter)))) {
                for (SingleQustion singleQustion : queLevel.getQuestions()) {
                    if (singleQustion.getQue_id().equals(msg.getString("id"))) {
                        prevAttempted = true;
                        attemptedQuePathCache = singleQustion.getRecordingName();
                    }

                }
            }
            if (prevAttempted) {
                refreshIcon.setVisibility(View.VISIBLE);
                tv_question.setAlpha(0.5f);
                recording = playing = true;
                recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
                attemped.setVisibility(View.VISIBLE);
            } else {
                initiateRecording();
                attemped.setVisibility(View.GONE);
            }

            tv_question.invalidate();
            tv_question.setText(msg.getString("data"));
            tv_question.setTag(msg.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openNextActivity() {

        Intent intent = new Intent(LanguageActivity.this, MathActivity.class);
        startActivity(intent);

    }

    @Override
    public void getSelectedwords(List list) {
        if (!list.isEmpty()) {
            wordCOunt = -1;
            selectedWordsList = list;
            nextItem.setVisibility(View.VISIBLE);
            showNextItem();
        }
    }

    @OnClick(R.id.nextItem)
    public void showNextItem() {
        initiateRecording();
        wordCOunt++;
        showQue(selectedWordsList.get(wordCOunt));
        if (wordCOunt == 1) {
            prevItem.setVisibility(View.VISIBLE);
        }
        if ((wordCOunt + 1) == selectedWordsList.size()) {
            nextItem.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.prevItem)
    public void showPrevItem() {
        initiateRecording();
        wordCOunt--;
        showQue(selectedWordsList.get(wordCOunt));
        if (wordCOunt == 0) {
            prevItem.setVisibility(View.GONE);
            nextItem.setVisibility(View.GONE);
        }
        if (wordCOunt > -1) {
            nextItem.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.next)
    public void next() {
        currentClick = "NEXT";
        if (isQueAttempt) {
            showMistakeCountDialog();
        } else {
            isQueAttempt = false;
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
    }

    @OnClick(R.id.previous)
    public void previous() {
        currentClick = "PREVIOUS";
        if (isQueAttempt)
            showMistakeCountDialog();
        else {
            isQueAttempt = false;
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
        prevAttempted = false;
        playing = false;
        recording = false;
    }

    @OnClick(R.id.refreshIV)
    public void refreshRecording() {
        initiateRecording();
    }

    @OnClick(R.id.recordButtonSP)
    public void startOrStopRecording() {
        if (isNewQuestion && !recording && !playing) {
            isNewQuestion = false;
            currentFileName = updateJsonDetails();
        }

        String fileStorePath = currentFilePath + currentFileName;


        File file = new File(currentFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (!recording && playing) {
            //initiateRecording();
        } else if (recording && playing) {
            if (prevAttempted)
                AudioUtil.playRecording(currentFilePath + attemptedQuePathCache, this);
            else
                AudioUtil.playRecording(fileStorePath, this);
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.playing_icon));
        } else if (recording) {
            AudioUtil.stopRecording();
            refreshIcon.setVisibility(View.VISIBLE);
            tv_question.setAlpha(0.5f);
            playing = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        } else {
            AudioUtil.startRecording(fileStorePath);
            recording = true;
            isQueAttempt = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recording));
        }
    }

    private String updateJsonDetails() {
        String recordingFileName;
        SingleQustion singleQustion = new SingleQustion();
        singleQustion.setQue_seq_cnt(tempSingleQue.size());
        singleQustion.setQue_id(tv_question.getTag().toString());
        singleQustion.setQue_text(tv_question.getText().toString());
        recordingFileName = queLevel.getLevel_seq_cnt() + "_" + singleQustion.getQue_seq_cnt() + "_" + tv_question.getTag().toString() + ".mp3";
        singleQustion.setRecordingName(recordingFileName);
        tempSingleQue.add(singleQustion);
        return recordingFileName;
    }

    private void setVisibilityForPrevNext() {
        nextItem.setVisibility(View.GONE);
        prevItem.setVisibility(View.GONE);
        // Initiate question level
        if (queLevel != null && queLevel.getQuestions().size() > 0) {
            parentDataList.add(queLevel);
        }
        queLevel = new QueLevel();
        queLevel.setSubject(AserSample_Constant.selectedLanguage);
        queLevel.setLevel(currentLevel);
        queLevel.setLevel_seq_cnt(parentDataList.size());
        tempSingleQue = queLevel.getQuestions();
    }

    private void setNavigation(String prevText, String nextText) {
        previous.setText("< " + prevText);
        next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        setVisibilityForPrevNext();
        AserSample_Constant.getAserSample_Constant().getStudent().setNativeProficiency(proficiency);
        EndOfLevelDialog endOfLevelDialog = new EndOfLevelDialog(this, "End of " + AserSample_Constant.selectedLanguage + " Test");
        endOfLevelDialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setMessage("Student's progress will be lost! Do you still want to continue?");
        builder.setCancelable(false);
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
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

    public void showMistakeCountDialog() {
        mistakCountDialog = new MistakeCountDialog(this, currentLevel);
        mistakCountDialog.show();
    }

    @Override
    public void getMistakeCount(int mistakeCnt) {
        queLevel.setMistakes(mistakeCnt);
        isQueAttempt = false;
        if (currentClick.equals("NEXT")) {
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
        } else if (currentClick.equals("PREVIOUS")) {
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
        } else if (currentClick.equals("PROFICIENCY")) {
            List optionList = new ArrayList();
            optionList.add(getString(R.string.Letter));
            optionList.add(getString(R.string.WordNative));
            optionList.add(getString(R.string.Paragraph));
            optionList.add(getString(R.string.Story));
            optionList.add(getString(R.string.Beginner));
            optionList.add(getString(R.string.TestWasNotComplete));
            ProficiencyDialog proficiencyDialog = new ProficiencyDialog(this, optionList);
            proficiencyDialog.show();
        }
    }

    @Override
    public void onLevelFinish() {
        openNextActivity();
    }
}
