package info.pratham.asersample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.utility.AserSampleUtility;

/**
 * Created by PEF on 24/11/2018.
 */

public class SelectLanguageFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_language, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.hindiLL)
    public void onHindiLLClick() {
        AserSampleUtility.showFragment(getActivity(), new ServeyOrEvaluation(), ServeyOrEvaluation.class.getSimpleName());
    }

    @OnClick(R.id.englishLL)
    public void englishLLClick() {
        AserSampleUtility.showFragment(getActivity(), new ServeyOrEvaluation(), ServeyOrEvaluation.class.getSimpleName());
    }

}
