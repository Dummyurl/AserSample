package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.adapters.RecyclerViewCheckAnswerAdapter;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.database.modalClasses.SingleQuestionNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.interfaces.CheckQuestionListener;
import info.pratham.asersample.utility.AserSample_Constant;

import static java.lang.Float.NaN;
import static java.lang.Float.isNaN;

public class ChekingDialog extends Dialog {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.correct_count)
    TextView correct_count;
    @BindView(R.id.level_status)
    TextView level_status;
    @BindView(R.id.score_probability)
    TextView score_probability;
    @BindView(R.id.score_status)
    LinearLayout score_status;

    List<QuestionStructure> qustionList;
    List<QuestionStructure> tempList;
    Context context;
    String subject, calledFrom, level;
    CheckQuestionListener checkQuestionListener;

    public ChekingDialog(@NonNull Context context, List qustionList, String subject, String level, String calledFrom) {
        super(context);
        this.context = context;
        this.qustionList = qustionList;
        this.subject = subject;
        this.level = level;
        this.calledFrom = calledFrom;
        checkQuestionListener = (CheckQuestionListener) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheking_dialog);
        setCancelable(false);
        ButterKnife.bind(this);
        tempList = new ArrayList<>();
        for (QuestionStructure questionStructure : qustionList) {
            if (questionStructure.isSelected()) {
                tempList.add(questionStructure);
            }
        }
        checkDecisionDialogShowOrNot(level, tempList);
        initRecycler();
    }

    private void initRecycler() {
        if (level.equals("Para") || level.equals("Story") || level.equals("Sentence")) {
            final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            if (getScreenWidthDp() >= 1200) {
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
                recyclerView.setLayoutManager(gridLayoutManager);
            } else if (getScreenWidthDp() >= 400) {
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                recyclerView.setLayoutManager(gridLayoutManager);
            } else {
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
        }
        RecyclerViewCheckAnswerAdapter recyclerViewCheckAnswerAdapter = new RecyclerViewCheckAnswerAdapter(context, tempList, level);
        recyclerView.setAdapter(recyclerViewCheckAnswerAdapter);
    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    @OnClick(R.id.submitbtn)
    public void submit() {
        boolean allQuetionChecked = true;
        int noOfMistakes = 0;
        for (QuestionStructure questionStructure : qustionList) {
            if (questionStructure.isSelected()) {
                if (questionStructure.getIsCorrect().equals(AserSample_Constant.NOTATTEMPED)) {
                    allQuetionChecked = false;
                    break;
                }
                if (level.equals("Para") || level.equals("Story") || level.equals("Sentence")) {
                    if (questionStructure.getNoOfMistakes() == null || questionStructure.getNoOfMistakes().isEmpty()) {
                        allQuetionChecked = false;
                        break;
                    }
                }
            }
        }
        if (allQuetionChecked) {
            //calculate no of mistakes for word letter
            for (QuestionStructure questionStructure : qustionList) {
                if (questionStructure.getIsCorrect().equals(AserSample_Constant.WRONG)) {
                    noOfMistakes++;
                }
            }

            if (!level.equals("Para") && !level.equals("Story") && !level.equals("Sentence")) {
                Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
                List<SingleQuestionNew> temp = studentNew.getSequenceList();
                for (QuestionStructure questionStructure : qustionList) {
                    if (questionStructure.isSelected()) {
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                                temp.get(i).setNoOfMistakes(String.valueOf(noOfMistakes));
                                questionStructure.setNoOfMistakes(String.valueOf(noOfMistakes));
                                break;
                            }
                        }
                    }
                }
            }
            checkQuestionListener.onSubmitListener(subject, false, calledFrom);
            dismiss();
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Check all Questions and fill no of mistakes if required");
            alertDialog.setPositiveButton("OK", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.create().show();
        }
    }

    private void checkDecisionDialogShowOrNot(String level, List<QuestionStructure> questionList) {
        int correctCnt = checkRightCount(questionList);
        float scoredProbabilities = checkScoredProbabilities(questionList);
        if (!isNaN(scoredProbabilities)) {
            switch (level) {
                /*---------------- correct 4/5-----------------*/
                //NATIVE LANGUAGE
                case "Words":
                case "Letters":
                    //ENGLISH Language
                case "Capital":
                case "Small":
                case "word":
                    //MATHEMATICS
                case "Single":
                case "Double":

                    if (correctCnt >= 4) {
                        setSCore(correctCnt, 5, true, scoredProbabilities);
                    } else {
                        setSCore(correctCnt, 5, false, scoredProbabilities);
                    }
                    break;
                /*---------------- correct  no of mistake <=3 -----------------*/
                case "Para":
                case "Story":
                case "Division":
                    if (correctCnt >= 1) {
                        setSCore(correctCnt, 1, true, scoredProbabilities);
                    } else {
                        setSCore(correctCnt, 1, false, scoredProbabilities);
                    }
                    break;
                case "Sentence":
                    //   2/4
                    if (correctCnt >= 2) {
                        setSCore(correctCnt, 4, true, scoredProbabilities);
                    } else {
                        setSCore(correctCnt, 4, false, scoredProbabilities);
                    }
                    break;
                case "Subtraction":
                    //   2/2
                    if (correctCnt >= 2) {
                        setSCore(correctCnt, 2, true, scoredProbabilities);
                    } else {
                        setSCore(correctCnt, 2, false, scoredProbabilities);
                    }
                    break;
          /*  case "Division":
                if (correctCnt >= 1) {
                    setSCore(correctCnt, 1, true,scoredProbabilities);
                } else {
                    setSCore(correctCnt, 1, false,scoredProbabilities);
                }
                break;*/
            }
        } else {
            score_status.setVisibility(View.GONE);
        }
    }

    private float checkScoredProbabilities(List<QuestionStructure> questionList) {
        float sumOfScoredProbabilities = 0;
        float totalCnt = 0;
        for (QuestionStructure questionStructure : questionList) {
            try {
                sumOfScoredProbabilities += Float.parseFloat(questionStructure.getModel_Scored_Probabilities());
                totalCnt++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            float a = sumOfScoredProbabilities / totalCnt;
            return a;
        } catch (Exception e) {
            return 0;
        }
    }

    private void setSCore(int correctCnt, int outof, boolean result, float scoredProbabilities) {
        correct_count.setText(": " + correctCnt + "/" + outof);
        score_probability.setText(": " + scoredProbabilities*100 + "%");
        if (result) {
            level_status.setText(": " + context.getString(R.string.Pass));
            level_status.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            level_status.setText(": " + context.getString(R.string.Fail));
            level_status.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    private int checkRightCount(List<QuestionStructure> questionList) {
        int correctCnt = 0;
        for (QuestionStructure questionStructure : questionList) {
            if (questionStructure.getModel_Scored_Labels() != null && questionStructure.getModel_Scored_Labels().trim().equals("1")) {
                correctCnt++;
            }
        }
        return correctCnt;
    }

}
