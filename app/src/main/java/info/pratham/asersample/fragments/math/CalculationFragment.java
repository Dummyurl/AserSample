package info.pratham.asersample.fragments.math;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.dialog.SelectWordsDialog;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

/**
 * Created by PEF on 24/11/2018.
 */

public class CalculationFragment extends BaseFragment implements WordsListListener {
    @BindView(R.id.questionSub1)
    TextView questionSub1;
    @BindView(R.id.questionSub2)
    TextView questionSub2;
    @BindView(R.id.questionDiv)
    TextView questionDiv;

    @BindView(R.id.subtractionLayout)
    LinearLayout subtractionLayout;

    @BindView(R.id.divisionLayout)
    LinearLayout divisionLayout;

    String currentLevel;

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
        if ("Subtraction".equals(currentLevel)) {
            showSubtraction();
        } else if ("Division".equals(currentLevel)) {
            showDivision();
        }
    }


    private void showSubtraction() {
        JSONArray msg = AserSample_Constant.getMathOperation(AserSample_Constant.sample, "Subtraction");
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(getActivity(), this, wordList, 2);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(getActivity(), "Something goes Wrong");
        }
    }

    private void showDivision() {
        JSONArray msg = AserSample_Constant.getMathOperation(AserSample_Constant.sample, "Division");
        if (msg != null) {
            List wordList = new ArrayList();
            try {
                for (int i = 0; i < msg.length(); i++) {
                    wordList.add(msg.getString(i));
                }
                SelectWordsDialog selectWordsDialog = new SelectWordsDialog(getActivity(), this, wordList, 1);
                selectWordsDialog.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            AserSampleUtility.showToast(getActivity(), "Something goes Wrong");
        }
    }

    @Override
    public void getSelectedwords(List list) {
        if (currentLevel.equals("Subtraction")) {
            if (divisionLayout.isShown()) {
                divisionLayout.setVisibility(View.GONE);
            }
            if (!subtractionLayout.isShown()) {
                subtractionLayout.setVisibility(View.VISIBLE);
            }
            questionSub1.setText(list.get(0).toString());
            questionSub2.setText(list.get(1).toString());
        } else if (currentLevel.equals("Division")) {
            if (!divisionLayout.isShown()) {
                divisionLayout.setVisibility(View.VISIBLE);
            }
            if (subtractionLayout.isShown()) {
                subtractionLayout.setVisibility(View.GONE);
            }
            questionDiv.setText(list.get(0).toString());
        }

    }
}
