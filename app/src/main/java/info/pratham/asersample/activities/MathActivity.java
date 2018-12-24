package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import info.pratham.asersample.database.modalClasses.QueLevel;
import info.pratham.asersample.database.modalClasses.SingleQustion;
import info.pratham.asersample.dialog.EndOfLevelDialog;
import info.pratham.asersample.dialog.MistakeCountDialog;
import info.pratham.asersample.dialog.PreviewDialog;
import info.pratham.asersample.dialog.ProficiencyDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.fragments.SelectLanguageFragment;
import info.pratham.asersample.fragments.math.CalculationFragment;
import info.pratham.asersample.fragments.math.NumberRecognitionFragment;
import info.pratham.asersample.interfaces.LevelFinishListner;
import info.pratham.asersample.interfaces.MistakeCountListener;
import info.pratham.asersample.interfaces.PreviewDialogListener;
import info.pratham.asersample.interfaces.ProficiencyListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;

public class MathActivity extends BaseActivity implements WordsListListener, ProficiencyListener, MistakeCountListener, LevelFinishListner, PreviewDialogListener {

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
    @BindView(R.id.recordButtonSP)
    Button recordButton;
    @BindView(R.id.displayLayout)
    RelativeLayout displayLayout;

    public String currentLevel;
    String currentFilePath, currentFileName;
    boolean recording, playing;
    public static boolean isNewQuestion;
    NumberRecognitionFragment childFragment;
    CalculationFragment calculationFragment;
    private DatabaseReference mDatabase;


    List parentDataList;
    QueLevel queLevel;
    List tempSingleQue;

    String currentClick;
    boolean isAttempt = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        currentFilePath = LanguageActivity.currentFilePath;
        mDatabase = FirebaseDatabase.getInstance().getReference("students");
        parentDataList = AserSample_Constant.getAserSample_Constant().getStudent().getSequenceList();
        testType.setText("Mathematics Test");
        showSubtraction();
    }

    @OnClick(R.id.markProficiency)
    public void markProficiency() {
        currentClick = "PROFICIENCY";
        switch (currentLevel) {
            case "Subtraction":
                boolean flag = true;
                calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);
                if (calculationFragment != null) {
                    flag = calculationFragment.writeSubtraction();
                }
                if (!flag) {
                    showProficiencyDialog();
                }
                break;
            case "Division":
                boolean flag1 = true;
                calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);
                if (calculationFragment != null)
                    flag1 = calculationFragment.writeDivision();
                if (!flag1)
                    showProficiencyDialog();
                break;
            case "Double digit":
                if (isAttempt) {
                    showMistakeCountDialog();
                } else {
                    showProficiencyDialog();
                }
                break;
            case "Single digit":
                if (isAttempt) {
                    showMistakeCountDialog();
                } else {
                    showProficiencyDialog();
                }
                break;
        }


    }

    public void showProficiencyDialog() {
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
        /*AserSampleUtility.removeFragment(this, CalculationFragment.class.getSimpleName());*/
        currentLevel = getString(R.string.tenToNinetyNine);
        setNavigation(getString(R.string.oneToNine), getString(R.string.Subtraction));
        initiateJsonProperties();
        tv_level.setText("Number Recognition - " + currentLevel);
        JSONArray msg = AserSample_Constant.getMathNumberRecognition(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                /*   mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getTenToNinetyNine_mistake());*/
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5,currentLevel);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void showOneToNine() {
        isAttempt = false;
        recordButton.setVisibility(View.VISIBLE);
        previous.setVisibility(View.GONE);
        currentLevel = getString(R.string.oneToNine);
        setNavigation("", getString(R.string.tenToNinetyNine));
        initiateJsonProperties();
        tv_level.setText("Number Recognition - " + currentLevel);
        JSONArray msg = AserSample_Constant.getMathNumberRecognition(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                /* mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getOneToNine_mistake());*/
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5,currentLevel);
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
        //initiateJsonProperties();
        currentLevel = getString(R.string.Subtraction);
        Bundle bundle = new Bundle();
/*
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getSubtrtaction_mistake());
*/
        tv_level.setText("Basic Operation - " + currentLevel);
        bundle.putString("currentLevel", currentLevel);
        CalculationFragment calculationFragment = new CalculationFragment();
        calculationFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, calculationFragment, CalculationFragment.class.getSimpleName());
    }

    private void showDivision() {
        recordButton.setVisibility(View.INVISIBLE);
        next.setVisibility(View.GONE);
        setNavigation(getString(R.string.Subtraction), "");
        currentLevel = getString(R.string.Division);
        Bundle bundle = new Bundle();
/*
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().getDivision_mistake());
*/
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
        currentClick = "NEXT";
        initiateRecording();
        /*assignMistakeCount(currentLevel, mistakes.getText().toString());*/
        switch (currentLevel) {
            case "Subtraction":
                boolean flag = true;
                calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);
                if (calculationFragment != null) {
                    flag = calculationFragment.writeSubtraction();
                }
                if (!flag) {
                    showDivision();
                }
                break;
            case "Double digit":
                if (isAttempt) {
                    showMistakeCountDialog();
                } else {
                    isAttempt = false;
                    showSubtraction();
                }
                break;
            case "Single digit":
                if (isAttempt) {
                    showMistakeCountDialog();
                } else {
                    isAttempt = false;
                    showTenToNinetyNine();
                }
                break;
        }
    }

    @OnClick(R.id.previous)
    public void previous() {
        currentClick = "PREVIOUS";
        initiateRecording();
        /*     assignMistakeCount(currentLevel, mistakes.getText().toString());*/
        switch (currentLevel) {
            case "Subtraction":
                boolean flag = true;
                calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);
                if (calculationFragment != null) {
                    flag = calculationFragment.writeSubtraction();
                }
                if (!flag)
                    showTenToNinetyNine();
                break;
            case "Division":
                boolean flag1 = true;
                calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);
                if (calculationFragment != null)
                    flag1 = calculationFragment.writeDivision();
                if (!flag1)
                    showSubtraction();
                break;
            case "Double digit":
                if (isAttempt) {
                    showMistakeCountDialog();
                } else {
                    isAttempt = false;
                    showOneToNine();
                }
                break;
        }
    }

    private void setNavigation(String prevText, String nextText) {
        previous.setText("< " + prevText);
        next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        //   AserSample_Constant.getAserSample_Constant().getStudent().getMathematics().setMathProficiency(proficiency);
        AserSample_Constant.getAserSample_Constant().getStudent().setMathematicsProficiency(proficiency);
        switch (currentLevel) {
        /*  case "Division":
                calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);
                calculationFragment.writeDivision();
                break;
            case "Subtraction":
                calculationFragment = (CalculationFragment) getFragmentManager().findFragmentById(R.id.framelayout);
                calculationFragment.writeSubtraction();
                break;*/
            case "Double digit":
                initiateJsonProperties();
                break;
            case "Single digit":
                initiateJsonProperties();
                break;
        }

        EndOfLevelDialog endOfLevelDialog = new EndOfLevelDialog(this, "End of Mathematics Test");
        endOfLevelDialog.show();
    }

    private void openNextActivity() {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(MathActivity.this);
            builder.setMessage(R.string.Navigate)
                    .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(MathActivity.this, EnglishActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            mDatabase.child(AserSample_Constant.getCrlID()).child(AserSample_Constant.getAserSample_Constant().getStudent().getId()).setValue(AserSample_Constant.getAserSample_Constant().getStudent())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Write was successful!
                                            AserSampleUtility.showToast(MathActivity.this, "Done..");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            AserSampleUtility.showToast(MathActivity.this, "FAIL..");
                                        }
                                    });

                            AserSampleUtility.writeStudentInJson(MathActivity.this);

                            PreviewDialog previewDialog = new PreviewDialog(MathActivity.this);
                            previewDialog.show();
                        }
                    });
            builder.create();
            builder.show();
    }

    /*private void assignMistakeCount(String level, String cnt) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void audioStopped() {
        recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        recording = true;
        playing = true;
    }

    public void initiateRecording() {
        AudioUtil.stopRecording();
        AudioUtil.stopPlayingAudio();
        recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.mic_blue_round));
        Fragment fragment = getFragmentManager().findFragmentById(R.id.framelayout);
        if (fragment instanceof NumberRecognitionFragment) {
            childFragment = (NumberRecognitionFragment) fragment;
            childFragment.getRefreshIconView().setVisibility(View.INVISIBLE);
        }
        question.setAlpha(1f);
        playing = false;
        recording = false;
    }

    @OnClick(R.id.recordButtonSP)
    public void startOrStopRecording() {
        childFragment = (NumberRecognitionFragment) getFragmentManager().findFragmentById(R.id.framelayout);

        if (isNewQuestion) {
            isNewQuestion = false;
            currentFileName = updateJsonDetails();
        }

        String fileStorePath = currentFilePath + currentFileName;
        /*switch (currentLevel) {
            case "10-99":
                fileStorePath = currentFilePath + "doubleDigit/";
                currentFileName = childFragment.getQuestionIdByView().get(childFragment.getWordsCount()).toString() + ".mp3";
                break;
            case "1-9":
                fileStorePath = currentFilePath + "singleDigit/";
                currentFileName = childFragment.getQuestionIdByView().get(childFragment.getWordsCount()).toString() + ".mp3";
                break;
        }*/

        File file = new File(currentFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (playing && !recording) {
            //initiateRecording();
        } else if (recording && playing) {
//            recording = false;
            AudioUtil.playRecording(fileStorePath, this);
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.playing_icon));
        } else if (recording) {
            AudioUtil.stopRecording();
            childFragment.getRefreshIconView().setVisibility(View.VISIBLE);
            question.setAlpha(0.5f);
            playing = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
        } else {
            AudioUtil.startRecording(fileStorePath);
            recording = true;
            isAttempt = true;
            recordButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.recording));
        }
    }

    private String updateJsonDetails() {
        String recordingFileName;
        SingleQustion singleQustion = new SingleQustion();
        singleQustion.setQue_seq_cnt(tempSingleQue.size());
        singleQustion.setQue_id(childFragment.getQuestionIdByView());
        singleQustion.setQue_text(childFragment.getQuestionTextByView());
        recordingFileName = queLevel.getLevel_seq_cnt() + "_" + singleQustion.getQue_seq_cnt() + "_" + childFragment.getQuestionIdByView() + ".mp3";
        singleQustion.setRecordingName(recordingFileName);
        tempSingleQue.add(singleQustion);
        return recordingFileName;
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

    public void initiateJsonProperties() {

        try {
            if (queLevel != null && queLevel.getQuestions().size() > 0) {
                parentDataList.add(queLevel);
            }

            queLevel = new QueLevel();
            queLevel.setSubject("Mathematics");
            queLevel.setLevel(currentLevel);
            queLevel.setLevel_seq_cnt(parentDataList.size());
            tempSingleQue = queLevel.getQuestions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMistakeCount(int mistakeCnt) {
        isAttempt = false;
        Fragment fragment = getFragmentManager().findFragmentById(R.id.framelayout);
        if (fragment instanceof CalculationFragment) {
            CalculationFragment childFragment = (CalculationFragment) fragment;
            childFragment.setMistakes(mistakeCnt);
        }
        if (currentClick.equals("NEXT")) {
            switch (currentLevel) {
                case "Subtraction":
                    showDivision();
                    break;
                case "Double digit":
                    queLevel.setMistakes(mistakeCnt);
                    initiateJsonProperties();
                    showSubtraction();
                    break;
                case "Single digit":
                    queLevel.setMistakes(mistakeCnt);
                    initiateJsonProperties();
                    showTenToNinetyNine();
                    break;
            }
        } else if (currentClick.equals("PREVIOUS")) {
            switch (currentLevel) {
                case "Subtraction":
                    showTenToNinetyNine();
                    break;
                case "Division":
                    showSubtraction();
                    break;
                case "Double digit":
                    queLevel.setMistakes(mistakeCnt);
                    showOneToNine();
                    break;
            }
        } else if (currentClick.equals("PROFICIENCY")) {
            switch (currentLevel) {
                case "Subtraction":
                case "Division":
                    showProficiencyDialog();
                    break;
                case "Double digit":
                case "Single digit":
                    queLevel.setMistakes(mistakeCnt);
                    initiateJsonProperties();
                    showProficiencyDialog();
                    break;

            }

        }

    }

    public void showMistakeCountDialog() {
        MistakeCountDialog mistakCountDialog = new MistakeCountDialog(this, currentLevel);
        mistakCountDialog.show();
    }

    @Override
    public void onLevelFinish() {
        openNextActivity();
    }

    @Override
    public void onSubmit() {
        Intent intent = new Intent(MathActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("fragment", SelectLanguageFragment.class.getSimpleName());
        startActivity(intent);
        finishAffinity();
    }
}
