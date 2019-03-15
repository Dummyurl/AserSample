package info.pratham.asersample.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.R;
import info.pratham.asersample.adapters.RecyclerPreviewAdapter;
import info.pratham.asersample.adapters.RecyclerViewCheckAnswerAdapter;
import info.pratham.asersample.database.modalClasses.SingleQustioNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

public class Submit extends AppCompatActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.stud_info)
    TextView stud_info;
    @BindView(R.id.nativeLanguageProf)
    TextView nativeLanguageProf;
    @BindView(R.id.mathLanguageProf)
    TextView mathLanguageProf;
    @BindView(R.id.engLanguageProf)
    TextView engLanguageProf;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;


    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.bind(this);
        AserSampleUtility.writeStudentInJson(this);
        ListConstant.clearFields();
        student = AserSample_Constant.getAserSample_Constant().getStudent();
        setStudentInfo();
        initRecycler();
    }

    private void initRecycler() {
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(gridLayoutManager);
        RecyclerPreviewAdapter recyclerViewCheckAnswerAdapter = new RecyclerPreviewAdapter(this, student.getSequenceList());
        recyclerView.setAdapter(recyclerViewCheckAnswerAdapter);
    }

    private void setStudentInfo() {
        name.setText(student.getName() + " " + student.getFather());
        stud_info.setText("age:" + student.getAgeGroup() + ", class " + student.getStudClass());
        nativeLanguageProf.setText(student.getNativeProficiency());
        mathLanguageProf.setText(student.getMathematicsProficiency());
        engLanguageProf.setText(student.getEnglishProficiency());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("fragment", "SelectLanguageFragment");
        startActivity(intent);
    }
}
