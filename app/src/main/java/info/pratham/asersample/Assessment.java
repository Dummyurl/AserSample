package info.pratham.asersample;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.database.AS_Database;
import info.pratham.asersample.fragments.subject.English;
import info.pratham.asersample.fragments.subject.MathFragment;
import info.pratham.asersample.fragments.subject.NativeLang;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;

public class Assessment extends AppCompatActivity {

    @BindView(R.id.header)
    TextView header;

    @BindView(R.id.chronometer)
    Chronometer chronometer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        ButterKnife.bind(this);
        // header = findViewById(R.id.header);
        int selecteditem = getIntent().getIntExtra("Sample", -1);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        loadFragment(new NativeLang());
        header.setText("Native");
        String currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";
        File file = new File(currentFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        //digital font to Timer
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/digital.ttf");
        chronometer.setTypeface(custom_font);


        AudioUtil.startRecording(currentFilePath + AserSample_Constant.getAserSample_Constant().getStudent().getId() + "1.mp3");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_native:
                    header.setText("Native");
                    fragment = new NativeLang();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_maths:
                    header.setText("Mathematics");
                    fragment = new MathFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_english:
                    header.setText("English");
                    fragment = new English();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AudioUtil.stopRecording();
        chronometer.stop();
    }

    @Override
    public void onBackPressed() {
        AlertDialog builder = new AlertDialog.Builder(this).create();
        builder.setMessage("Do you want to exit");
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
