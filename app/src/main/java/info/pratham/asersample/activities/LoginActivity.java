package info.pratham.asersample.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.fragments.LoginFragment;
import info.pratham.asersample.fragments.StudentDetails;
import info.pratham.asersample.networkManager.NetworkManager;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.PermissionResult;
import info.pratham.asersample.utility.PermissionUtils;

public class LoginActivity extends BaseActivity implements PermissionResult {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dynamicPermissionCheck();
    }

    private void dynamicPermissionCheck() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            //Dynamic permissions needed
            String[] permissionArray = new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE, PermissionUtils.Manifest_CAMERA};
            if (!isPermissionsGranted(LoginActivity.this, permissionArray)) {
                // permissions not granted
                askCompactPermissions(permissionArray, this);
            } else proceedFurther();
        } else proceedFurther();

    }

    private void proceedFurther() {
        // Application is ready to go with permission acceptance

        //Hide notification bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Fragment display
//        AserSampleUtility.showFragment(LoginActivity.this,new ServeyOrEvaluation());
//        AserSampleUtility.showFragment(LoginActivity.this,new LoginFragment());
        AserSampleUtility.showFragment(LoginActivity.this, new LoginFragment(), LoginFragment.class.getSimpleName());
        NetworkManager.getInstance(LoginActivity.this).getQuestionData();
       /* FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,new LoginFragment());
        fragmentTransaction.commit();*/
    }

    @Override
    public void permissionGranted() {
        proceedFurther();
    }

    @Override
    public void permissionDenied() {

    }

    @Override
    public void permissionForeverDenied() {

    }
}
