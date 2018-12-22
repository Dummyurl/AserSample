package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.expandableRecyclerView.RecyclerAdapter;
import info.pratham.asersample.utility.AserSample_Constant;

/**
 * Created by PEF on 22/12/2018.
 */

public class PreviewDialog extends Dialog {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.age_group)
    TextView age_group;
    @BindView(R.id.stud_class)
    TextView stud_class;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.deviceId)
    TextView deviceId;
    @BindView(R.id.nativeLanguageProf)
    TextView nativeLanguageProf;
    @BindView(R.id.mathLanguageProf)
    TextView mathLanguageProf;
    @BindView(R.id.engLanguageProf)
    TextView engLanguageProf;

    Context context;

    public PreviewDialog(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_Light_NoActionBar);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_dialog);
        ButterKnife.bind(this);
        setInformation();
        recyclerView.setAdapter(new RecyclerAdapter(recyclerView));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void setInformation() {
        Student student = AserSample_Constant.getAserSample_Constant().getStudent();
        name.setText(student.getName() + " " + student.getFather());
        age_group.setText(student.getAgeGroup());
        stud_class.setText(student.getStudClass());
        date.setText(student.getDate());
        deviceId.setText(student.getDeviceID());
        nativeLanguageProf.setText(student.getNativeProficiency());
        mathLanguageProf.setText(student.getMathematicsProficiency());
        engLanguageProf.setText(student.getEnglishProficiency());
    }
}
