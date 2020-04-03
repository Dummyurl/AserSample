package info.pratham.asersample.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.dialog.ChekingDialog;
import info.pratham.asersample.fragments.subject.English;
import info.pratham.asersample.fragments.subject.MathFragment;
import info.pratham.asersample.fragments.subject.NativeLang;
import info.pratham.asersample.interfaces.CheckQuestionListener;
import info.pratham.asersample.interfaces.GetTimeListener;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;
import info.pratham.asersample.utility.ListConstant;

public class Assessment extends AppCompatActivity implements GetTimeListener, CheckQuestionListener {

    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.chronometer)
    Chronometer chronometer;

    int recordingIndex = 0;
    long timeWhenStopped = 0;

    String previousSelectedItem;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        ButterKnife.bind(this);
        // header = findViewById(R.id.header);
        //  int selecteditem = getIntent().getIntExtra("Sample", -1);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        fragment = new NativeLang();
        loadFragment(fragment);
        header.setText("Native");
        previousSelectedItem = "Native";
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
       /* String currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";
        File file = new File(currentFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }*/

        // AudioUtil.startRecording(this, currentFilePath + recordingIndex + "_" + AserSample_Constant.getAserSample_Constant().getStudent().getId() + ".mp3");
        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chronometer.start();
        // new  CameraStateManager( currentFilePath + recordingIndex + "_" + AserSample_Constant.getAserSample_Constant().getStudent().getId() + ".mp3",this);
      /*  chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();*/
        //Toast.makeText(this, "Recording Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    {
        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_native:
                        showDialog();
                        header.setText("Native");
                        fragment = new NativeLang();
                        AserSample_Constant.subject = AserSample_Constant.selectedLanguage;
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_maths:
                        showDialog();
                        header.setText("Mathematics");
                        fragment = new MathFragment();
                        AserSample_Constant.subject = AserSample_Constant.selectedLanguage;
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_english:
                        showDialog();
                        header.setText("English");
                        fragment = new English();
                        AserSample_Constant.subject = getString(R.string.English);
                        loadFragment(fragment);
                        return true;
                }
                return false;
            }
        };
    }

    private void showDialog() {
        if (fragment instanceof NativeLang) {
            ((NativeLang) fragment).showCheckAnswerDialog("outSide");
        } else if (fragment instanceof MathFragment) {
            ((MathFragment) fragment).showCheckAnswerDialog("outSide");
        } else if (fragment instanceof English) {
            ((English) fragment).showCheckAnswerDialog("outSide");
        }
    }


    public void showProfincyDialog(final String selectedItem, final String calledFrom) {

        String[] singleChoiceItems = null;
        final String[] proficiency = {null};
        int itemSelected = -1;
        switch (selectedItem) {
            case "Native":
                singleChoiceItems = new String[]{getString(R.string.Letter), getString(R.string.WordNative), getString(R.string.Paragraph), getString(R.string.Story), getString(R.string.Beginner), getString(R.string.TestWasNotComplete)};
                proficiency[0] = AserSample_Constant.getAserSample_Constant().getStudent().getNativeProficiency();
                break;
            case "Mathematics":
                singleChoiceItems = new String[]{getString(R.string.oneToNine), getString(R.string.tenToNinetyNine), getString(R.string.Subtraction), getString(R.string.Division), getString(R.string.Beginner), getString(R.string.TestWasNotComplete)};
                proficiency[0] = AserSample_Constant.getAserSample_Constant().getStudent().getMathematicsProficiency();
                break;
            case "English":
                singleChoiceItems = new String[]{getString(R.string.Capitalletter), getString(R.string.Smallletter), getString(R.string.word), getString(R.string.Sentence), getString(R.string.Beginner), getString(R.string.TestWasNotComplete)};
                proficiency[0] = AserSample_Constant.getAserSample_Constant().getStudent().getEnglishProficiency();
                break;
        }
        if (proficiency[0] != null) {
            for (int i = 0; i < singleChoiceItems.length; i++) {
                if (proficiency[0].equals(singleChoiceItems[i])) {
                    itemSelected = i;
                    break;
                }
            }
        }

        final String[] finalSingleChoiceItems = singleChoiceItems;
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select " + selectedItem + " proficiency")
                .setCancelable(false)
                .setSingleChoiceItems(singleChoiceItems, itemSelected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        proficiency[0] = finalSingleChoiceItems[i];
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;

                if (proficiency[0] != null) {
                    switch (selectedItem) {
                        case "Native":
                            AserSample_Constant.getAserSample_Constant().getStudent().setNativeProficiency(proficiency[0]);
                            // previousSelectedItem = selectedItem;
                            break;
                        case "Mathematics":
                            AserSample_Constant.getAserSample_Constant().getStudent().setMathematicsProficiency(proficiency[0]);
                            //  previousSelectedItem = selectedItem;
                            break;
                        case "English":
                            AserSample_Constant.getAserSample_Constant().getStudent().setEnglishProficiency(proficiency[0]);
                            break;
                    }
                    wantToCloseDialog = true;
                    proficiency[0] = null;
                    previousSelectedItem = selectedItem;
                }
                //Do stuff, possibly set wantToCloseDialog to true then...
                if (wantToCloseDialog) {
                    dialog.dismiss();
                    if (calledFrom.equals("finish")) {
                        new AlertDialog.Builder(Assessment.this)
                                .setMessage("do you want to Submit test?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AudioUtil.stopRecording(Assessment.this);
                                        chronometer.stop();
                                        dialog.dismiss();
                                        Intent intent = new Intent(Assessment.this, Submit.class);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


                    }
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AudioUtil.stopRecording(Assessment.this);
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
                AudioUtil.stopRecording(Assessment.this);
                chronometer.stop();
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
        AudioUtil.stopRecording(Assessment.this);
        chronometer.stop();
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (fragment instanceof NativeLang) {
            ((NativeLang) fragment).showCheckAnswerDialog("finish");
        } else if (fragment instanceof MathFragment) {
            ((MathFragment) fragment).showCheckAnswerDialog("finish");
        } else if (fragment instanceof English) {
            ((English) fragment).showCheckAnswerDialog("finish");
        }
    }

    @Override
    public String getTime() {
        return chronometer.getText().toString();
    }


    // its getting called from Sunject fragment
    public void showChekingDialog(String subject, String level, String calledFrom) {
        List<QuestionStructure> qustionList = new ArrayList<>();
        switch (level) {
            //NATIVE LANGUAGE
            case "Words":
                qustionList = ListConstant.Words;
                break;
            case "Letters":
                qustionList = ListConstant.Letters;
                break;
            case "Para":
                qustionList = ListConstant.Para;
                break;
            case "Story":
                qustionList = ListConstant.Story;
                break;
            //ENGLISH Language
            case "Capital":
                qustionList = ListConstant.Capital;
                break;
            case "Small":
                qustionList = ListConstant.Small;
                break;
            case "word":
                qustionList = ListConstant.engWord;
                break;
            case "Sentence":
                qustionList = ListConstant.Sentence;
                break;
            //MATHEMATICS
            case "Single":
                qustionList = ListConstant.Single;
                break;
            case "Double":
                qustionList = ListConstant.Double;
                break;
            case "Subtraction":
                qustionList = ListConstant.Subtraction;
                break;
            case "Division":
                qustionList = ListConstant.Division;
                break;
        }
        boolean flag = false;
        for (QuestionStructure questionStructure : qustionList) {
            if (questionStructure.isSelected()) {
                flag = true;
            }
        }
        if (flag) {
            ChekingDialog chekingDialog = new ChekingDialog(Assessment.this, qustionList, subject, level, calledFrom);
            chekingDialog.show();
        } else {
            //call a show proficincy
            if (calledFrom.equals("inFragment")) {

            } else {
                showProfincyDialog(subject, calledFrom);
            }
        }
    }

    /* after checking correct wrong answer by crl then on submit callback*/
    @Override
    public void onSubmitListener(String subject, boolean changeTab, String calledFrom) {
        if (calledFrom.equals("inFragment")) {

        } else {
            showProfincyDialog(subject, calledFrom);
        }
    }
}
