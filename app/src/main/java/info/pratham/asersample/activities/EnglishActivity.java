package info.pratham.asersample.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

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
        getData("Capital");
    }

    private void getData(String type) {
        if (type.equalsIgnoreCase("Capital"))
            currentLevel = getString(R.string.Capitalletter);
        else if (type.equalsIgnoreCase("Small"))
            currentLevel = getString(R.string.Smallletter);
        else
            currentLevel = getString(R.string.word);

        JSONArray dataArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);
        if (dataArray != null) {
            List dataList = new ArrayList();
            try {
                for (int i = 0; i < dataArray.length(); i++) {
                    dataList.add(dataArray.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, dataList);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void getSentences() {
        currentLevel = getString(R.string.Sentence);
        JSONArray dataArray = AserSample_Constant.getEnglishDataByLevel(AserSample_Constant.sample, currentLevel);
        showQue(dataArray.toString());
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
                    case "Capital letter":
                        getData("Small");
                        break;
                    case "Small letter":
                        getData("Words");
                        break;
                    case "word":
                        getSentences();
                        break;
                    case "Sentence":
                        //todo setProficiency to Sentences
                        terminationWorks();
                        break;
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentLevel) {
                    case "Capital letter":
                        // todo set proficiency to beginner
                        break;
                    case "Small letter":
                        // todo set proficiency to Capital letter
                        break;
                    case "word":
                        // todo set proficiency to Small letter
                        break;
                    case "Sentence":
                        //todo setProficiency to Letter
                        break;
                }
                terminationWorks();
            }
        });
        dialog.show();
    }

    private void terminationWorks() {
        // ASER test is completed now do the app termination things like sync and all
    }

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
