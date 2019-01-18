package info.pratham.asersample.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.LanguageActivity;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

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
    private String blockCharacterSet = ".~#^|$%&*!";

    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

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
        childName.setFilters(new InputFilter[]{filter});
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
        if (!childFirstName.isEmpty() && !childFatherName.isEmpty() && !childVillageName.isEmpty() && selectedclass > 0 && agegroup != -1) {
            if (!isAlpha(childFirstName))
                AserSampleUtility.showToast(getActivity(), "Student name should not contain special characters!!");
            else {
                String id = childFirstName + "__" + AserSampleUtility.getUUID();
                Student student = new Student(id, childFirstName, childFatherName, childVillageName, classChild.getSelectedItem().toString(), ((RadioButton) radioGroup.findViewById(agegroup)).getText().toString(), Calendar.getInstance().getTime().toString(), AserSample_Constant.getDeviceID());
                AserSample_Constant.getAserSample_Constant().setStudent(student);
                Intent intent = new Intent(getActivity(), LanguageActivity.class);
                getActivity().startActivity(intent);
            }
        } else {
            AserSampleUtility.showToast(getActivity(), "All fields are mandatory");
        }
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
}
