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
import info.pratham.asersample.database.modalClasses.QueLevel;
import info.pratham.asersample.database.modalClasses.SingleQustion;
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
    @BindView(R.id.refreshIV)
    ImageView refreshIcon;
    @BindView(R.id.displayLayout)
    RelativeLayout displayLayout;
    @BindView(R.id.mistakes)
    EditText mistakes;

    public static String currentFilePath,currentFileName;
    String currentLevel;
    boolean recording, playing, isNewQuestion;
    int wordCOunt;
    List<JSONObject> selectedWordsList;

    List parentDataList;
    QueLevel queLevel;
    List tempSingleQue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";

        nextItem.setVisibility(View.INVISIBLE);
        prevItem.setVisibility(View.INVISIBLE);
        testType.setText(AserSample_Constant.selectedLanguage + " Test");
        parentDataList = AserSample_Constant.getAserSample_Constant().getStudent().getTestQuestionList();
        String question = databaseInstance.getQuestiondao().getLanguageQuestions(AserSample_Constant.selectedLanguage);
        try {
            if (question != null) {
                JSONObject questionJson = new JSONObject(question);
                int randomNo = ASERApplication.getRandomNumber(0, questionJson.length());
                // AserSample_Constant.sample = (JSONObject) questionJson.get("Sample" + (randomNo + 1));
                //todo remove hardcoded sample
                AserSample_Constant.sample = (JSONObject) questionJson.get("Sample1");
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
        next.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.Word), getString(R.string.Story));
        currentLevel = getString(R.string.Paragraph);
        setVisibilityForPrevNext();
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().getParagragh_mistake());
        JSONObject msg = AserSample_Constant.getPara(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            tv_question.setTextSize(1, 30);
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showStory() {
        next.setVisibility(View.GONE);
        setNavigation(getString(R.string.Paragraph), "");
        currentLevel = getString(R.string.Story);
        setVisibilityForPrevNext();
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().getStory_mistake());
        JSONObject msg = AserSample_Constant.getStory(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            tv_question.setTextSize(1, 25);
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showLetters() {
        previous.setVisibility(View.GONE);
        setNavigation("", getString(R.string.Word));
        currentLevel = getString(R.string.Letter);
        setVisibilityForPrevNext();

        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().getLetter_mistake());
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
        previous.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.Letter), getString(R.string.Paragraph));
        currentLevel = getString(R.string.Word);
        setVisibilityForPrevNext();
        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.get(i));
                }
                mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().getWord_mistake());
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showQue(JSONObject msg) {
        try {
            isNewQuestion = true;
            tv_question.setText(msg.getString("data"));
            tv_question.setTag(msg.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            nextItem.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.prevItem)
    public void showPrevItem() {
        initiateRecording();
        wordCOunt--;
        showQue(selectedWordsList.get(wordCOunt));
        if (wordCOunt == 0) {
            prevItem.setVisibility(View.INVISIBLE);
            nextItem.setVisibility(View.VISIBLE);
        }
        if (wordCOunt > -1) {
            nextItem.setVisibility(View.VISIBLE);
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
        if (isNewQuestion) {
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
            AudioUtil.playRecording(fileStorePath, this);
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.playing_icon));
        } else if (recording && !playing) {
            AudioUtil.stopRecording();
            refreshIcon.setVisibility(View.VISIBLE);
            tv_question.setAlpha(0.5f);
            playing = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        } else {
            AudioUtil.startRecording(fileStorePath);
            recording = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recording));
        }
    }

    private String updateJsonDetails() {
        String recordingFileName;
        SingleQustion singleQustion = new SingleQustion();
        singleQustion.setQue_seq_cnt(tempSingleQue.size());
        singleQustion.setQue_id(tv_question.getTag().toString());
        recordingFileName = queLevel.getLevel_seq_cnt() + "_" + singleQustion.getQue_seq_cnt() + "_" + tv_question.getTag().toString()+".mp3";
        singleQustion.setRecordingName(recordingFileName);
        tempSingleQue.add(singleQustion);
        return recordingFileName;
    }

    private void setVisibilityForPrevNext() {
        nextItem.setVisibility(View.INVISIBLE);
        prevItem.setVisibility(View.INVISIBLE);
        // Initiate question level
        if (queLevel != null && queLevel.getQuestions().size() > 0) {
            parentDataList.add(queLevel);
        }
        queLevel = new QueLevel();
        queLevel.setLevel(currentLevel);
        queLevel.setLevel_seq_cnt(parentDataList.size());
        tempSingleQue = queLevel.getQuestions();
    }

    private void setNavigation(String prevText, String nextText) {
        if (previous.isShown())
            previous.setText("< " + prevText);
        if (next.isShown())
            next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        setVisibilityForPrevNext();
        AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().setProficiency(proficiency);
        openNextActivity(proficiency);
    }

    private void assignMistakeCount(String level, String cnt) {
        switch (level) {
            case "Story":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().setStory_mistake(cnt);
                break;
            case "Paragraph":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().setParagragh_mistake(cnt);
                break;
            case "Word":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().setWord_mistake(cnt);
                break;
            case "Letter":
                AserSample_Constant.getAserSample_Constant().getStudent().getNativeLanguage().setLetter_mistake(cnt);
                break;
        }
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

    @OnClick(R.id.question)
    public void show_tag() {
        AserSampleUtility.showToast(this, tv_question.getTag().toString());
    }
}
