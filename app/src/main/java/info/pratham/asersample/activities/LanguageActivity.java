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
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

public class LanguageActivity extends BaseActivity implements WordsListListener {
    @BindView(R.id.question)
    TextView tv_question;
    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;

    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        List optionList = new ArrayList();
        optionList.add(getString(R.string.Letter));
        optionList.add(getString(R.string.Word));
        optionList.add(getString(R.string.Paragraph));
        optionList.add(getString(R.string.Story));
        optionList.add(getString(R.string.Beginner));
        optionList.add(getString(R.string.TestWasNotComplete));

        ProficiencyDialog proficiencyDialog = new ProficiencyDialog(this, optionList);
        proficiencyDialog.show();

        /*AlertDialog dialog = new AlertDialog.Builder(this).create();
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
        dialog.show();*/
    }

    private void showParagraph() {
        if (!next.isShown())
            next.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.Word), getString(R.string.Story));
        currentLevel = getString(R.string.Paragraph);
        String msg = AserSample_Constant.getPara(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showStory() {
        if (next.isShown())
            next.setVisibility(View.GONE);
        setNavigation(getString(R.string.Paragraph), "");
        currentLevel = getString(R.string.Story);
        String msg = AserSample_Constant.getStory(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            showQue(msg);
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showLetters() {
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

    private void setNavigation(String prevText, String nextText) {
        if (previous.isShown())
            previous.setText("< " + prevText);
        if (next.isShown())
            next.setText(nextText + " >");
    }
}
