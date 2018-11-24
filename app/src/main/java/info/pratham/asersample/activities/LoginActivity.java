package info.pratham.asersample.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.fragments.LoginFragment;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.framelayout)
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loadFragment();
    }

    private void loadFragment() {
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        LoginFragment loginFragment=new LoginFragment();
        fragmentTransaction.add(frameLayout,loginFragment,"");
    }
}
