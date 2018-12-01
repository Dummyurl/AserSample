package info.pratham.asersample.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.EnglishActivity;
import info.pratham.asersample.activities.LanguageActivity;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSampleUtility;

public class StudentDetails extends BaseFragment {
    @BindView(R.id.childName)
    EditText childName;
    @BindView(R.id.fatherName)
    EditText fatherName;
    @BindView(R.id.villageName)
    EditText villageName;
    @BindView(R.id.radiogroup)
    RadioGroup radioGroup;

    @BindView(R.id.classChild)
    Spinner classChild;
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
        mDatabase = FirebaseDatabase.getInstance().getReference("students");
        loadSpinner();
    }

    private void loadSpinner() {
        String[] classArray = getResources().getStringArray(R.array.class_of_child);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, classArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classChild.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.nextButton)
    public void next() {
        String childFirstName = childName.getText().toString().trim();
        String childFatherName = fatherName.getText().toString().trim();
        String childVillageName = villageName.getText().toString().trim();
        int selectedclass = classChild.getSelectedItemPosition();
        int agegroup = radioGroup.getCheckedRadioButtonId();
      //  if (!childFirstName.isEmpty() && !childFatherName.isEmpty() && !childVillageName.isEmpty() && selectedclass > 0 && agegroup != -1) {

          //  Student student = new Student(childFirstName, childFatherName, childVillageName, classChild.getSelectedItem().toString(), ((RadioButton) radioGroup.findViewById(agegroup)).getText().toString());
            Intent intent = new Intent(getActivity(), EnglishActivity.class);
           // intent.putExtra("student", student);
            getActivity().startActivity(intent);


            //call native language Activity

            // AserSampleUtility.showFragment();


           /* mDatabase.child(student.name).setValue(student)
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
                    });*/
      /*  } else {
            //  mDatabase.setValue(childFirstName);
            AserSampleUtility.showToast(getActivity(), "All Fields Are mandatory");
        }*/
    }


}
