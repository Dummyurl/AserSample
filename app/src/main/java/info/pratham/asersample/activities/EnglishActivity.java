package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import info.pratham.asersample.dialog.ProficiencyDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.fragments.SelectLanguageFragment;
import info.pratham.asersample.interfaces.ProficiencyListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;

public class EnglishActivity extends BaseActivity implements WordsListListener, ProficiencyListener {

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
    @BindView(R.id.mistakes)
    EditText mistakes;
    @BindView(R.id.refreshIV)
    ImageView refreshIcon;
    @BindView(R.id.displayLayout)
    RelativeLayout displayLayout;

    String currentLevel, currentFilePath, currentFileName;
    boolean recording, playing;
    int wordCOunt;
    List<JSONObject> selectedWordsList;


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("students");
        currentFilePath = LanguageActivity.currentFilePath;
        nextItem.setVisibility(View.INVISIBLE);
        prevItem.setVisibility(View.INVISIBLE);
        testType.setText("English" + " Test");
        getData("Capital");
    }

    private void getData(String type) {
        setVisibilityForPrevNext();
        if (type.equalsIgnoreCase("Capital")) {
            mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().getCapitalLetter_mistake());
            previous.setVisibility(View.GONE);
            setNavigation("", getString(R.string.Smallletter));
            currentLevel = getString(R.string.Capitalletter);
        } else if (type.equalsIgnoreCase("Small")) {
            mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().getSmallLetter_mistake());
            if (!previous.isShown())
                previous.setVisibility(View.VISIBLE);
            setNavigation(getString(R.string.Capitalletter), getString(R.string.word));
            currentLevel = getString(R.string.Smallletter);
        } else {
            mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().getWords_mistake());
            if (!next.isShown())
                next.setVisibility(View.VISIBLE);
            setNavigation(getString(R.string.Smallletter), getString(R.string.Sentence));
            currentLevel = getString(R.string.word);
        }

        JSONArray dataArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);
        if (dataArray != null) {
            List<JSONObject> dataList = new ArrayList();
            try {
                for (int i = 0; i < dataArray.length(); i++) {
                    dataList.add(dataArray.getJSONObject(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, dataList, 5);
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
        mistakes.setText(AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().getSentence_mistake());
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

    @OnClick(R.id.next)
    public void next() {
        initiateRecording();
        assignMistakeCount(currentLevel, mistakes.getText().toString());
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

    @OnClick(R.id.previous)
    public void previous() {
        assignMistakeCount(currentLevel, mistakes.getText().toString());
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
        String fileStorePath = currentFilePath + "sample.mp3";
        switch (currentLevel) {
            case "Capital letter":
                fileStorePath = currentFilePath + "Capital letter/";
                currentFileName = selectedWordsList.get(wordCOunt).toString() + ".mp3";
                break;
            case "Small letter":
                fileStorePath = currentFilePath + "Small letter/";
                currentFileName = selectedWordsList.get(wordCOunt).toString() + ".mp3";
                break;
            case "word":
                fileStorePath = currentFilePath + "word/";
                currentFileName = selectedWordsList.get(wordCOunt).toString() + ".mp3";
                break;
            case "Sentence":
                fileStorePath = currentFilePath + "Sentence/";
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
        previous.setText("< " + prevText);
        next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().setEnglishProficiency(proficiency);
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
        AlertDialog builder = new AlertDialog.Builder(this).create();
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

    private void assignMistakeCount(String level, String cnt) {
        switch (level) {
            case "Capital letter":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().setCapitalLetter_mistake(cnt);
                break;
            case "Small letter":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().setSmallLetter_mistake(cnt);
                break;
            case "word":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().setWords_mistake(cnt);
                break;
            case "Sentence":
                AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency().setSentence_mistake(cnt);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setMessage("Do you want to navigate to the Math test");
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
    public void showID() {
        Toast.makeText(this, "" + tv_question.getTag(), Toast.LENGTH_SHORT).show();
    }
}
