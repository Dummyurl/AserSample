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
import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.dialog.ProficiencyDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.fragments.math.CalculationFragment;
import info.pratham.asersample.fragments.math.NumberRecognitionFragment;
import info.pratham.asersample.interfaces.ProficiencyListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;

public class MathActivity extends BaseActivity implements WordsListListener, ProficiencyListener {

    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.testType)
    TextView testType;
    @BindView(R.id.level)
    TextView tv_level;
    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.mistakes)
    EditText mistakes;
    @BindView(R.id.recordButtonSP)
    Button recordButton;
    @BindView(R.id.refreshIV)
    ImageView refreshIcon;
    @BindView(R.id.displayLayout)
    RelativeLayout displayLayout;


    public String currentLevel;
    String currentFilePath, currentFileName;
    boolean recording, playing;
    NumberRecognitionFragment childFragment;
    CalculationFragment calculationFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        currentFilePath = LanguageActivity.currentFilePath;
        testType.setText("Mathematics Test");
        showSubtraction();
    }


    @OnClick(R.id.markProficiency)
    public void markProficiency() {
        List optionList = new ArrayList();
        optionList.add(getString(R.string.oneToNine));
        optionList.add(getString(R.string.tenToNinetyNine));
        optionList.add(getString(R.string.Subtraction));
        optionList.add(getString(R.string.Division));
        optionList.add(getString(R.string.Beginner));
        optionList.add(getString(R.string.TestWasNotComplete));

        ProficiencyDialog proficiencyDialog = new ProficiencyDialog(this, optionList);
        proficiencyDialog.show();
    }

    private void showTenToNinetyNine() {
        recordButton.setVisibility(View.VISIBLE);
        previous.setVisibility(View.VISIBLE);

        AserSampleUtility.removeFragment(this, CalculationFragment.class.getSimpleName());
        currentLevel = getString(R.string.tenToNinetyNine);
        setNavigation(getString(R.string.oneToNine), getString(R.string.Subtraction));
        tv_level.setText("Number Recognition - " + currentLevel);
        JSONArray msg = AserSample_Constant.getMathNumberRecognition(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getTenToNinetyNine_mistake());
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void showOneToNine() {
        recordButton.setVisibility(View.VISIBLE);
        previous.setVisibility(View.GONE);
        currentLevel = getString(R.string.oneToNine);
        setNavigation("", getString(R.string.tenToNinetyNine));
        tv_level.setText("Number Recognition - " + currentLevel);
        JSONArray msg = AserSample_Constant.getMathNumberRecognition(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getOneToNine_mistake());
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void showSubtraction() {
        recordButton.setVisibility(View.INVISIBLE);
        next.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.tenToNinetyNine), getString(R.string.Division));
        currentLevel = getString(R.string.Subtraction);
        Bundle bundle = new Bundle();
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getSubtrtaction_mistake());
        tv_level.setText("Basic Operation - " + currentLevel);
        bundle.putString("currentLevel", currentLevel);
        CalculationFragment calculationFragment = new CalculationFragment();
        calculationFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, calculationFragment, CalculationFragment.class.getSimpleName());
    }

    private void showDivision() {
        calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);

        calculationFragment.writeSubtraction();
        recordButton.setVisibility(View.INVISIBLE);
        next.setVisibility(View.GONE);
        setNavigation(getString(R.string.Subtraction), "");
        currentLevel = getString(R.string.Division);
        Bundle bundle = new Bundle();
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getDivision_mistake());
        tv_level.setText("Basic Operation - " + currentLevel);
        bundle.putString("currentLevel", currentLevel);
        CalculationFragment calculationFragment = new CalculationFragment();
        calculationFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, calculationFragment, CalculationFragment.class.getSimpleName());
    }

    @Override
    public void getSelectedwords(List list) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", new ArrayList<>(list));
        NumberRecognitionFragment numberRecognitionFragment = new NumberRecognitionFragment();
        numberRecognitionFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, numberRecognitionFragment, NumberRecognitionFragment.class.getSimpleName());
    }

    @OnClick(R.id.next)
    public void next() {
        initiateRecording();
        assignMistakeCount(currentLevel, mistakes.getText().toString());
        switch (currentLevel) {
            case "Subtraction":
                showDivision();
                break;
            case "Double digit":
                showSubtraction();
                break;
            case "Single digit":
                showTenToNinetyNine();
                break;
        }
    }

    @OnClick(R.id.previous)
    public void previous() {
        initiateRecording();
        assignMistakeCount(currentLevel, mistakes.getText().toString());
        switch (currentLevel) {
            case "Subtraction":
                showTenToNinetyNine();
                break;
            case "Division":
                showSubtraction();
                break;
            case "Double digit":
                showOneToNine();
                break;
        }
    }

    private void setNavigation(String prevText, String nextText) {
        previous.setText("< " + prevText);
        next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().setMathProficiency(proficiency);
        openNextActivity(proficiency);
    }

    private void openNextActivity(String proficiency) {
        AserSampleUtility.showToast(this, AserSample_Constant.selectedLanguage + "_" + proficiency);
        Intent intent = new Intent(this, EnglishActivity.class);
        startActivity(intent);
    }

    private void assignMistakeCount(String level, String cnt) {
        switch (level) {
            case "Division":
                AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().setDivision_mistake(cnt);
                break;
            case "Subtraction":
                AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().setSubtrtaction_mistake(cnt);
                break;
            case "Double digit":
                AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().setTenToNinetyNine_mistake(cnt);
                break;
            case "Single digit":
                AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().setOneToNine_mistake(cnt);
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
        question.setAlpha(1f);
        playing = false;
        recording = false;
    }

    @OnClick(R.id.refreshIV)
    public void refreshRecording() {
        initiateRecording();
    }

    @OnClick(R.id.recordButtonSP)
    public void startOrStopRecording() {
        childFragment = (NumberRecognitionFragment) getFragmentManager().findFragmentById(R.id.framelayout);
        String fileStorePath = currentFilePath + "sample.mp3";
        switch (currentLevel) {
            case "10-99":
                fileStorePath = currentFilePath + "doubleDigit/";
                currentFileName = childFragment.getWordsList().get(childFragment.getWordsCount()).toString() + ".mp3";
                break;
            case "1-9":
                fileStorePath = currentFilePath + "singleDigit/";
                currentFileName = childFragment.getWordsList().get(childFragment.getWordsCount()).toString() + ".mp3";
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
            question.setAlpha(0.5f);
            playing = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        } else {
            AudioUtil.startRecording(fileStorePath + currentFileName);
            recording = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recording));
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setMessage("You Want navigate to Native Language test");
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
