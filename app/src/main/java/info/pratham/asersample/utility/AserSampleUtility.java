package info.pratham.asersample.utility;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.widget.Toast;

import info.pratham.asersample.R;

/**
 * Created by PEF on 24/11/2018.
 */

public class AserSampleUtility {

    public static void showFragment(Activity activity, Fragment fragment, String TAG) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }

    /*public static void removeFragment(Activity activity, String TAG) {

        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (activity.getFragmentManager().getBackStackEntryCount()> 0) {
            fragmentManager.popBackStackImmediate(activity.getFragmentManager().getBackStackEntryCount() - 1, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            String previousTag = activity.getFragmentManager().getBackStackEntryAt(activity.getFragmentManager().getBackStackEntryCount() - 1).getName();
            Fragment previousInstance = activity.getFragmentManager().findFragmentByTag(previousTag);

            fragmentTransaction.replace(R.id.framelayout, previousInstance, previousTag);
            fragmentTransaction.commit();


        }













       *//* Fragment fragment = activity.getFragmentManager().findFragmentByTag(TAG);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment).commit();
            activity.getFragmentManager().beginTransaction().remove(fragment).commit();
            activity.getFragmentManager().popBackStackImmediate();
        }*//*
    }
*/

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
