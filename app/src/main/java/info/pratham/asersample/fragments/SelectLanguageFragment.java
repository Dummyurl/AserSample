package info.pratham.asersample.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
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
import info.pratham.asersample.utility.AserSample_Constant;

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

    @OnClick({R.id.hindiButton, R.id.hindi})
    public void onHindiLLClick() {
        AserSample_Constant.selectedLanguage = getString(R.string.Hindi);
        AserSample_Constant.subject = getString(R.string.Hindi);
        AserSampleUtility.showFragment(getActivity(), new ServeyOrEvaluation(), ServeyOrEvaluation.class.getSimpleName());
    }

    @OnClick({R.id.marathiButton, R.id.marathi})
    public void marathiClick() {
        AserSample_Constant.selectedLanguage = getString(R.string.Marathi);
        AserSample_Constant.subject = getString(R.string.Marathi);
        AserSampleUtility.showFragment(getActivity(), new ServeyOrEvaluation(), ServeyOrEvaluation.class.getSimpleName());
    }

}
