package info.pratham.asersample.fragments.math;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.QueLevel;
import info.pratham.asersample.database.modalClasses.SingleQustion;
import info.pratham.asersample.dialog.MistakeCountDialog;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.interfaces.MistakeCountListener;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

/**
 * Created by PEF on 24/11/2018.
 */

public class CalculationFragment extends BaseFragment implements WordsListListener, MistakeCountListener {

    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.questionSub1)
    TextView questionSub1;
    @BindView(R.id.questionSub2)
    TextView questionSub2;


    @BindView(R.id.questionDiv)
    TextView questionDiv;


    @BindView(R.id.answerSub1)
    EditText answerSub1;

    @BindView(R.id.answerSub2)
    EditText answerSub2;

    @BindView(R.id.quotient)
    EditText quotient;
    @BindView(R.id.remainder)
    EditText remainder;


    @BindView(R.id.subtractionLayout)
    LinearLayout subtractionLayout;

    @BindView(R.id.divisionLayout)
    LinearLayout divisionLayout;

    String currentLevel;


    List parentDataList;
    QueLevel queLevel;
    List tempSingleQue;
    MistakeCountDialog mistakCountDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calculation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        editorListener(answerSub1);
        editorListener(answerSub2);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // questionSub1.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
          //  questionSub2.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
            //questionDiv.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        } else {
         //   TextViewCompat.setAutoSizeTextTypeWithDefaults(questionSub1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        //    TextViewCompat.setAutoSizeTextTypeWithDefaults(questionSub1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            //TextViewCompat.setAutoSizeTextTypeWithDefaults(questionDiv, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }*/
        currentLevel = getArguments().getString("currentLevel");
        parentDataList = AserSample_Constant.getAserSample_Constant().getStudent().getSequenceList();
        if ("Subtraction".equals(currentLevel)) {
            showSubtraction();
        } else if ("Division".equals(currentLevel)) {
            showDivision();
        }
    }

    public void editorListener(final EditText view) {
        view.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT ||
                                actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            view.clearFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            return true;
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
    }

    public boolean writeSubtraction() {
        if (!answerSub1.getText().toString().isEmpty() || !answerSub2.getText().toString().isEmpty()) {
            queLevel = new QueLevel();
            queLevel.setLevel(currentLevel);
            queLevel.setSubject("Mathematics");
            queLevel.setLevel_seq_cnt(parentDataList.size());
            tempSingleQue = queLevel.getQuestions();
            if (!answerSub1.getText().toString().isEmpty()) {
                SingleQustion singleQustion = new SingleQustion();
                singleQustion.setQue_seq_cnt(tempSingleQue.size());
                singleQustion.setQue_id(questionSub1.getTag().toString());
                singleQustion.setQue_text(questionSub1.getText().toString());
                singleQustion.setAnswer(answerSub1.getText().toString());
                tempSingleQue.add(singleQustion);
            }
            if (!answerSub2.getText().toString().isEmpty()) {
                SingleQustion singleQustion = new SingleQustion();
                singleQustion.setQue_seq_cnt(tempSingleQue.size());
                singleQustion.setQue_id(questionSub2.getTag().toString());
                singleQustion.setQue_text(questionSub2.getText().toString());
                singleQustion.setAnswer(answerSub2.getText().toString());
                tempSingleQue.add(singleQustion);
            }
            parentDataList.add(queLevel);
            showMistakeCountDialog();
            return true;
        }
        return false;
    }

    public void setMistakes(int mistCnt) {
        queLevel.setMistakes(mistCnt);
    }

    public boolean writeDivision() {
        if (!quotient.getText().toString().isEmpty() && !remainder.getText().toString().isEmpty()) {
            queLevel = new QueLevel();
            queLevel.setLevel(currentLevel);
            queLevel.setSubject("Mathematics");
            queLevel.setLevel_seq_cnt(parentDataList.size());
            tempSingleQue = queLevel.getQuestions();

            SingleQustion singleQustion = new SingleQustion();
            singleQustion.setQue_seq_cnt(tempSingleQue.size());
            singleQustion.setQue_id(questionDiv.getTag().toString());
            singleQustion.setQue_text(questionDiv.getText().toString());
            singleQustion.setAnswer("Quotient: " + quotient.getText().toString() + "   Remainder :" + remainder.getText().toString());
            tempSingleQue.add(singleQustion);
            parentDataList.add(queLevel);
            showMistakeCountDialog();
            return true;
        }
        return false;
    }

    private void showSubtraction() {
        message.setText("Solve the subtraction");
        JSONArray msg = AserSample_Constant.getMathOperation(AserSample_Constant.sample, "Subtraction");
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(getActivity(), this, wordList, 2, currentLevel);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(getActivity(), "Problem in getting data");
        }
    }

    private void showDivision() {
        message.setText("Solve the division");
        JSONArray msg = AserSample_Constant.getMathOperation(AserSample_Constant.sample, "Division");
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(getActivity(), this, wordList, 1, currentLevel);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(getActivity(), "Problem in getting data");
        }
    }

    @Override
    public void getSelectedwords(List list) {
        try {
            if (currentLevel.equals("Subtraction")) {
                if (divisionLayout.isShown()) {
                    divisionLayout.setVisibility(View.GONE);
                }
                if (!subtractionLayout.isShown()) {
                    subtractionLayout.setVisibility(View.VISIBLE);
                }
                JSONObject js = (JSONObject) list.get(0);
                showQue(questionSub1, js);
                JSONObject js1 = (JSONObject) list.get(1);
                showQue(questionSub2, js1);
            } else if (currentLevel.equals("Division")) {
                if (!divisionLayout.isShown()) {
                    divisionLayout.setVisibility(View.VISIBLE);
                }
                if (subtractionLayout.isShown()) {
                    subtractionLayout.setVisibility(View.GONE);
                }
                JSONObject js = (JSONObject) list.get(0);
                showQue(questionDiv, js);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Problem in getting data", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

    }

    public void showQue(final TextView view, JSONObject jsonObject) {
        try {
            view.setText(jsonObject.getString("data"));
            view.setTag(jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showMistakeCountDialog() {
        mistakCountDialog = new MistakeCountDialog(getActivity(), currentLevel);
        mistakCountDialog.show();
    }

    @Override
    public void getMistakeCount(int mistakeCnt) {
        queLevel.setMistakes(mistakeCnt);
    }
}
