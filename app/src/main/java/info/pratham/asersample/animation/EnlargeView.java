package info.pratham.asersample.animation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.ASERApplication;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.database.modalClasses.SingleQuestionNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.interfaces.GetTimeListener;
import info.pratham.asersample.interfaces.RefreshRecycler;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;
import info.pratham.asersample.utility.ListConstant;

public class EnlargeView extends Dialog {
    @BindView(R.id.text)
    TextView textView;

    @BindView(R.id.number1)
    TextView number1;

    @BindView(R.id.number2)
    TextView number2;

    @BindView(R.id.instruction)
    TextView instruction;


    @BindView(R.id.sun_ans)
    EditText sun_ans;


    @BindView(R.id.quotient)
    EditText quotient;

    @BindView(R.id.remainder)
    EditText remainder;
    @BindView(R.id.divisor)
    TextView divisor;
    @BindView(R.id.dividend)
    TextView dividend;

   /* @BindView(R.id.quotient)
    EditText quotient_ans;

    @BindView(R.id.remainder)
    EditText remainder_ans;*/

    Dialog dialogParent;

    @BindView(R.id.reading_task)
    RelativeLayout reading_task;

    @BindView(R.id.mathematics_operation)
    RelativeLayout mathematics_operation;

    @BindView(R.id.mathematics_division)
    RelativeLayout mathematics_division;
    private String que_text;
    private String level;
    private String que_id;
    private Context mContext;
    private String startTime, endTime;
    //private String answer, remainder_ans;
    private boolean isAttemptedQue = false;
    private GetTimeListener getTimeListener;
    private RefreshRecycler refreshRecycler;
    private QuestionStructure questionStructure;

    public EnlargeView(@NonNull Context context, QuestionStructure questionStructure, String level, boolean isAttemptedQue, RecyclerView.Adapter recyclerViewAdapter) {
        super(context, R.style.Transparent);
        this.que_text = questionStructure.getData();
        this.level = level;
        this.que_id = questionStructure.getId();
        this.questionStructure = questionStructure;
        this.mContext = context;
        this.isAttemptedQue = isAttemptedQue;
        getTimeListener = (GetTimeListener) context;
        refreshRecycler = (RefreshRecycler) recyclerViewAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.transperentbg)));
        setContentView(R.layout.activity_enlaege_view);
        ButterKnife.bind(this);
        dialogParent = this;
        startTime = getTimeListener.getTime();
        if (level.equals(mContext.getString(R.string.Subtraction))) {
            reading_task.setVisibility(View.GONE);
            mathematics_division.setVisibility(View.GONE);
            mathematics_operation.setVisibility(View.VISIBLE);
            String[] numbers = que_text.split("-");
            if (numbers.length == 2) {
                number1.setText(numbers[0].trim());
                number2.setText(numbers[1].trim());
                if (isAttemptedQue) {
                    Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
                    List<SingleQuestionNew> temp = studentNew.getSequenceList();
                    //if question is old then remove old entry
                    if (isAttemptedQue) {
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).getQue_id().equals(que_id)) {
                                sun_ans.setText(temp.get(i).getAnswer());
                                break;
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "Some thing went wrong..", Toast.LENGTH_SHORT).show();
            }

        } else if (level.equals(mContext.getString(R.string.Division))) {
            reading_task.setVisibility(View.GONE);
            mathematics_operation.setVisibility(View.GONE);
            mathematics_division.setVisibility(View.VISIBLE);
            String[] numbers = que_text.split("/");
            if (numbers.length == 2) {
                dividend.setText(numbers[0].trim());
                divisor.setText(numbers[1].trim());
                if (isAttemptedQue) {
                    Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
                    List<SingleQuestionNew> temp = studentNew.getSequenceList();
                    //if question is old then remove old entry
                    if (isAttemptedQue) {
                        for (int i = 0; i < temp.size(); i++) {
                            if (temp.get(i).getQue_id().equals(que_id)) {
                                quotient.setText(temp.get(i).getAnswer());
                                remainder.setText(temp.get(i).getRemainder());
                                break;
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(mContext, "Some thing went wrong..", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (level.equals("Double") || level.equals("Single")) {
                instruction.setText("Read above number loudly and press back");
            } else {
                instruction.setText("Read above text loudly and press back");
            }

            reading_task.setVisibility(View.VISIBLE);
            mathematics_operation.setVisibility(View.GONE);
            mathematics_division.setVisibility(View.GONE);
            textView.setText(que_text);
            if (level.equals("Para") || level.equals("Sentence")) {
                textView.setTextSize(1, 50);
            } else if (level.equals("Story")) {
                textView.setTextSize(1, 30);
            }
        }
    }


    @OnClick(R.id.close)
    public void closeBtb() {
        /*Intent intent = new Intent();
        setResult(2, intent);
        finish();*/
        autoCheck();
    }

    private void autoCheck() {
        AudioUtil.stopRecording(mContext);
        if (level.equals("Subtraction") || level.equals("Division")) {
            addEntry(null);
        } else {
            try {
                //    "{\"Ground Truth\":\"I like to sing.\",\"Confidence\":\"0.12\", \"Transcript\":\"house\"}"
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Ground Truth", que_text);
                jsonObject.put("Confidence", "0.12");
                jsonObject.put("Transcript", "house");


                azure_model(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    /*private String getBodyParameter(String Student, String Proficiency, String Ground_Truth, String IsCorrect, String Transcript, String Confidence) {
        Body body = new Body();
        body.inputs.new parameters(Student, Proficiency, Ground_Truth, IsCorrect, Transcript, Confidence);
        Gson gson = new Gson();
        return gson.toJson(body);

        *//*return "{\n" +
                "    \"Inputs\": {\n" +
                "    \"input1\": [{\n" +
                "    \"Student\": \"value1\",\n" +
                "    \"Proficiency\": \"S\",\n" +
                "    \"Ground Truth\": \"What is the time\",\n" +
                "    \"IsCorrect\": \"false\",\n" +
                "    \"Transcript\": \"may be\",\n" +
                "    \"Confidence\": \"1\"\n" +
                "    }]\n" +
                "    },\n" +
                "    \"GlobalParameters\": {}\n" +
                "    }";*//*
    }

*/
    private void azure_model(JSONObject jsonObject) {
        //todo "HI_S4_S_1.wav" remove and uncoment below line
        String currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
        AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/" + questionStructure.getId() + ".mp3";
        File file = new File(currentFilePath);
        if(file.exists()){
            boolean f=true;
        }
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("loading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = "http://ec2-54-224-187-187.compute-1.amazonaws.com/api";


        AndroidNetworking.upload(url)
                .addMultipartParameter("Ground Truth",que_text)
                .addMultipartFile("file", file)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        addEntry(response.toString());
                        Log.d("data", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        addEntry(anError.toString());
                        Log.d("data", anError.toString());
                    }
                });












    /*    AndroidNetworking.post(url)
                .addHeaders("Content-Type", "text/plain")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        addEntry(response.toString());
                        Log.d("data", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        addEntry(anError.toString());
                        Log.d("data", anError.toString());
                    }
                });*/
    }

    @Override
    public void onBackPressed() {
        autoCheck();
    }

    private void addEntry(String response) {
        boolean istrue = true;
        Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
        List<SingleQuestionNew> temp = studentNew.getSequenceList();

        //if question is old then remove old entry
        if (isAttemptedQue) {
          /*  for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getQue_id().equals(que_id)) {
                    temp.remove(i);
                    questionStructure.setIsCorrect(AserSample_Constant.NOTATTEMPED);
                    questionStructure.setNoOfMistakes(null);
                    break;
                }
            }*/
            // Iterator to traverse the list
            Iterator iterator = temp.iterator();
            while (iterator.hasNext()) {
                SingleQuestionNew singleQuestionNew = (SingleQuestionNew) iterator.next();
                if (singleQuestionNew.getQue_id().equals(que_id)) {
                    iterator.remove();
                    questionStructure.setIsCorrect(AserSample_Constant.NOTATTEMPED);
                    questionStructure.setNoOfMistakes(null);
                    questionStructure.setAzure_Scored_Labels(AserSample_Constant.NOTATTEMPED);
                    break;
                }
            }
        }

        // endTime = getTimeListener.getTime();
        SingleQuestionNew singleQuestionNew = new SingleQuestionNew();
        singleQuestionNew.setQue_id(que_id);
        singleQuestionNew.setQue_text(que_text);
        //Disable start and end time
        // singleQuestionNew.setStartTime(startTime);
        //singleQuestionNew.setEndTime(endTime);
        if (level.equals(mContext.getString(R.string.Subtraction))) {
            String subtraction = sun_ans.getText().toString();
            if (subtraction.isEmpty()) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Answer is empty do you want to leave it?")
                        .setPositiveButton("yes", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                questionStructure.setSelected(false);
                                refreshRecycler.refreshRecycler();
                                dialogParent.dismiss();
                            }
                        }).setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            } else {

                String[] numbers = que_text.split("-");
                int number1 = Integer.parseInt(numbers[0].trim());
                int number2 = Integer.parseInt(numbers[1].trim());
                int ans = number1 - number2;
                int subAnsByUser = Integer.parseInt(subtraction);
                if (ans == subAnsByUser) {
                    questionStructure.setIsCorrect(AserSample_Constant.CORRECT);
                    singleQuestionNew.setAzure_Scored_Labels(AserSample_Constant.CORRECT);
                    singleQuestionNew.setCorrect(true);
                    questionStructure.setAzure_Scored_Labels(AserSample_Constant.CORRECT);

                } else {
                    questionStructure.setIsCorrect(AserSample_Constant.WRONG);
                    singleQuestionNew.setAzure_Scored_Labels(AserSample_Constant.WRONG);
                    questionStructure.setAzure_Scored_Labels(AserSample_Constant.WRONG);
                    singleQuestionNew.setCorrect(false);
                }
                singleQuestionNew.setAnswer(subtraction);
                addQuestionToAnswerList(studentNew, singleQuestionNew);
                dialogParent.dismiss();
            }
        } else if (level.equals(mContext.getString(R.string.Division))) {
            String quotient_ans = quotient.getText().toString();
            String remainder_ans = remainder.getText().toString();
            if (!quotient_ans.isEmpty() || !remainder_ans.isEmpty()) {

                String[] numbers = que_text.split("/");
                int number1 = Integer.parseInt(numbers[0].trim());
                int number2 = Integer.parseInt(numbers[1].trim());
                int quo_ans = number1 / number2;
                int quotientAnsByUser = Integer.parseInt(quotient_ans);
                int remain_ans = number1 % number2;
                int remainderAnsByUser = Integer.parseInt(remainder_ans);

                if ((quo_ans == quotientAnsByUser) && (remain_ans == remainderAnsByUser)) {
                    questionStructure.setIsCorrect(AserSample_Constant.CORRECT);
                    singleQuestionNew.setAzure_Scored_Labels(AserSample_Constant.CORRECT);
                    singleQuestionNew.setCorrect(true);
                    questionStructure.setAzure_Scored_Labels(AserSample_Constant.CORRECT);

                } else {
                    questionStructure.setIsCorrect(AserSample_Constant.WRONG);
                    singleQuestionNew.setAzure_Scored_Labels(AserSample_Constant.WRONG);
                    questionStructure.setAzure_Scored_Labels(AserSample_Constant.WRONG);
                    singleQuestionNew.setCorrect(false);
                }

                singleQuestionNew.setAnswer(quotient_ans);
                singleQuestionNew.setRemainder(remainder_ans);
                addQuestionToAnswerList(studentNew, singleQuestionNew);
                dialogParent.dismiss();
            } else {
                new AlertDialog.Builder(getContext())
                        .setMessage("Answer is empty do you want to leave it?")
                        .setPositiveButton("yes", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                questionStructure.setSelected(false);
                                refreshRecycler.refreshRecycler();
                                dialogParent.dismiss();
                            }
                        }).setNegativeButton("No", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        } else {
            if (response != null) {
                try {
                    JSONObject result = new JSONObject(response);
                    //set value of azure response if get on ERROR
                    // singleQuestionNew.setAzure_Ground_Truth(result.getString("Ground Truth"));
                    //  singleQuestionNew.setAzure_Confidence(result.getString("Confidence"));
                    singleQuestionNew.setAzure_Distance(result.getString("Distance"));
                    singleQuestionNew.setAzure_Scored_Labels(result.getString("Scored Labels"));
                    singleQuestionNew.setAzure_Scored_Probabilities(result.getString("Scored Probabilities"));

                } catch (Exception e) {
                    e.printStackTrace();
                    // set value of azure response if get on ERROR
                    singleQuestionNew.setAzure_Ground_Truth("ERROR");
                    singleQuestionNew.setAzure_Confidence("ERROR");
                    singleQuestionNew.setAzure_Distance("ERROR");
                    singleQuestionNew.setAzure_Scored_Labels("ERROR");
                    singleQuestionNew.setAzure_Scored_Probabilities("ERROR");

                }
            } else {
                //set value of azure response if question is mathematics(division/subtraction)
                singleQuestionNew.setAzure_Ground_Truth("MATHEMATICS_OPERATION");
                singleQuestionNew.setAzure_Confidence("MATHEMATICS_OPERATION");
                singleQuestionNew.setAzure_Distance("MATHEMATICS_OPERATION");
                singleQuestionNew.setAzure_Scored_Labels("MATHEMATICS_OPERATION");
                singleQuestionNew.setAzure_Scored_Probabilities("MATHEMATICS_OPERATION");
            }

            singleQuestionNew.setRecordingName(que_id + ".mp3");
            //set server side answer
            if (istrue) {
                questionStructure.setIsCorrect(AserSample_Constant.CORRECT);
                singleQuestionNew.setAzure_Scored_Labels(AserSample_Constant.CORRECT);
                singleQuestionNew.setCorrect(true);
                questionStructure.setAzure_Scored_Labels(AserSample_Constant.CORRECT);

            } else {
                questionStructure.setIsCorrect(AserSample_Constant.WRONG);
                singleQuestionNew.setAzure_Scored_Labels(AserSample_Constant.WRONG);
                questionStructure.setAzure_Scored_Labels(AserSample_Constant.WRONG);
                singleQuestionNew.setCorrect(false);
            }
            addQuestionToAnswerList(studentNew, singleQuestionNew);
            dialogParent.dismiss();
        }
    }

    private void addQuestionToAnswerList(Student studentNew, SingleQuestionNew singleQuestionNew) {

        studentNew.getSequenceList().add(singleQuestionNew);
        //if quetion is new then only increment counter
        if (!isAttemptedQue) {
            switch (level) {
                //NATIVE LANGUAGE
                case "Words":
                    ListConstant.Words_cnt++;
                    break;
                case "Letters":
                    ListConstant.Letters_cnt++;
                    break;
                case "Para":
                    ListConstant.Para_cnt++;
                    break;
                case "Story":
                    ListConstant.Story_cnt++;
                    break;

                case "Capital":
                    ListConstant.Capital_cnt++;
                    break;
                case "Small":
                    ListConstant.Small_cnt++;
                    break;
                case "word":
                    ListConstant.engWord_cnt++;
                    break;
                case "Sentence":
                    ListConstant.Sentence_cnt++;
                    break;
                //MATHEMATICS
                case "Single":
                    ListConstant.Single_cnt++;
                    break;
                case "Double":
                    ListConstant.Double_cnt++;
                    break;
                case "Subtraction":
                    ListConstant.Subtraction_cnt++;
                    break;
                case "Division":
                    ListConstant.Division_cnt++;
                    break;
            }
        }
    }

}
