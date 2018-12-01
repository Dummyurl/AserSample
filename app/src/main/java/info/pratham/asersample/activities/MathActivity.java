package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import info.pratham.asersample.fragments.math.CalculationFragment;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

public class MathActivity extends BaseActivity implements WordsListListener {
    @BindView(R.id.question)
    TextView question;
    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        showSubtraction();
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
                    case "Subtraction":
                        showDivision();
                        break;
                    case "Division":
                        //todo proficiency level to division
                        break;
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentLevel) {
                    case "Subtraction":
                        //todo open Number Recognition
                        tenToNinetyNine();
                        break;
                    case "Division":
                        //todo proficiency level to Subtraction
                        break;
                }
            }
        });
        dialog.show();
    }

    private void tenToNinetyNine() {
    //    AserSampleUtility.removeFragment(this, CalculationFragment.class.getSimpleName());
        currentLevel = getString(R.string.tenToNinetyNine);
        JSONArray msg = AserSample_Constant.getMathNumberRecognition(AserSample_Constant.sample, currentLevel);
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

    private void showSubtraction() {
        currentLevel = getString(R.string.Subtraction);
        Bundle bundle = new Bundle();
        bundle.putString("currentLevel", currentLevel);
        CalculationFragment calculationFragment = new CalculationFragment();
        calculationFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, calculationFragment, CalculationFragment.class.getSimpleName());
    }

    private void showDivision() {
        currentLevel = getString(R.string.Division);
        Bundle bundle = new Bundle();
        bundle.putString("currentLevel", currentLevel);
        CalculationFragment calculationFragment = new CalculationFragment();
        calculationFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, calculationFragment, CalculationFragment.class.getSimpleName());
    }

    @Override
    public void getSelectedwords(List list) {
        question.setText(list.toString());
    }
}
