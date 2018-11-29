package info.pratham.asersample.fragments.language;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.CRL;
import info.pratham.asersample.fragments.PullCRl;
import info.pratham.asersample.fragments.SelectLanguageFragment;
import info.pratham.asersample.utility.AserSampleUtility;

/**
 * Created by PEF on 24/11/2018.
 */

public class StoryFragment extends BaseFragment {

    @BindView(R.id.username)
    android.support.design.widget.TextInputEditText userName;
    @BindView(R.id.password)
    android.support.design.widget.TextInputEditText password;
    @BindView(R.id.loginSubmitButton)
    Button loginSubmitButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
