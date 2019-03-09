package info.pratham.asersample;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.activities.Submit;
import info.pratham.asersample.database.AS_Database;
import info.pratham.asersample.fragments.subject.English;
import info.pratham.asersample.fragments.subject.MathFragment;
import info.pratham.asersample.fragments.subject.NativeLang;
import info.pratham.asersample.interfaces.GetTimeListener;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;
import info.pratham.asersample.utility.ListConstant;

public class Assessment extends AppCompatActivity implements GetTimeListener {

    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.chronometer)
    Chronometer chronometer;

    int recordingIndex = 0;
    long timeWhenStopped = 0;

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
        //reset secording index and chronometer
        recordingIndex = 0;
        chronometer.setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
        //digital font to Timer
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/digital.ttf");
        chronometer.setTypeface(custom_font);

    }

    @Override
    protected void onStart() {
        super.onStart();
        String currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";
        File file = new File(currentFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        AudioUtil.startRecording(currentFilePath + recordingIndex + AserSample_Constant.getAserSample_Constant().getStudent().getId() + "1.mp3");
      /*  chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();*/
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();
        Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
        recordingIndex++;
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
                ListConstant.clearFields();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.submit)
    public void submit() {
        Intent intent = new Intent(this, Submit.class);
        startActivity(intent);
    }

    @Override
    public String getTime() {
        return chronometer.getText().toString();
    }
}
