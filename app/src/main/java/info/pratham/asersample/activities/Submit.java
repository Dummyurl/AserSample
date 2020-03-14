package info.pratham.asersample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.adapters.RecyclerPreviewAdapter;
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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        ButterKnife.bind(this);
        AserSampleUtility.writeStudentInJson(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("azure_model");

        mDatabase.child(AserSample_Constant.getCrlID()).child(AserSample_Constant.getAserSample_Constant().getStudent().getId()).setValue(AserSample_Constant.getAserSample_Constant().getStudent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        AserSampleUtility.showToast(Submit.this, "Successfully uploaded!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        AserSampleUtility.showToast(Submit.this, "Uploading failed!");
                    }
                });


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

    @OnClick(R.id.ok)
    public void ok() {
        finishAffinity();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("fragment", "SelectLanguageFragment");
        startActivity(intent);
    }
}
