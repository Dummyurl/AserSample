package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

public class LanguageActivity extends BaseActivity implements WordsListListener {
    @BindView(R.id.question)
    TextView tv_question;

    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        //   Student student = new Intent().getParcelableExtra("student");

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
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage("Is This Ok");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentLevel) {
                    case "Story":
                        //todo setProficiency to Story
                        openNextActivity("Story");
                        break;
                    case "Paragraph":
                        showStory();
                        break;
                    case "Word":
                        //todo setProficiency to word
                        openNextActivity("Word");
                        break;
                    case "Letter":
                        //todo setProficiency to Letter
                        openNextActivity("Letter");
                        break;
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentLevel) {
                    case "Story":
                        //todo setProficiency
                        openNextActivity("Paragraph");
                        break;
                    case "Paragraph":
                        showWords();
                        break;
                    case "Word":
                        showLetters();
                        break;
                    case "Letter":
                        //todo setProficiency to beginer
                        openNextActivity("Beginer");
                        break;
                }
            }
        });
        dialog.show();
    }

    private void showParagraph() {
        currentLevel = getString(R.string.Paragraph);
        String msg = AserSample_Constant.getPara(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showStory() {
        currentLevel = getString(R.string.Story);
        String msg = AserSample_Constant.getStory(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showLetters() {
        currentLevel = getString(R.string.Letter);
        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showWords() {
        currentLevel = getString(R.string.Word);
        JSONArray msg = AserSample_Constant.getWords(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList);
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
            showQue(list.toString());
        }
    }
}
