package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.adapters.RecyclerViewAdapter;
import info.pratham.asersample.adapters.RecyclerViewCheckAnswerAdapter;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.interfaces.CheckQuestionListener;
import info.pratham.asersample.utility.ListConstant;

public class ChekingDialog extends Dialog {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    List<QuestionStructure> qustionList;
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
        RecyclerViewCheckAnswerAdapter recyclerViewCheckAnswerAdapter = new RecyclerViewCheckAnswerAdapter(context, qustionList, level);
        recyclerView.setAdapter(recyclerViewCheckAnswerAdapter);
    }

    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }

    @OnClick(R.id.submitbtn)
    public void submit() {
        checkQuestionListener.onSubmitListener(subject, false, calledFrom);
        dismiss();
    }
}
