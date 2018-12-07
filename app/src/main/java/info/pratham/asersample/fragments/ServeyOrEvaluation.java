package info.pratham.asersample.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.networkManager.UploadRec;
import info.pratham.asersample.utility.AserSampleUtility;


public class ServeyOrEvaluation extends BaseFragment {

    @BindView(R.id.selectionActivitySettings)
    ImageView selectionActivitySettings;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serveyorevaluation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.startEvaluationButton)
    public void startEvalution() {
        AserSampleUtility.showFragment(getActivity(), new StudentDetails(), StudentDetails.class.getSimpleName());
    }

    @OnClick(R.id.selectionActivitySettings)
    public void pushrecording() {
        Intent intent = new Intent(getActivity(), UploadRec.class);
        getActivity().startActivity(intent);
    }
}
