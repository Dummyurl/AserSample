package info.pratham.asersample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import info.pratham.asersample.dialog.ProficiencyDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.fragments.math.CalculationFragment;
import info.pratham.asersample.fragments.math.NumberRecognitionFragment;
import info.pratham.asersample.interfaces.ProficiencyListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

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

    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
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
        if (!previous.isShown()) {
            previous.setVisibility(View.VISIBLE);
        }
        AserSampleUtility.removeFragment(this, CalculationFragment.class.getSimpleName());
        currentLevel = getString(R.string.tenToNinetyNine);
        setNavigation(getString(R.string.oneToNine), getString(R.string.Subtraction));
        tv_level.setText("Number Recognition - " + currentLevel);
        JSONArray msg = AserSample_Constant.getMathNumberRecognition(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showOneToNine() {
        if (previous.isShown()) {
            previous.setVisibility(View.GONE);
        }
        currentLevel = getString(R.string.oneToNine);
        setNavigation("", getString(R.string.tenToNinetyNine));
        tv_level.setText("Number Recognition - " + currentLevel);
        JSONArray msg = AserSample_Constant.getMathNumberRecognition(AserSample_Constant.sample, currentLevel);
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(this, wordList, 5);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(this, "Something goes Wrong");
        }
    }

    private void showSubtraction() {
        if (!next.isShown())
            next.setVisibility(View.VISIBLE);
        setNavigation(getString(R.string.tenToNinetyNine), getString(R.string.Division));
        currentLevel = getString(R.string.Subtraction);
        Bundle bundle = new Bundle();
        tv_level.setText("Basic Operation - " + currentLevel);
        bundle.putString("currentLevel", currentLevel);
        CalculationFragment calculationFragment = new CalculationFragment();
        calculationFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, calculationFragment, CalculationFragment.class.getSimpleName());
    }

    private void showDivision() {
        if (next.isShown())
            next.setVisibility(View.GONE);
        setNavigation(getString(R.string.Subtraction), "");
        currentLevel = getString(R.string.Division);
        Bundle bundle = new Bundle();
        tv_level.setText("Basic Operation - " + currentLevel);
        bundle.putString("currentLevel", currentLevel);
        CalculationFragment calculationFragment = new CalculationFragment();
        calculationFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, calculationFragment, CalculationFragment.class.getSimpleName());
    }

    @Override
    public void getSelectedwords(List list) {
       /* if(currentLevel.equals(getString(R.string.tenToNinetyNine)))
        question.setText(list.toString());*/
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", new ArrayList<>(list));
        NumberRecognitionFragment numberRecognitionFragment = new NumberRecognitionFragment();
        numberRecognitionFragment.setArguments(bundle);
        AserSampleUtility.showFragment(this, numberRecognitionFragment, NumberRecognitionFragment.class.getSimpleName());
    }

    @OnClick(R.id.next)
    public void next() {
        switch (currentLevel) {
            case "Subtraction":
                assignMistakeCount(currentLevel, mistakes.getText().toString());
                showDivision();
                break;
            case "10-99":
                assignMistakeCount(currentLevel, mistakes.getText().toString());
                showSubtraction();
                break;
            case "1-9":
                assignMistakeCount(currentLevel, mistakes.getText().toString());
                showTenToNinetyNine();
                break;
        }
    }

    @OnClick(R.id.previous)
    public void previous() {
        switch (currentLevel) {
            case "Subtraction":
                assignMistakeCount(currentLevel, mistakes.getText().toString());
                showTenToNinetyNine();
                break;
            case "Division":
                assignMistakeCount(currentLevel, mistakes.getText().toString());
                showSubtraction();
                break;
            case "10-99":
                assignMistakeCount(currentLevel, mistakes.getText().toString());
                showOneToNine();
                break;

        }
    }

    private void setNavigation(String prevText, String nextText) {
        if (previous.isShown())
            previous.setText("< " + prevText);
        if (next.isShown())
            next.setText(nextText + " >");
    }

    @Override
    public void getProficiency(String proficiency) {
        AserSample_Constant.getAserSample_Constant().getStudent().getMathProficiency().setMathProficiency(proficiency);
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
                AserSample_Constant.getAserSample_Constant().getStudent().getMathProficiency().setDivision_mistake(cnt);
                break;
            case "Subtraction":
                AserSample_Constant.getAserSample_Constant().getStudent().getMathProficiency().setSubtrtaction_mistake(cnt);
                break;
            case "10-99":
                AserSample_Constant.getAserSample_Constant().getStudent().getMathProficiency().setTenToNinetyNine_mistake(cnt);
                break;
            case "1-9":
                AserSample_Constant.getAserSample_Constant().getStudent().getMathProficiency().setOneToNine_mistake(cnt);
                break;
        }
    }

    /*private void getMistakeCount(int cnt) {

    }*/
}
