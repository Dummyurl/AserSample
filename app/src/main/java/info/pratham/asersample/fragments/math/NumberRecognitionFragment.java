package info.pratham.asersample.fragments.math;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.MathActivity;
import info.pratham.asersample.database.modalClasses.QueLevel;
import info.pratham.asersample.database.modalClasses.SingleQustion;
import info.pratham.asersample.utility.AserSample_Constant;

import static android.widget.TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;

/**
 * Created by PEF on 24/11/2018.
 */

public class NumberRecognitionFragment extends BaseFragment {
    @BindView(R.id.question)
    TextView question;

    @BindView(R.id.nextItem)
    ImageView nextItem;
    @BindView(R.id.prevItem)
    ImageView prevItem;
    @BindView(R.id.fragmengtRefreshIV)
    ImageView refreshIcon;

    List<JSONObject> selectedWordsList;
    int wordCOunt;
    List<QueLevel> parentDataList;
    public String attemptedQuePathCache;
    public boolean prevAttempted = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_number_recognition, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        nextItem.setVisibility(View.INVISIBLE);
        prevItem.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            question.setAutoSizeTextTypeWithDefaults(AUTO_SIZE_TEXT_TYPE_UNIFORM);
        } else {
            TextViewCompat.setAutoSizeTextTypeWithDefaults(question, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
        wordCOunt = -1;
        selectedWordsList = (List) getArguments().getSerializable("data");
        parentDataList = AserSample_Constant.getAserSample_Constant().getStudent().getSequenceList();

        if (!nextItem.isShown()) {
            nextItem.setVisibility(View.VISIBLE);
        }
        showNextItem();
    }

    @OnClick(R.id.nextItem)
    public void showNextItem() {
        ((MathActivity) getActivity()).initiateRecording();
        wordCOunt++;
        showQue(selectedWordsList.get(wordCOunt));
        if (wordCOunt == 1) {
            if (!prevItem.isShown()) {
                prevItem.setVisibility(View.VISIBLE);
            }
        }
        if ((wordCOunt + 1) == selectedWordsList.size()) {
            if (nextItem.isShown()) {
                nextItem.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick(R.id.prevItem)
    public void showPrevItem() {
        ((MathActivity) getActivity()).initiateRecording();
        wordCOunt--;
        showQue(selectedWordsList.get(wordCOunt));
        if (wordCOunt == 0) {
            if (prevItem.isShown()) {
                prevItem.setVisibility(View.INVISIBLE);
                nextItem.setVisibility(View.VISIBLE);
            }
        }

        if (wordCOunt > -1) {
            if (prevItem.isShown()) {
                nextItem.setVisibility(View.VISIBLE);
            }
        }

    }

    public String getQuestionIdByView() {
        return question.getTag().toString();
    }

    public String getQuestionTextByView() {
        return question.getText().toString();
    }

    public View getRefreshIconView() {
        return refreshIcon;
    }

    public void blurView(float ratio) {
        question.setAlpha(ratio);
    }

    @OnClick(R.id.fragmengtRefreshIV)
    public void refreshRecording() {
        ((MathActivity) getActivity()).initiateRecording();
    }

    private void showQue(JSONObject msg) {
        try {
            prevAttempted = false;
            for (QueLevel queLevel : parentDataList) {
                if (queLevel.getLevel().equals(getString(R.string.tenToNinetyNine)) || queLevel.getLevel().equals(getString(R.string.oneToNine))) {
                    for (SingleQustion singleQustion : queLevel.getQuestions()) {
                        if (singleQustion.getQue_id().equals(msg.getString("id"))) {
                            prevAttempted = true;
                            attemptedQuePathCache = singleQustion.getRecordingName();
                        }
                    }
                    /*if (prevAttempted) {
                        break;
                    }*/
                }
            }
            if (!prevAttempted) {
                for (SingleQustion singleQustion : ((MathActivity) getActivity()).queLevel.getQuestions()) {
                    if (singleQustion.getQue_id().equals(msg.getString("id"))) {
                        prevAttempted = true;
                        attemptedQuePathCache = singleQustion.getRecordingName();
                    }
                }
            }

            if (prevAttempted) {
                refreshIcon.setVisibility(View.VISIBLE);
                question.setAlpha(0.5f);
            }

            ((MathActivity) getActivity()).updateUI(prevAttempted);

            MathActivity.isNewQuestion = true;
            question.setText(msg.getString("data"));
            question.setTag(msg.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
