package info.pratham.asersample.utility;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import info.pratham.asersample.R;

/**
 * Created by PEF on 24/11/2018.
 */

public class AserSampleUtility {

    public static void showFragment(Activity activity,Fragment fragment,String TAG){
        FragmentManager fragmentManager=activity.getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commit();
    }
}
