package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.SingleQuestionNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSampleUtility;

public class UpdateActivity extends AppCompatActivity {

    int iterator;
    String crl_id;
    String studentName;
    private List<SingleQuestionNew> sequenceList;
    private SingleQuestionNew singleQuestionNew;
    private Student currentStudent;
    private ProgressDialog progressDialog;
    private String DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DOWNLOADS).getPath();
    String localDirectoryPath;
    MediaPlayer mp;
    private boolean playing, infoHide;

    @BindView(R.id.tv_crl_txt)
    TextView crl_name;
    @BindView(R.id.tv_student_txt)
    TextView student_info;
    @BindView(R.id.tv_date_txt)
    TextView test_date;
    @BindView(R.id.tv_question_txt)
    TextView question;
    @BindView(R.id.tv_answer_txt)
    TextView answer;
    @BindView(R.id.ib_listen)
    ImageView listenAnswer;
    @BindView(R.id.ib_correct)
    ImageView correctGiven;
    @BindView(R.id.ib_wrong)
    ImageView wrongGiven;
    @BindView(R.id.ib_next)
    Button next;
    @BindView(R.id.ib_prev)
    Button prev;
    @BindView(R.id.native_spinner)
    Spinner native_prof;
    @BindView(R.id.math_spinner)
    Spinner math_prof;
    @BindView(R.id.eng_spinner)
    Spinner eng_prof;
    @BindView(R.id.tv_txt_mistakes)
    TextView tv_mistakes;
    @BindView(R.id.rl_info)
    RelativeLayout rl_info;
    @BindView(R.id.noise_descRL)
    RelativeLayout noise_descRL;
    @BindView(R.id.noise_rg)
    RadioGroup noise_rg;
    @BindView(R.id.remark_ll)
    LinearLayout remark_ll;
    @BindView(R.id.GTruth_et)
    EditText GTruth_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_update);
        ButterKnife.bind(this);
        currentStudent = (Student) getIntent().getSerializableExtra("STUDENT_SEQUENCE");
        crl_id = getIntent().getStringExtra("crl_id");
        studentName = currentStudent.getName();
        if (!studentName.isEmpty())
            studentName = studentName + "__";
//        crl_id = "1821_up_sanjeet";
        sequenceList = currentStudent.getSequenceList();
        String[] stdId = currentStudent.getId().split("__");
        String fileName;
        if (stdId.length == 1)
            fileName = stdId[0];
        else
            fileName = stdId[1];
        String firebaseDirectoryPath = "StudentRecordings/" + crl_id + "/";
        localDirectoryPath = DOWNLOAD_DIR + "/ASER_Audios/" + crl_id;
        downloadFile(firebaseDirectoryPath, localDirectoryPath, fileName);

        GTruth_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                singleQuestionNew.setGroundTruthDescription(s.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        stopAudio();
        super.onBackPressed();
    }

    private void showQuestionsSequentially() {
        if (iterator == 0)
            prev.setVisibility(View.INVISIBLE);
        else if (iterator == (sequenceList.size() - 1))
            next.setVisibility(View.INVISIBLE);

        if (iterator > 0 && iterator < sequenceList.size() - 1) {
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }

        if (iterator >= 0) {

            singleQuestionNew = sequenceList.get(iterator);

            // setting profieciencies
            math_prof.setSelection(getIndex(math_prof, currentStudent.getMathematicsProficiency()));
            native_prof.setSelection(getIndex(native_prof, currentStudent.getNativeProficiency()));
            eng_prof.setSelection(getIndex(eng_prof, currentStudent.getEnglishProficiency()));

            // set information of test
            crl_name.setText(Html.fromHtml("<b>" + crl_id + "</b>"));
            student_info.setText(Html.fromHtml("<b>" + currentStudent.getName() +
                    "</b> from <b>" + currentStudent.getVillage() +
                    "</b> in class <b>" + currentStudent.getStudClass() + "</b>"));
            test_date.setText(Html.fromHtml("<b>" + currentStudent.getDate() + "</b>"));

            // setting question and answer
            question.setText(singleQuestionNew.getQue_text());

            //setting number of mistakes
            tv_mistakes.setText(singleQuestionNew.getNoOfMistakes());

            GTruth_et.setText(singleQuestionNew.getGroundTruthDescription());

            //setting answer in text or audio format
            if (singleQuestionNew.getAnswer() != null) {
                listenAnswer.setVisibility(View.GONE);
                answer.setVisibility(View.VISIBLE);
                answer.setText(singleQuestionNew.getAnswer());
                noise_descRL.setVisibility(View.GONE);
            } else if (singleQuestionNew.getRecordingName() != null) {
                answer.setVisibility(View.GONE);
                listenAnswer.setVisibility(View.VISIBLE);

                //get noise description
                noise_descRL.setVisibility(View.VISIBLE);
//                GTruth_et.setText("");
                remarksMarked = "";
                // set radios and checkboxes
                resetRadiobutton();
                resetCheckboxes();
            }

            if (singleQuestionNew.isCorrect()) {
                wrongGiven.setVisibility(View.GONE);
                correctGiven.setVisibility(View.VISIBLE);
            } else {
                correctGiven.setVisibility(View.GONE);
                wrongGiven.setVisibility(View.VISIBLE);
            }
        }
    }

    private void resetRadiobutton() {
        String noiseDescription = singleQuestionNew.getNoiseDescription();
        if (noiseDescription == null)
            noiseDescription = "";
        if (noiseDescription.isEmpty()) {
            noise_rg.clearCheck();
            remark_ll.setVisibility(View.GONE);
        } else {
            int count = noise_rg.getChildCount();
            for (int Ccnt = 0; Ccnt < count; Ccnt++) {
                if (noise_rg.getChildAt(Ccnt) instanceof RadioButton) {
                    RadioButton radioButton = ((RadioButton) noise_rg.getChildAt(Ccnt));
                    if (radioButton.getText().toString().equals(noiseDescription)) {
                        radioButton.setChecked(true);
                        if (radioButton.getText().equals("Noisy") || radioButton.getText().equals("Very noisy"))
                            remark_ll.setVisibility(View.VISIBLE);
                        else
                            remark_ll.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        }

    }

    private void resetCheckboxes() {
        String remarks = singleQuestionNew.getRemarks();
        if (remarks == null)
            remarks = "";
        remarksMarked = remarks;
        if (!remarks.isEmpty()) {
            String[] remarksSplitted = remarks.split("\\|");
            if (remarksSplitted.length > 0) {
                int count = remark_ll.getChildCount();
                for (int Ccnt = 1; Ccnt < count; Ccnt++) {
                    if (remark_ll.getChildAt(Ccnt) instanceof CheckBox) {
                        CheckBox checkBox = ((CheckBox) remark_ll.getChildAt(Ccnt));
                        if (Arrays.asList(remarksSplitted).contains(checkBox.getText().toString())) {
                            checkBox.setChecked(true);
                        } else checkBox.setChecked(false);
                    }
                }
            }
        } else {
            singleQuestionNew.setRemarks("");
            int count = remark_ll.getChildCount();
            for (int Ccnt = 1; Ccnt < count; Ccnt++) {
                if (remark_ll.getChildAt(Ccnt) instanceof CheckBox)
                    ((CheckBox) remark_ll.getChildAt(Ccnt)).setChecked(false);
            }
//            ((CheckBox) findViewById(R.id.cb_Btalk)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_Cnoise)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_Birds)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_Vehicles)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_Rain)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_Wind)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_Cps)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_Music)).setChecked(false);
//            ((CheckBox) findViewById(R.id.cb_pr)).setChecked(false);
        }
    }

    String remarksMarked;

    @OnClick({R.id.cb_Btalk, R.id.cb_Cnoise, R.id.cb_Birds, R.id.cb_Vehicles, R.id.cb_Rain,
            R.id.cb_Wind, R.id.cb_Cps, R.id.cb_Music, R.id.cb_pr})
    public void setRemarkCheckboxData(View view) {
        CheckBox clickedCheckbox = (CheckBox) view;
        String currentCheckboxText = clickedCheckbox.getText().toString();
        if (clickedCheckbox.isChecked()) {
            if (!remarksMarked.contains(currentCheckboxText + "|"))
                remarksMarked += currentCheckboxText + "|";
        } else {
            if (remarksMarked.contains(currentCheckboxText + "|"))
                remarksMarked = remarksMarked.replace(currentCheckboxText + "|", "");
        }
        singleQuestionNew.setRemarks(remarksMarked);
        Toast.makeText(this, "" + remarksMarked, Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.verynoisy_rb, R.id.noisy_rb, R.id.clean_rb})
    void setNoiseRadioData(RadioButton button) {
        if (button.isChecked())
            singleQuestionNew.setNoiseDescription(button.getText().toString());
        resetCheckboxes();
        if (button.getText().toString().equals("Very noisy") || button.getText().toString().equals("Noisy")) {
            if (button.isChecked())
                remark_ll.setVisibility(View.VISIBLE);
            else {
                remark_ll.setVisibility(View.GONE);
            }
        } else {
            remark_ll.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.ib_minimize)
    public void minimizeInfoLayout() {
        if (infoHide) {
            infoHide = false;
            rl_info.setVisibility(View.VISIBLE);
        } else {
            infoHide = true;
            rl_info.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.fab)
    public void submit() {
//        submitUpdateDialog();
        submitGenderDialog();
    }

    @OnClick(R.id.ib_edit_mistakes)
    public void editMistakes() {
        setInputMistake();
    }

    private void setInputMistake() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter updated number of mistakes");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setNumberOfMistakes(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void setNumberOfMistakes(String mistakes) {
        tv_mistakes.setText(mistakes);
        String type = singleQuestionNew.getQue_id().split("_")[2];
        switch (type) {
            case "CL":
            case "SL":
            case "W":
            case "L":
            case "D":
            case "DD":
            case "SD":
            case "WD":
            case "SB":
                setAllSiblingsWithSameMistakes(type, mistakes);
                break;

            case "S":
            case "P":
            case "ST":
                singleQuestionNew.setNoOfMistakes(mistakes);
                break;

        }
    }

    private void setAllSiblingsWithSameMistakes(String type, String mistakes) {
        for (int itr = 0; itr < sequenceList.size(); itr++) {
            if (sequenceList.get(itr).getQue_id().split("_")[2].equals(type))
                sequenceList.get(itr).setNoOfMistakes(mistakes);
        }
    }

    @OnClick(R.id.ib_listen)
    public void listenAudio() {
        if (!playing) {
            // set link to listen audio from local
            String[] stdId = currentStudent.getId().split("__");
            String fileName;
            if (stdId.length == 1)
                fileName = stdId[0];
            else
                fileName = stdId[1];

            String filePath = localDirectoryPath + "/" + fileName + "/" + singleQuestionNew.getRecordingName();
            playAudio(filePath);
        } else {
            stopAudio();
        }
    }

    @OnClick(R.id.ib_next)
    public void nextQuestion() {
        stopAudio();
        iterator++;
        if (iterator < sequenceList.size())
            showQuestionsSequentially();
        else
            Toast.makeText(this, "No more questions..", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.ib_prev)
    public void prevQuestion() {
        stopAudio();
        iterator--;
        if (iterator >= 0)
            showQuestionsSequentially();
        else
            Toast.makeText(this, "This is the first question..", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.ib_change)
    public void changeMarking() {
        displayIsCorrectMarkChangeDialog();
    }

    public void submitGenderDialog() {
        final Dialog submitGenderDialog = new Dialog(this);
        submitGenderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        submitGenderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        submitGenderDialog.setContentView(R.layout.gender_dilog);
        Button yesButton = submitGenderDialog.findViewById(R.id.dia_yes_btn);
        final RadioGroup genderGroup = submitGenderDialog.findViewById(R.id.genderRG);
        String genderSelectedBefore = currentStudent.getGender();
        if (genderSelectedBefore != null && !genderSelectedBefore.isEmpty()) {
            int count = genderGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View o = genderGroup.getChildAt(i);
                if (o instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) o;
                    if (radioButton.getText().equals(genderSelectedBefore)) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGenderDialog.dismiss();
                int selectedId = genderGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedGenderRadio = genderGroup.findViewById(selectedId);
                    currentStudent.setGender(selectedGenderRadio.getText().toString());
                } else
                    currentStudent.setGender("");
                submitUpdateDialog();
            }
        });
        Button noButton = submitGenderDialog.findViewById(R.id.dia_no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGenderDialog.dismiss();
            }
        });
        submitGenderDialog.show();
    }

    public void submitUpdateDialog() {
        final Dialog submitUpdateDialog = new Dialog(this);
        submitUpdateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        submitUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        submitUpdateDialog.setContentView(R.layout.yes_no_dilog);
        Button yesButton = submitUpdateDialog.findViewById(R.id.dia_yes_btn);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUpdateDialog.dismiss();
                updateDataOnFirebase();
            }
        });
        Button noButton = submitUpdateDialog.findViewById(R.id.dia_no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUpdateDialog.dismiss();
            }
        });
        submitUpdateDialog.show();
    }

    public void displayIsCorrectMarkChangeDialog() {
        final Dialog isCorrectMarkChangeDialog = new Dialog(this);
        isCorrectMarkChangeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        isCorrectMarkChangeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        isCorrectMarkChangeDialog.setContentView(R.layout.yes_no_dilog);
        TextView message = isCorrectMarkChangeDialog.findViewById(R.id.dia_title);
        message.setText("Do you really want to change Marked by CRL field");
        Button yesButton = isCorrectMarkChangeDialog.findViewById(R.id.dia_yes_btn);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCorrectMarkChangeDialog.dismiss();
                // invert the answer given by CRL
                if (singleQuestionNew.isCorrect()) {
                    correctGiven.setVisibility(View.GONE);
                    wrongGiven.setVisibility(View.VISIBLE);
                    singleQuestionNew.setCorrect(false);
                } else {
                    wrongGiven.setVisibility(View.GONE);
                    correctGiven.setVisibility(View.VISIBLE);
                    singleQuestionNew.setCorrect(true);
                }
            }
        });
        Button noButton = isCorrectMarkChangeDialog.findViewById(R.id.dia_no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCorrectMarkChangeDialog.dismiss();
            }
        });
        isCorrectMarkChangeDialog.show();
    }

    private void updateDataOnFirebase() {
        progressDialog = new ProgressDialog(this);
        AserSampleUtility.showProgressDialog(progressDialog, "Updating data storage..");
        DatabaseReference studentReference = FirebaseDatabase.getInstance().getReference("studentsCopy")
                .child(crl_id)
                .child(currentStudent.getId());

        studentReference.removeValue();// delete old value with name in ID

        currentStudent.setEnglishProficiency(eng_prof.getSelectedItem().toString());
        currentStudent.setMathematicsProficiency(math_prof.getSelectedItem().toString());
        currentStudent.setNativeProficiency(native_prof.getSelectedItem().toString());

        // set Validated or not, using field deviceID
//        currentStudent.setDeviceID(currentStudent.getDeviceID().split(":")[0] + ":" + AserSampleUtility.currentTime());
        // set Validated or not
        currentStudent.setValidatedDate(AserSampleUtility.currentTime());
        //todo comment this for removing confidentiality of student name village and father name
        convertStudentDataForConfidentiality();

        String updatedFilePath = localDirectoryPath + "/" + currentStudent.getId();
        //todo change json with updated data
        if (updateStudentInJson(updatedFilePath)) {
            // todo change file in zip
            String newFilePath = localDirectoryPath + "/" + currentStudent.getId();
            if (zipFileAtPath(newFilePath, newFilePath + ".zip")) {
                uploadDataToStorage(newFilePath + ".zip");
            } else {
                AserSampleUtility.dismissProgressDialog(progressDialog);
                Toast.makeText(UpdateActivity.this, "Problem in zipping updated file", Toast.LENGTH_LONG).show();
            }
        } else {
            AserSampleUtility.dismissProgressDialog(progressDialog);
            Toast.makeText(UpdateActivity.this, "Problem in updating local JSON file", Toast.LENGTH_LONG).show();
        }
    }

    private void convertStudentDataForConfidentiality() {
        currentStudent.setFather("");
        currentStudent.setName("");
        String[] splittedId = currentStudent.getId().split("__");
        if (splittedId.length == 2)
            currentStudent.setId(splittedId[1]);

        String stateName;
        if (crl_id.equals("1116_mh_prajeetkumar"))
            stateName = "Rajasthan";
        else if (crl_id.equals("48_aniljadhav305"))
            stateName = "Maharashtra";
        else
            stateName = "UttarPradesh";

        currentStudent.setVillage(stateName);
    }

    private void playAudio(String path) {
        playing = true;
        listenAnswer.setBackgroundResource(R.drawable.ic_stop);
        //set up MediaPlayer
        mp = new MediaPlayer();
        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudio();
                }
            });
        } catch (Exception e) {
            stopAudio();
            Toast.makeText(this, "Problem in audio playing!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        playing = false;
        listenAnswer.setBackgroundResource(R.drawable.ic_play_button);
        if (mp != null)
            mp.stop();
    }

    private void downloadFile(String firebaseDirectoryPath, final String localDirectoryPath, final String fileName) {
        // database storage
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mFirebaseStorage.getReference();
        StorageReference downloadRef = storageRef.child(/*userPath*/firebaseDirectoryPath + fileName + ".zip");
        final File fileNameOnDevice = new File(/*DOWNLOAD_DIR*/ localDirectoryPath + "/" + fileName + ".zip");

        final File local = new File(localDirectoryPath);
        if (!local.exists())
            if (!local.mkdirs())
                Toast.makeText(this, fileNameOnDevice + " can't be created.", Toast.LENGTH_SHORT).show();

        if (!fileNameOnDevice.exists()) {
            progressDialog = new ProgressDialog(this);
            //displaying progress dialog while fetching images
            AserSampleUtility.showProgressDialog(progressDialog, "Please wait...");
            downloadRef.getFile(fileNameOnDevice).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("File RecyclerView", "downloaded the file");
                            //dismissing the progress dialog
                            try {
                                // unzip downloaded file
                                AserSampleUtility.unzip(fileNameOnDevice, local);
                                File unzippedFolder = new File(localDirectoryPath + "/" + studentName + fileName);
                                File newName = new File(localDirectoryPath + "/" + fileName);
                                if (unzippedFolder.exists()) {
                                    unzippedFolder.renameTo(newName);
                                }
                                AserSampleUtility.dismissProgressDialog(progressDialog);
                                Toast.makeText(UpdateActivity.this,
                                        "Downloaded and unzipped the file successfully..",
                                        Toast.LENGTH_SHORT).show();
                                showQuestionsSequentially();
                            } catch (IOException e) {
                                AserSampleUtility.dismissProgressDialog(progressDialog);
                                Toast.makeText(UpdateActivity.this,
                                        "Problem in unzipping!!",
                                        Toast.LENGTH_SHORT).show();
                                UpdateActivity.this.finish();
                                fileNameOnDevice.deleteOnExit();
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("File RecyclerView", "Failed to download the file");
                    //dismissing the progress dialog
                    AserSampleUtility.dismissProgressDialog(progressDialog);
                    Log.d("pk***", "onFailure: " + exception.getMessage());
                    Log.d("pk***", "onFailure: " + fileNameOnDevice.getName());
                    Toast.makeText(UpdateActivity.this,
                            "Media for this student isn't pushed on server..",
                            Toast.LENGTH_SHORT).show();
                    UpdateActivity.this.finish();
                }
            });
        } else
            showQuestionsSequentially();
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    //updating data of student in unzipped zip file
    public boolean updateStudentInJson(String updatedFilePath) {
//        String path = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" + AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";
        // deleting old file
        String fileName = updatedFilePath + "/" + studentName + currentStudent.getId() + "INFO.json";
        Gson gson = new Gson();
        String studentJson = gson.toJson(AserSampleUtility.convertStudentForValidation(currentStudent));
        File file = new File(fileName);
        if (file.exists())
            file.delete();
        // creating new file
        File newFile = new File(updatedFilePath + "/" + currentStudent.getId() + "INFO.json");
        try {
            newFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(studentJson);
            outputStreamWriter.flush();
            fileOutputStream.getFD().sync();
            outputStreamWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //updating data on storage
    public boolean zipFileAtPath(String sourcePath, String toLocation) {
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin;
            origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                entry.setTime(sourceFile.lastModified()); // to keep modification time after unzipping
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                entry.setTime(file.lastModified()); // to keep modification time after unzipping
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    public void uploadDataToStorage(String fileUri) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
//      todo change file name
        final Uri file = Uri.fromFile(new File(fileUri));

        StorageReference riversRef = mStorageRef.child("StudentRecordings/" + crl_id + "/" + currentStudent.getId() + ".zip");

        StorageTask uploadTask = riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        AserSampleUtility.changeMessage(progressDialog, "Updating realtime data on firestore");
                        updateRealtimeData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        Log.v("Failure", "failed");
                    }
                });

    }

    private void updateRealtimeData() {
        DatabaseReference studentReference = FirebaseDatabase.getInstance().getReference("studentsCopy")
                .child(crl_id)
                .child(currentStudent.getId());

        studentReference.setValue(currentStudent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        AserSampleUtility.showToast(UpdateActivity.this, "Successfully updated!");
                        UpdateActivity.this.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        AserSampleUtility.showToast(UpdateActivity.this, "Updating failed!");
                    }
                });
    }
}
