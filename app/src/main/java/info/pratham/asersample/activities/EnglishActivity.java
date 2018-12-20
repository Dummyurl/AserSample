package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import info.pratham.asersample.dialog.MistakCountDialog;
import info.pratham.asersample.dialog.ProficiencyDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.fragments.SelectLanguageFragment;
import info.pratham.asersample.interfaces.MistakeCountListener;
import info.pratham.asersample.interfaces.ProficiencyListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;
import nl.dionsegijn.konfetti.KonfettiView;

public class EnglishActivity extends BaseActivity implements WordsListListener, ProficiencyListener, MistakeCountListener {

    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.question)
    TextView tv_question;
    @BindView(R.id.testType)
    TextView testType;
    @BindView(R.id.recordButtonSP)
    Button recordButton;
    @BindView(R.id.nextItem)
    Button nextItem;
    @BindView(R.id.prevItem)
    Button prevItem;
    @BindView(R.id.refreshIV)
    ImageView refreshIcon;
    @BindView(R.id.displayLayout)
    RelativeLayout displayLayout;
    @BindView(R.id.celebrationView)
    KonfettiView celebrationView;

    String currentLevel, currentFilePath, currentFileName;
    boolean recording, playing, isNewQuestion;
    int wordCOunt;
    List<JSONObject> selectedWordsList;

    private DatabaseReference mDatabase;
    List parentDataList;
    QueLevel queLevel;
    List tempSingleQue;


    String currentClick;
    boolean isQueAttemp = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("students");
        parentDataList = AserSample_Constant.getAserSample_Constant().getStudent().getTestQuestionList();
        currentFilePath = LanguageActivity.currentFilePath;
        nextItem.setVisibility(View.INVISIBLE);
        prevItem.setVisibility(View.INVISIBLE);
        testType.setText("English" + " Test");
        getData("Capital");
    }

    private void getData(String type) {
        setVisibilityForPrevNext();
        if (type.equalsIgnoreCase("Capital")) {
            //  mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().getCapitalLetter_mistake());
            previous.setVisibility(View.GONE);
            setNavigation("", getString(R.string.Smallletter));
            currentLevel = getString(R.string.Capitalletter);
        } else if (type.equalsIgnoreCase("Small")) {
            //  mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().getSmallLetter_mistake());
            if (!previous.isShown())
                previous.setVisibility(View.VISIBLE);
            setNavigation(getString(R.string.Capitalletter), getString(R.string.word));
            currentLevel = getString(R.string.Smallletter);
        } else {
            //    mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().getWords_mistake());
            if (!next.isShown())
                next.setVisibility(View.VISIBLE);
            setNavigation(getString(R.string.Smallletter), getString(R.string.Sentence));
            currentLevel = getString(R.string.word);
        }

        initiateJsonProperties();
        JSONArray dataArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);
        if (dataArray != null) {
            List<JSONObject> dataList = new ArrayList();
            try {
                for (int i = 0; i < dataArray.length(); i++) {
                    dataList.add(dataArray.getJSONObject(i));
                }
                final SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, dataList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Problem in getting data!");
        }
    }

    private void getSentences() {
        setVisibilityForPrevNext();
        next.setVisibility(View.GONE);
        setNavigation(getString(R.string.word), "");
        currentLevel = getString(R.string.Sentence);
        // mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().getSentence_mistake());
        initiateJsonProperties();
        JSONArray dataArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);

        try {
            List<JSONObject> listdata = new ArrayList();
            if (dataArray != null) {
                for (int i = 0; i < dataArray.length(); i++) {
                    listdata.add(dataArray.getJSONObject(i));
                }
                getSelectedwords(listdata);
            } else
                Toast.makeText(this, "Problem in getting data", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.markProficiency)
    public void markProficiency() {
        currentClick = "PROFICIENCY";
        if (isQueAttemp)
            showMistakeCountDialog();
        else {
            initiateRecording();
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
    }

    @OnClick(R.id.next)
    public void next() {
        currentClick = "NEXT";
        initiateRecording();
       /* assignMistakeCount(currentLevel, mistakes.getText().toString());*/
        if (isQueAttemp) {
            showMistakeCountDialog();
        } else {
            isQueAttemp = false;
            switch (currentLevel) {
                case "Capital letter":
                    getData("Small");
                    break;
                case "Small letter":
                    getData("words");
                    break;
                case "word":
                    getSentences();
                    break;
            }
        }
    }

    @OnClick(R.id.previous)
    public void previous() {
        currentClick = "PREVIOUS";
        initiateRecording();
//        assignMistakeCount(currentLevel, mistakes.getText().toString());
        if (isQueAttemp)
            showMistakeCountDialog();
        else {
            isQueAttemp = false;
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
        }
    }

    @OnClick(R.id.nextItem)
    public void showNextItem() {
        initiateRecording();
        wordCOunt++;
        showQue(selectedWordsList.get(wordCOunt));
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
        showQue(selectedWordsList.get(wordCOunt));
        if (wordCOunt == 0) {
            prevItem.setVisibility(View.INVISIBLE);
            nextItem.setVisibility(View.VISIBLE);
        }

        if (wordCOunt > -1) {
            nextItem.setVisibility(View.VISIBLE);
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

        if (playing && !recording) {
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
            isQueAttemp = true;
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
        nextItem.setVisibility(View.INVISIBLE);
        prevItem.setVisibility(View.INVISIBLE);
    }

    private void setNavigation(String prevText, String nextText) {
        previous.setText("< " + prevText);
        next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        AserSample_Constant.getAserSample_Constant().getStudent().setEnglishProficiency(proficiency);
        mDatabase.child(AserSample_Constant.getCrlID()).child(AserSample_Constant.getAserSample_Constant().getStudent().getId()).setValue(AserSample_Constant.getAserSample_Constant().getStudent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        AserSampleUtility.showToast(EnglishActivity.this, "Done..");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        AserSampleUtility.showToast(EnglishActivity.this, "FAIL..");
                    }
                });

        AserSampleUtility.writeStudentInJson(this);
        AserSampleUtility.startCelebration(celebrationView);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog builder = new AlertDialog.Builder(EnglishActivity.this).create();
                builder.setMessage("Test successfully submitted");
                builder.setCancelable(false);
                builder.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EnglishActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("fragment", SelectLanguageFragment.class.getSimpleName());
                        startActivity(intent);
                        finishAffinity();
                    }
                });
                builder.show();

            }
        }, 2500);
    }

/*    private void assignMistakeCount(String level, String cnt) {
        switch (level) {
            case "Capital letter":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().setCapitalLetter_mistake(cnt);
                break;
            case "Small letter":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().setSmallLetter_mistake(cnt);
                break;
            case "word":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().setWords_mistake(cnt);
                break;
            case "Sentence":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglish().setSentence_mistake(cnt);
                break;
        }
    }*/

    @Override
    public void onBackPressed() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setMessage("Do you want to navigate to the Math test");
        builder.setCancelable(false);
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EnglishActivity.this, MathActivity.class);
                startActivity(intent);
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
    public void showID() {
        Toast.makeText(this, "" + tv_question.getTag(), Toast.LENGTH_SHORT).show();
    }

    public void initiateJsonProperties() {
        try {
            if (queLevel != null && queLevel.getQuestions().size() > 0) {
                parentDataList.add(queLevel);
            }

            queLevel = new QueLevel();
            queLevel.setSubject("English");
            queLevel.setLevel(currentLevel);
            queLevel.setLevel_seq_cnt(parentDataList.size());
            tempSingleQue = queLevel.getQuestions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMistakeCountDialog() {
        MistakCountDialog mistakCountDialog = new MistakCountDialog(this, currentLevel);
        mistakCountDialog.show();
    }

    @Override
    public void getMistakeCount(int mistakeCnt) {
        queLevel.setMistakes(mistakeCnt);
        isQueAttemp = false;
        if (currentClick.equals("NEXT")) {
            initiateRecording();
            switch (currentLevel) {
                case "Capital letter":
                    getData("Small");
                    break;
                case "Small letter":
                    getData("words");
                    break;
                case "word":
                    getSentences();
                    break;
            }
        } else if (currentClick.equals("PREVIOUS")) {
            initiateRecording();
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
        } else if (currentClick.equals("PROFICIENCY")) {
            initiateJsonProperties();
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
    }
}
