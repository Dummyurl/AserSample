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

public class ChekingDialog extends Dialog {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.correct_count)
    TextView correct_count;

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
                correct_count.setText("Result:" + correctCnt + "/5");
                if (correctCnt >= 4) {
                    correct_count.setTextColor(context.getResources().getColor(R.color.green));
                } else {
                    correct_count.setTextColor(context.getResources().getColor(R.color.red));
                }
                break;
            /*---------------- correct  no of mistake <=3 -----------------*/
            case "Para":
            case "Story":
                correct_count.setText("Result:" + correctCnt + "/1");
                if (correctCnt >= 1) {
                    correct_count.setTextColor(context.getResources().getColor(R.color.green));
                } else {

                }
                break;
            case "Sentence":
                //   2/4
                correct_count.setText("Result:" + correctCnt + "/4");
                if (correctCnt >= 2) {
                    correct_count.setTextColor(context.getResources().getColor(R.color.green));
                } else {
                    correct_count.setTextColor(context.getResources().getColor(R.color.red));
                }
                break;
            case "Subtraction":
                //   2/2
                correct_count.setText("Result:" + correctCnt + "/2");
                if (correctCnt >= 2) {
                    correct_count.setTextColor(context.getResources().getColor(R.color.green));
                } else {
                    correct_count.setTextColor(context.getResources().getColor(R.color.red));
                }
                break;
            case "Division":
                correct_count.setText("Result:" + correctCnt + "/1");
                if (correctCnt >= 1) {
                    correct_count.setTextColor(context.getResources().getColor(R.color.green));
                } else {
                    correct_count.setTextColor(context.getResources().getColor(R.color.red));
                }
                break;
        }
    }

    private int checkRightCount(List<QuestionStructure> questionList) {
        int correctCnt = 0;
        for (QuestionStructure questionStructure : questionList) {
            if (questionStructure.getAzure_Scored_Labels().trim().equals("1")) {
                correctCnt++;
            }
        }
        return correctCnt;
    }

}
