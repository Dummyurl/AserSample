package info.pratham.asersample;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Random;

/**
 * Created by Pravin on 2 Feb 2018.
 */

public class ASERApplication extends Application {

    private static ASERApplication aserApplication;

    public ASERApplication() {
        aserApplication = this;
    }

    public static String getVersion() {
        Context context = ASERApplication.aserApplication;
        String packageName = context.getPackageName();
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static ASERApplication getInstance() {
        return aserApplication;
    }

    public static int getRandomNumber(int min, int max) {
        return min + (new Random().nextInt(max));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        AndroidNetworking.initialize(getApplicationContext());

    }

    public String getRootPath() {
        File root = android.os.Environment.getExternalStorageDirectory();
        return root.getAbsolutePath() + "/StudentRecordings/";
    }

    public String getRootPathForDeletion() {
        File root = android.os.Environment.getExternalStorageDirectory();
        return root.getAbsolutePath();
    }
}
