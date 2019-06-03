package info.pratham.asersample.animation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.database.modalClasses.SingleQustioNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.interfaces.GetTimeListener;
import info.pratham.asersample.interfaces.RefreshRecycler;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;
import info.pratham.asersample.utility.ListConstant;

public class EnlaegeView extends Dialog {
    @BindView(R.id.text)
    TextView textView;

    @BindView(R.id.number1)
    TextView number1;

    @BindView(R.id.number2)
    TextView number2;

    @BindView(R.id.instruction)
    TextView instruction;


    @BindView(R.id.sun_ans)
    EditText sun_ans;


    @BindView(R.id.quotient)
    EditText quotient;

    @BindView(R.id.remainder)
    EditText remainder;
    @BindView(R.id.divisor)
    TextView divisor;
    @BindView(R.id.dividend)
    TextView dividend;

   /* @BindView(R.id.quotient)
    EditText quotient_ans;

    @BindView(R.id.remainder)
    EditText remainder_ans;*/

    Dialog dialogParent;

    @BindView(R.id.reading_task)
    RelativeLayout reading_task;

    @BindView(R.id.mathematics_operation)
    RelativeLayout mathematics_operation;

    @BindView(R.id.mathematics_division)
    RelativeLayout mathematics_division;
    String que_text;
    String level;
    String que_id;
    Context mContext;
    String startTime, endTime;
    String answer, remainder_ans;
    boolean isAttemptedQue = false;
    GetTimeListener getTimeListener;
    RefreshRecycler refreshRecycler;
    QuestionStructure questionStructure;

    public EnlaegeView(@NonNull Context context, QuestionStructure questionStructure, String level, boolean isAttemptedQue, RecyclerView.Adapter recyclerViewAdapter) {
        super(context, R.style.Transparent);
        this.que_text = questionStructure.getData();
        this.level = level;
        this.que_id = questionStructure.getId();
        this.questionStructure = questionStructure;
        this.mContext = context;
        this.isAttemptedQue = isAttemptedQue;
        getTimeListener = (GetTimeListener) context;
        refreshRecycler = (RefreshRecycler) recyclerViewAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transperentbg)));
        setContentView(R.layout.activity_enlaege_view);
        ButterKnife.bind(this);
        dialogParent = this;
        startTime = getTimeListener.getTime();
        if (level.equals(mContext.getString(R.string.Subtraction))) {
            reading_task.setVisibility(View.GONE);
            mathematics_division.setVisibility(View.GONE);
            mathematics_operation.setVisibility(View.VISIBLE);
            String[] numbers = que_text.split("-");
            if (numbers.length == 2) {
                number1.setText(numbers[0].trim());
                number2.setText(numbers[1].trim());
                if (isAttemptedQue) {
                    Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
                    List<SingleQustioNew> temp = studentNew.getSequenceList();
                    //if question is old then remove old entry
                    if (isAttemptedQue) {
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).getQue_id().equals(que_id)) {
                                sun_ans.setText(temp.get(i).getAnswer());
                                break;
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "Some thing went wrong..", Toast.LENGTH_SHORT).show();
            }

        } else if (level.equals(mContext.getString(R.string.Division))) {
            reading_task.setVisibility(View.GONE);
            mathematics_operation.setVisibility(View.GONE);
            mathematics_division.setVisibility(View.VISIBLE);
            String[] numbers = que_text.split("/");
            if (numbers.length == 2) {
                dividend.setText(numbers[0].trim());
                divisor.setText(numbers[1].trim());
                if (isAttemptedQue) {
                    Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
                    List<SingleQustioNew> temp = studentNew.getSequenceList();
                    //if question is old then remove old entry
                    if (isAttemptedQue) {
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).getQue_id().equals(que_id)) {
                                quotient.setText(temp.get(i).getAnswer());
                                remainder.setText(temp.get(i).getRemainder());
                                break;
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "Some thing went wrong..", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (level.equals("Double") || level.equals("Single")) {
                instruction.setText("Read above number loudly and press back");
            } else {
                instruction.setText("Read above text loudly and press back");
            }

            reading_task.setVisibility(View.VISIBLE);
            mathematics_operation.setVisibility(View.GONE);
            mathematics_division.setVisibility(View.GONE);
            textView.setText(que_text);
            if (level.equals("Para") || level.equals("Sentence")) {
                textView.setTextSize(1, 50);
            } else if (level.equals("Story")) {
                textView.setTextSize(1, 30);
            }
        }
    }


    @OnClick(R.id.close)
    public void closeBtb() {
        /*Intent intent = new Intent();
        setResult(2, intent);
        finish();*/
        addEntry();
    }

    @Override
    public void onBackPressed() {
        addEntry();
    }

    private void addEntry() {
        AudioUtil.stopRecording(mContext);
        Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
        List<SingleQustioNew> temp = studentNew.getSequenceList();

        //if question is old then remove old entry
        if (isAttemptedQue) {
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getQue_id().equals(que_id)) {
                    temp.remove(i);
                    questionStructure.setIsCorrect(AserSample_Constant.NOTATTEMPED);
                    questionStructure.setNoOfMistakes(null);
                    break;
                }
            }
        }

        // endTime = getTimeListener.getTime();
        SingleQustioNew singleQustioNew = new SingleQustioNew();
        singleQustioNew.setQue_id(que_id);
        singleQustioNew.setQue_text(que_text);
        //Disable start and end time
        // singleQustioNew.setStartTime(startTime);
        //singleQustioNew.setEndTime(endTime);
        if (level.equals(mContext.getString(R.string.Subtraction))) {
            String subtraction = sun_ans.getText().toString();
            if (subtraction.isEmpty()) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Answer is empty do you want to leave it?")
                        .setPositiveButton("yes", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                questionStructure.setSelected(false);
                                refreshRecycler.refreshRecycler();
                                dialogParent.dismiss();
                            }
                        }).setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            } else {
                singleQustioNew.setAnswer(subtraction);
                addQuestionToAnswerList(studentNew, singleQustioNew);
                dialogParent.dismiss();
            }
        } else if (level.equals(mContext.getString(R.string.Division))) {
            String quotient_ans = quotient.getText().toString();
            String remainder_ans = remainder.getText().toString();
            if (!quotient_ans.isEmpty() || !remainder_ans.isEmpty()) {
                singleQustioNew.setAnswer(quotient_ans);
                singleQustioNew.setRemainder(remainder_ans);
                addQuestionToAnswerList(studentNew, singleQustioNew);
                dialogParent.dismiss();
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Answer is empty do you want to leave it?")
                        .setPositiveButton("yes", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                questionStructure.setSelected(false);
                                refreshRecycler.refreshRecycler();
                                dialogParent.dismiss();
                            }
                        }).setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        } else {
            singleQustioNew.setRecordingName(que_id + ".mp3");
            addQuestionToAnswerList(studentNew, singleQustioNew);
            dialogParent.dismiss();
        }
    }

    private void addQuestionToAnswerList(Student studentNew, SingleQustioNew singleQustioNew) {

        studentNew.getSequenceList().add(singleQustioNew);
        //if quetion is new then only increment counter
        if (!isAttemptedQue) {
            switch (level) {
                //NATIVE LANGUAGE
                case "Words":
                    ListConstant.Words_cnt++;
                    break;
                case "Letters":
                    ListConstant.Letters_cnt++;
                    break;
                case "Para":
                    ListConstant.Para_cnt++;
                    break;
                case "Story":
                    ListConstant.Story_cnt++;
                    break;

                case "Capital":
                    ListConstant.Capital_cnt++;
                    break;
                case "Small":
                    ListConstant.Small_cnt++;
                    break;
                case "word":
                    ListConstant.engWord_cnt++;
                    break;
                case "Sentence":
                    ListConstant.Sentence_cnt++;
                    break;
                //MATHEMATICS
                case "Single":
                    ListConstant.Single_cnt++;
                    break;
                case "Double":
                    ListConstant.Double_cnt++;
                    break;
                case "Subtraction":
                    ListConstant.Subtraction_cnt++;
                    break;
                case "Division":
                    ListConstant.Division_cnt++;
                    break;
            }
        }
    }
}
