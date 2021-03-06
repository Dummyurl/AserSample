package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.expandableRecyclerView.RecyclerAdapter;
import info.pratham.asersample.interfaces.PreviewDialogListener;
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

    @BindView(R.id.nextButton)
    ImageView nextButton;

    Context context;
    PreviewDialogListener previewDialogListener;

    public PreviewDialog(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_Light_NoActionBar);
        this.context = context;
        previewDialogListener = (PreviewDialogListener) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_dialog);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setInformation();
        recyclerView.setAdapter(new RecyclerAdapter(recyclerView));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    /*@OnClick(R.id.submit)
    public void cancel() {
        dismiss();
        previewDialogListener.onSubmit();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        previewDialogListener.onSubmit();
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

    @OnClick(R.id.nextButton)
    public void setNextButton() {
        previewDialogListener.onSubmit();
        dismiss();
    }
}
