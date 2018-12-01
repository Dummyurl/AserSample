package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

public class EnglishActivity extends BaseActivity implements WordsListListener {

    @BindView(R.id.question)
    TextView tv_question;

    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        showCapitalLetters();
    }

    private void showCapitalLetters() {
        currentLevel = getString(R.string.Capitalletter);
        JSONArray lettersArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);
        if (lettersArray != null) {
            List letterList = new ArrayList();
            try {
                for (int i = 0; i < lettersArray.length(); i++) {
                    letterList.add(lettersArray.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, letterList);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showSmallLetters() {
        currentLevel = getString(R.string.Smallletter);
    }

    /*@OnClick(R.id.markProficiency)
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
    }*/

    private void showQue(String msg) {
        tv_question.setText(msg);
    }

    @Override
    public void getSelectedwords(List list) {
        if (!list.isEmpty()) {
            showQue(list.toString());
        }
    }

}
