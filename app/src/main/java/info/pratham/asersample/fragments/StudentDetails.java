package info.pratham.asersample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSampleUtility;

public class StudentDetails extends BaseFragment {
    @BindView(R.id.childName)
    EditText childName;
    @BindView(R.id.fatherName)
    EditText fatherName;
    @BindView(R.id.villageName)
    EditText villageName;

    private DatabaseReference mDatabase;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studentdetails, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference("students");
    }

    @OnClick(R.id.btn_next)
    public void next() {
        String childFirstName = childName.getText().toString().trim();
        String childFatherName = fatherName.getText().toString().trim();
        String childVillageName = villageName.getText().toString().trim();
        if (!childFirstName.isEmpty() && !childFatherName.isEmpty() && !childVillageName.isEmpty()) {

            Student student = new Student(childFirstName, childFatherName, childVillageName);

            mDatabase.child(student.name).setValue(student)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Write was successful!
                            AserSampleUtility.showToast(getActivity(), "Done..");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            AserSampleUtility.showToast(getActivity(), "FAIL..");
                        }
                    });
        } else {
            //  mDatabase.setValue(childFirstName);
            AserSampleUtility.showToast(getActivity(), "All Fields Are mandatory");
        }
    }
}
