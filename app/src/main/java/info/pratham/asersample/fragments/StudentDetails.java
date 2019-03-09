package info.pratham.asersample.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.common.collect.Iterators;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.ASERApplication;
import info.pratham.asersample.Assessment;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.LanguageActivity;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

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
        final String childFirstName = childName.getText().toString().trim();
        final String childFatherName = fatherName.getText().toString().trim();
        final String childVillageName = villageName.getText().toString().trim();
        int selectedclass = classChild.getSelectedItemPosition();
        final int agegroup = radioGroup.getCheckedRadioButtonId();
        if (!childFirstName.isEmpty() && !childFatherName.isEmpty() && !childVillageName.isEmpty() && selectedclass > 0 && agegroup != -1) {
            if (!isAlpha(childFirstName))
                AserSampleUtility.showToast(getActivity(), "Student name should not contain special characters!!");
            else {
                String question = databaseInstance.getQuestiondao().getLanguageQuestions(AserSample_Constant.selectedLanguage);
                String SampleArray[] = null;
                try {
                    if (question != null) {
                        int i = 0;
                        final JSONObject questionJson = new JSONObject(question);
                        Iterator<String> iter = questionJson.keys();
                        int sampleCnt = Iterators.size(iter);
                        SampleArray = new String[sampleCnt];
                        while (sampleCnt > i) {
                            SampleArray[i++] = "Sample" + i;
                        }
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Select Sample of " + AserSample_Constant.selectedLanguage)
                                .setSingleChoiceItems(SampleArray, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int selectedItem) {
                                        try {
                                            AserSample_Constant.sample = (JSONObject) questionJson.get("Sample" + (selectedItem + 1));
                                            String id = childFirstName + "__" + AserSampleUtility.getUUID();
                                            Student student = new Student(id, childFirstName, childFatherName, childVillageName, classChild.getSelectedItem().toString(), ((RadioButton) radioGroup.findViewById(agegroup)).getText().toString(), Calendar.getInstance().getTime().toString(), AserSample_Constant.getDeviceID());
                                            AserSample_Constant.getAserSample_Constant().setStudent(student);
                                            ListConstant.clearFields();
                                            Intent intent = new Intent(getActivity(), Assessment.class);
                                            intent.putExtra("Sample", selectedItem);
                                            getActivity().startActivity(intent);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        dialogInterface.dismiss();

                                    }
                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    } else {
                        AserSampleUtility.showToast(getActivity(), "No data available. Contact administrator!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
