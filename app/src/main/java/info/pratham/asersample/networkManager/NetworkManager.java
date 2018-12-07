package info.pratham.asersample.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.pratham.asersample.database.AS_Database;
import info.pratham.asersample.database.modalClasses.Question;
import info.pratham.asersample.utility.AserSampleUtility;

/**
 * Created by pravin on 27/11/18.
 */

public class NetworkManager {
    private static NetworkManager instance;
    HashMap map = new HashMap<>();
    Context mContext;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    public NetworkManager(Context _mContext) {
        this.mContext = _mContext;
        progressDialog = new ProgressDialog(mContext);

    }

    public static synchronized NetworkManager getInstance(Context mContext) {
        if (instance == null) {
            instance = new NetworkManager(mContext);
        }
        return instance;
    }

    public void getQuestionData(/*final String language, final ProgressDialog dialog*/) {
        AserSampleUtility.showProgressDialog(progressDialog);
        db.collection("Question")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AserSampleUtility.dismissProgressDialog(progressDialog);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                map.put(document.getId(), document.getData());
                            }

                            if (map.isEmpty()) {
                                // Data unavailable on server
                                showProblemAlert();
                            } else {
                                // update question data in DB
                                updateOrReplaceQuestionData(map);
                            }
                        } else {
                            Log.w("Alert", "Error getting documents.", task.getException());
                            showProblemAlert();
                        }
                    }
                });
    }

    private void showProblemAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Problem with the server!!");
        builder.setMessage("Please contact the administrator");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void updateOrReplaceQuestionData(HashMap map) {
        Log.d("Size", "updateOrReplaceQuestionData: " + map.size());
        List<Question> questionList = new ArrayList<>();
        Question question;
        Set set = map.entrySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            question = new Question();
            Map.Entry mentry = (Map.Entry) iterator.next();
            question.setLanguage(mentry.getKey().toString());
            question.setDataJson((new JSONObject((HashMap) mentry.getValue())).toString());
            questionList.add(question);
        }
        AS_Database.getDatabaseInstance(mContext).getQuestiondao().insertAllQuestions(questionList);
    }

    /*public void updateProficiencydata() {
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        keys.add("Name");
        values.add(AppPreference.getInstance(mContext).getStudentName());

        keys.add("Volunteer");
        values.add(AppPreference.getInstance(mContext).getVolunteerName());

        keys.add("Selected Language");
        values.add(AppPreference.getInstance(mContext).getSelectedLanguage());

        keys.add("Father name");
        values.add(AppPreference.getInstance(mContext).getFatherName());

        keys.add("Village");
        values.add(AppPreference.getInstance(mContext).getVillageName());

        keys.add("Class");
        values.add(AppPreference.getInstance(mContext).getClassName());

        keys.add("Age");
        values.add(AppPreference.getInstance(mContext).getStudentAge());
        if (AppPreference.getInstance(mContext).getStudentHistory() == null) {
            ArrayList<JsonObject> newObj = new ArrayList<>();
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(getJson(keys, values).toString());
            newObj.add(gsonObject);
            Gson gson = new Gson();

            String json = gson.toJson(newObj);
            AppPreference.getInstance(mContext).setStudentHistory(json);
            Log.d("ABC", AppPreference.getInstance(mContext).getStudentHistory());

        } else {
            Gson gson = new Gson();
            String json = AppPreference.getInstance(mContext).getStudentHistory();
            Type type = new TypeToken<ArrayList<JsonObject>>() {
            }.getType();
            ArrayList<JsonObject> arrayList = gson.fromJson(json, type);
            Log.d("ArrayList", arrayList.toString());

            ArrayList<String> keys1 = new ArrayList<>();
            ArrayList<String> values1 = new ArrayList<>();

            keys1.add("Name");
            values1.add(AppPreference.getInstance(mContext).getStudentName());

            keys1.add("Volunteer");
            values1.add(AppPreference.getInstance(mContext).getVolunteerName());

            keys1.add("Father name");
            values1.add(AppPreference.getInstance(mContext).getFatherName());

            keys1.add("Village");
            values1.add(AppPreference.getInstance(mContext).getVillageName());

            keys1.add("Class");
            values1.add(AppPreference.getInstance(mContext).getClassName());

            keys1.add("Age");
            values1.add(AppPreference.getInstance(mContext).getStudentAge());
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(getJson(keys1, values1).toString());
            arrayList.add(gsonObject);
            String json2 = gson.toJson(arrayList);
            AppPreference.getInstance(mContext).setStudentHistory(json2);
        }
    }*/

    public JSONObject getJson(ArrayList keys, ArrayList values) {
        JSONObject obj = new JSONObject();
        try {
            for (int i = 0; i < keys.size(); i++) {
                obj.put(keys.get(i).toString(), values.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
