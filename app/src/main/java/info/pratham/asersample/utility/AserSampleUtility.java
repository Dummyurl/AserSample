package info.pratham.asersample.utility;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.widget.Toast;

import info.pratham.asersample.R;
import info.pratham.asersample.fragments.math.CalculationFragment;

/**
 * Created by PEF on 24/11/2018.
 */

public class AserSampleUtility {

    public static void showFragment(Activity activity, Fragment fragment, String TAG) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        if (!TAG.equals(CalculationFragment.class.getSimpleName()))
            fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    public static void removeFragment(Activity activity, String TAG) {
        Fragment fragment = activity.getFragmentManager().findFragmentByTag(TAG);
        if (fragment != null)
            activity.getFragmentManager().beginTransaction().remove(fragment).commit();
    }


    public static void showProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
