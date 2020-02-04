package info.pratham.asersample.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.R;
import info.pratham.asersample.adapters.CRLsAdapter;
import info.pratham.asersample.adapters.StudentsAdapter;
import info.pratham.asersample.adapters.StudentsRecyclerAdapter;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSampleUtility;

public class ValidateActivity extends AppCompatActivity {

    @BindView(R.id.crls_list)
    ListView crls_Listview;
    //    @BindView(R.id.students_list)
//    ListView students_recyclerView;
    @BindView(R.id.students_list)
    RecyclerView students_recyclerView;


    private String DOWNLOAD_DIR = Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DOWNLOADS).getPath();

    private DatabaseReference mGetReference;// reference

    private ProgressDialog progressDialog;

    private HashMap<String, List<Student>> allData;
    private String refOriginalURL;
    private int currentId;
    //    private String[] CRL_IDS;
    public static String currentCrl;
    private ArrayList<Student> STUDENTS;
    private ArrayList<String> CRLS;

    CRLsAdapter crlsAdapter;
    StudentsAdapter studentsAdapter;
    StudentsRecyclerAdapter studentsRecyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String TAG = ValidateActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        CRLS = new ArrayList<>();
        CRLS.add("1116_mh_prajeetkumar");
        CRLS.add("48_aniljadhav305");
        CRLS.add("1821_up_sanjeet");
        currentCrl = CRLS.get(0);
        crlsAdapter = new CRLsAdapter(this, CRLS);
        crls_Listview.setAdapter(crlsAdapter);

        STUDENTS = new ArrayList<>();
//        studentsAdapter = new StudentsAdapter(this, STUDENTS);
        studentsRecyclerAdapter = new StudentsRecyclerAdapter(this, STUDENTS);
        layoutManager = new GridLayoutManager(this, 1);
        students_recyclerView.setLayoutManager(layoutManager);
//        students_recyclerView.setHasFixedSize(true);
        students_recyclerView.setAdapter(studentsRecyclerAdapter);
        allData = new HashMap<>();
        progressDialog = new ProgressDialog(this);
        AserSampleUtility.showProgressDialog(progressDialog, "Updating data of validation..");
        listData();
//        deleteAllEntriesOfCrl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (crlsAdapter != null && crls_Listview != null) {
            crlsAdapter.notifyDataSetChanged();
            crls_Listview.setSelectionAfterHeaderView();
        }
//        if (studentsAdapter != null && students_recyclerView != null) {
//            studentsAdapter.notifyDataSetChanged();
//            students_recyclerView.setSelectionAfterHeaderView();
//        }
        if (studentsRecyclerAdapter != null && students_recyclerView != null) {
//            studentsAdapter.notifyDataSetChanged();
            studentsRecyclerAdapter.notifyDataSetChanged();
            students_recyclerView.getLayoutManager().scrollToPosition(0);
//            students_recyclerView.setSelectionAfterHeaderView();
        }
    }

    public void setStudentDataChanges(String item) {
        currentCrl = item;
        STUDENTS.clear();
        STUDENTS.addAll(allData.get(item));
//        studentsAdapter.notifyDataSetChanged();
        studentsRecyclerAdapter.notifyDataSetChanged();
    }

    public void deleteDataForCRL(final String crl) {
        final Dialog deleteConfirmationDialog = new Dialog(this);
        deleteConfirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteConfirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteConfirmationDialog.setContentView(R.layout.yes_no_dilog);
        TextView dia_title = deleteConfirmationDialog.findViewById(R.id.dia_title);
        dia_title.setText("Are you sure you want to delete the data of this Sample?");
        Button yesButton = deleteConfirmationDialog.findViewById(R.id.dia_yes_btn);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmationDialog.dismiss();
                String localDirectoryPath = DOWNLOAD_DIR + "/ASER_Audios/" + crl;
                Log.d("pk**", "onClick: " + localDirectoryPath);
                deleteRecursive(new File(localDirectoryPath));
                if (crlsAdapter != null)
                    crlsAdapter.notifyDataSetChanged();
            }
        });
        Button noButton = deleteConfirmationDialog.findViewById(R.id.dia_no_btn);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmationDialog.dismiss();
            }
        });
        deleteConfirmationDialog.show();
    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    public boolean checkIfDataExist(String crl) {
        String localDirectoryPath = DOWNLOAD_DIR + "/ASER_Audios/" + crl;
        return new File(localDirectoryPath).exists();
    }

    private void listData() {
        if (currentId < CRLS.size()) {
            refOriginalURL = "https://asersample.firebaseio.com/studentsCopy/" + CRLS.get(currentId);
            // realtime database
            mGetReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refOriginalURL);
            // Attach a listener to read the data at our posts reference
            mGetReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, HashMap<String, String>> crlsMap =
                            (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                    Set<String> studentNames = crlsMap.keySet();
                    Type listType = new TypeToken<ArrayList<Student>>() {
                    }.getType();
                    List<Student> studentList;
                    ArrayList students = new ArrayList();
                    for (String key : studentNames)
                        students.add(new JSONObject(crlsMap.get(key)));
                    studentList = new Gson().fromJson(students.toString(), listType);
                    Collections.sort(studentList, new Comparator<Student>() {
                        @Override
                        public int compare(Student o1, Student o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });
                    allData.put(CRLS.get(currentId), studentList);
                    Log.d("pk**", "onDataChange: AllData " + CRLS.get(currentId) + " : size - " + allData.get(CRLS.get(currentId)).size());
                    currentId += 1;
                    listData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        } else {
            currentId = 0;
            setStudentDataChanges(currentCrl);
            AserSampleUtility.dismissProgressDialog(progressDialog);
        }
    }

    // Functionalities
    private void moveRecord(DatabaseReference fromPath, final DatabaseReference toPath) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d("*prk*", "Success!");
                        } else {
                            Log.d("*prk*", "Copy failed!");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        fromPath.addListenerForSingleValueEvent(valueEventListener);
    }

    private void copyData() {
        Log.d("**pk*", "in copyData: ");
        refOriginalURL = "https://asersample.firebaseio.com/studentsDemoCopy";
        mGetReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refOriginalURL);
        mGetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("**pk*", "onDataChange: ");
                DatabaseReference mGetReference =
                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://asersample.firebaseio.com");
                mGetReference.child("studentsDemo2Copy").setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        if (task.isComplete()) {
                            Log.d("**pk*", "Success!");
                            Toast.makeText(ValidateActivity.this, "Copied", Toast.LENGTH_LONG).show();
//                            keepDataOfThreeCrlsOnly();
                        } else {
                            Log.d("**pk*", "Copy failed!");
                            Toast.makeText(ValidateActivity.this, "Not Copied", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("**pk*", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    int crlcnt = 0;

    private void copyDataForThreeCrlsOnly() {
        Log.d("**pk*", "in copyData: ");
        if (crlcnt < CRLS.size()) {
            refOriginalURL = "https://asersample.firebaseio.com/students/" + CRLS.get(crlcnt);
            mGetReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refOriginalURL);

            mGetReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DatabaseReference mGetReference =
                            FirebaseDatabase.getInstance().getReferenceFromUrl("https://asersample.firebaseio.com");
                    mGetReference.child("studentsDemo2Copy").child(CRLS.get(crlcnt)).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete()) {
                                crlcnt++;
                                Log.d("**pk*", "Success!");
                                Toast.makeText(ValidateActivity.this, "Copied", Toast.LENGTH_LONG).show();
                                copyDataForThreeCrlsOnly();
                            } else {
                                Log.d("**pk*", "Copy failed!");
                                Toast.makeText(ValidateActivity.this, "Not Copied", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ValidateActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                }
            });
        } else {
//            crlcnt = 0;
        }
    }

    private void keepDataOfThreeCrlsOnly() {
        DatabaseReference copyRef = FirebaseDatabase.getInstance().getReference("studentsCopy");
        for (Map.Entry<String, List<Student>> entry : allData.entrySet()) {
            if (!CRLS.contains(entry.getKey()))
                copyRef.child(entry.getKey()).removeValue();
        }
    }

    private void deleteAllEntriesOfCrl() {
        //         Delete all entries of studentsCopy
        refOriginalURL = "https://asersample.firebaseio.com/studentsDemo2Copy";
        mGetReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refOriginalURL);
        mGetReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                changeIds();
            }
        });
    }

    private void deleteDataBeforeJuly() {
        ArrayList<Student> studs = new ArrayList<>();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("studentsCopy");

        for (String crl_id : CRLS) {
            studs.clear();
            studs.addAll(allData.get(crl_id));
            DatabaseReference crlRef = myRef.child(crl_id);
            Student student;
            for (int j = 0; j < studs.size(); j++) {
                try {
                    student = studs.get(j);
                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss");
                    String july_1 = "Mon Jul 01 00:00:00 IST 2019";
                    Date juneDate = sdf.parse(july_1);
                    Date studDate = sdf.parse(student.getDate());
                    if (juneDate.compareTo(studDate) > 0) {
                        crlRef.child(student.getId()).removeValue();
                        Log.d("pk**Mydate**", "deleteDataBeforeJuly: " + student.getDate());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    int cntr = 0;

    boolean onceDownloaded = false;

    private void changeIds() {
        if (currentId < CRLS.size()) {
            refOriginalURL = "https://asersample.firebaseio.com/students/" + CRLS.get(currentId);
            // realtime database
            mGetReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refOriginalURL);
            // Attach a listener to read the data at our posts reference
            mGetReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!onceDownloaded) {
                        HashMap<String, HashMap<String, String>> crlsMap =
                                (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                        Set<String> studentNames = crlsMap.keySet();
                        Type listType = new TypeToken<ArrayList<Student>>() {
                        }.getType();
                        List<Student> studentList;
                        ArrayList students = new ArrayList();
                        for (String key : studentNames)
                            students.add(new JSONObject(crlsMap.get(key)));
                        studentList = new Gson().fromJson(students.toString(), listType);
                        Collections.sort(studentList, new Comparator<Student>() {
                            @Override
                            public int compare(Student o1, Student o2) {
                                return o1.getName().compareToIgnoreCase(o2.getName());
                            }
                        });
                        allData.put(CRLS.get(currentId), studentList);
                        Log.d("pk**", "onDataChange: AllData " + CRLS.get(currentId) + " : size - " + allData.get(CRLS.get(currentId)).size());
                        currentId += 1;
                        changeIds();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onDataChange: Read failed" );
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        } else {
            onceDownloaded = true;
            AserSampleUtility.dismissProgressDialog(progressDialog);
            updateEntriesForIsCorrect();
        }
    }

    public void updateEntriesForIsCorrect() {
        progressDialog = new ProgressDialog(this);
        AserSampleUtility.showProgressDialog(progressDialog, "updatingEntriesForIsCorrect");
        Log.d("pk**", "updateEntriesForIsCorrect");
        for (int i = 0; i < allData.size(); i++) {
            String refOriginalURLhere = "https://asersample.firebaseio.com/studentsDemo2Copy/" + CRLS.get(i);
            mGetReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refOriginalURLhere);
            List<Student> studentArrayList = allData.get(CRLS.get(i));
            for (int j = 0; j < studentArrayList.size(); j++) {
                Student student = studentArrayList.get(j);
                Log.d("pk**", "updateEntriesForIsCorrectCount :"+ j + "Student :" + student.getSequenceList().toString());
                String id = student.getId();
                String[] splitted = id.split("__");
                if (splitted.length > 1)
                    mGetReference.child(splitted[1]).setValue(student);
                else
                    mGetReference.child(splitted[0]).setValue(student);
            }
            Log.d("pk**", (i+1) + " CRL done");
        }
        AserSampleUtility.dismissProgressDialog(progressDialog);
    }

    int newCntr = 0;
    public void updateValues(final View view) {
        if (newCntr < allData.size()) {
            Log.d("pk**", "UpdateValues");
                String refOriginalURLhere = "https://asersample.firebaseio.com/studentsDemo2Copy/" + CRLS.get(newCntr);
                mGetReference = FirebaseDatabase.getInstance().getReferenceFromUrl(refOriginalURLhere);
                mGetReference.setValue(allData.get(CRLS.get(newCntr))).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            newCntr++;
                            Log.d("**pk*", "Success!");
                            Toast.makeText(ValidateActivity.this, "Copied", Toast.LENGTH_LONG).show();
                            updateValues(view);
                        } else {
                            Log.d("**pk*", "Copy failed!");
                            Toast.makeText(ValidateActivity.this, "Not Copied", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        } else {
            AserSampleUtility.dismissProgressDialog(progressDialog);
        }
    }
}
