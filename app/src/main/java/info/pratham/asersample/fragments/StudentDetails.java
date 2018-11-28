package info.pratham.asersample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.LoginActivity;
import info.pratham.asersample.networkManager.NetworkManager;

public class StudentDetails extends BaseFragment {

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
    }

    @OnClick(R.id.nextButton)
    public void startAser() {
        // UpdateData
        updateStudentDataInDB();
        // start ASER test here
        getQuestion();
    }

    private void updateStudentDataInDB() {
        // Insert in DB
    }

    private void getQuestion() {
        NetworkManager.getInstance(getActivity()).getQuestionData();
    }

}
