package info.pratham.asersample.activities;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import java.io.File;

import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.fragments.LoginFragment;
import info.pratham.asersample.fragments.SelectLanguageFragment;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.PermissionResult;
import info.pratham.asersample.utility.PermissionUtils;

public class LoginActivity extends BaseActivity implements PermissionResult {
    String fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        fragment = getIntent().getStringExtra("fragment");
        dynamicPermissionCheck();
    }

    private void dynamicPermissionCheck() {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            //Dynamic permissions needed
            String[] permissionArray = new String[]{PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE, PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_RECORD_AUDIO};
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

        if (!createFolderStructureForStoringDataLocally())
            Toast.makeText(this, "Cannot create folder locally", Toast.LENGTH_SHORT).show();
        if (fragment == null) {
            AserSampleUtility.showFragment(LoginActivity.this, new LoginFragment(), LoginFragment.class.getSimpleName());
        } else if (fragment.equals(SelectLanguageFragment.class.getSimpleName())) {
            AserSampleUtility.showFragment(LoginActivity.this, new SelectLanguageFragment(), SelectLanguageFragment.class.getSimpleName());
        }
    }

    private boolean createFolderStructureForStoringDataLocally() {
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/StudentRecordings");
        if (!file.exists())
            return file.mkdirs();
        return true;
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

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        Fragment f = getFragmentManager().findFragmentById(R.id.framelayout);
        if (f instanceof LoginFragment)
            finish();
        else if (count == 1) {
            if (f instanceof SelectLanguageFragment)
                AserSampleUtility.showFragment(LoginActivity.this, new LoginFragment(), LoginFragment.class.getSimpleName());
        } else
            getFragmentManager().popBackStack();
    }
}
