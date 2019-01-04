package info.pratham.asersample.networkManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import info.pratham.asersample.fragments.PullCRl;
import info.pratham.asersample.interfaces.QuestionDataCompleteListener;
import info.pratham.asersample.utility.AserSampleUtility;

import static info.pratham.asersample.utility.AserSampleUtility.showProblemAlert;

/**
 * Created by pravin on 27/11/18.
 */

public class NetworkManager {
    private static NetworkManager instance;
    HashMap map = new HashMap<>();
    Context mContext;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    QuestionDataCompleteListener questionDataCompleteListener;

    public NetworkManager(Context _mContext, PullCRl pullCRl) {
        this.mContext = _mContext;
        progressDialog = new ProgressDialog(_mContext);
        questionDataCompleteListener = pullCRl;
    }
    /*public NetworkManager(Context _mContext) {
        this.mContext = _mContext;
        progressDialog = new ProgressDialog(mContext);
        questionDataCompleteListener = (QuestionDataCompleteListener)mContext;
    }*/

    /*public static synchronized NetworkManager getInstance(Context mContext) {
        if (instance == null) {
            instance = new NetworkManager(mContext, this);
        }
        return instance;
    }*/

    public void getQuestionData(final ProgressDialog progressDialog) {
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
                                showProblemAlert("Problem in getting data for questions. Please contact the administrator!", mContext);
                            } else {
                                // update question data in DB
                                updateOrReplaceQuestionData(map);
                            }
                        } else {
                            AserSampleUtility.dismissProgressDialog(NetworkManager.this.progressDialog);
                            showProblemAlert("Problem in getting data for questions. Please contact the administrator!", mContext);
                        }
                    }
                });
    }

    private void updateOrReplaceQuestionData(HashMap map) {
        Log.d("Size", "updateOrReplaceQuestionData: " + map.size());
        final List<Question> questionList = new ArrayList<>();
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

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AS_Database.getDatabaseInstance(mContext).getQuestiondao().insertAllQuestions(questionList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                questionDataCompleteListener.startPushingCrl();
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

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
