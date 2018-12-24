package info.pratham.asersample.fragments.math;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @BindView(R.id.answerDiv)
    EditText answerDiv;


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
        currentLevel = getArguments().getString("currentLevel");
        parentDataList = AserSample_Constant.getAserSample_Constant().getStudent().getTestQuestionList();
        if ("Subtraction".equals(currentLevel)) {
            showSubtraction();
        } else if ("Division".equals(currentLevel)) {
            showDivision();
        }


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
        if (!answerDiv.getText().toString().isEmpty()) {
            queLevel = new QueLevel();
            queLevel.setLevel(currentLevel);
            queLevel.setSubject("Mathematics");
            queLevel.setLevel_seq_cnt(parentDataList.size());
            tempSingleQue = queLevel.getQuestions();

            SingleQustion singleQustion = new SingleQustion();
            singleQustion.setQue_seq_cnt(tempSingleQue.size());
            singleQustion.setQue_id(questionDiv.getTag().toString());
            singleQustion.setQue_text(questionDiv.getText().toString());
            singleQustion.setAnswer(answerDiv.getText().toString());
            tempSingleQue.add(singleQustion);
            parentDataList.add(queLevel);
            showMistakeCountDialog();
            return true;
        }
        return false;
    }

    private void showSubtraction() {
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
        JSONArray msg = AserSample_Constant.getMathOperation(AserSample_Constant.sample, "Division");
        if (msg != null) {
            List<JSONObject> wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getJSONObject(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(getActivity(), this, wordList, 1,currentLevel);
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
              /*  questionSub1.setText(list.get(0).toString());
                questionSub2.setText(list.get(1).toString());*/

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
                //questionDiv.setText(list.get(0).toString());
                JSONObject js = (JSONObject) list.get(0);
                showQue(questionDiv, js);
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Data not get", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

    }

    public void showQue(final TextView view, JSONObject jsonObject) {
        try {
            view.setText(jsonObject.getString("data"));
            view.setTag(jsonObject.getString("id"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "" + view.getTag(), Toast.LENGTH_SHORT).show();
                }
            });
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
