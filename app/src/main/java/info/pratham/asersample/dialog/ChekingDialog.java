package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.adapters.RecyclerViewAdapter;
import info.pratham.asersample.adapters.RecyclerViewCheckAnswerAdapter;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.database.modalClasses.SingleQustioNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.interfaces.CheckQuestionListener;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

public class ChekingDialog extends Dialog {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

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
                List<SingleQustioNew> temp = studentNew.getSequenceList();
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
}
