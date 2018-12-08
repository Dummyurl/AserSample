package info.pratham.asersample.fragments.math;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.MathActivity;

/**
 * Created by PEF on 24/11/2018.
 */

public class NumberRecognitionFragment extends BaseFragment {
    @BindView(R.id.question)
    TextView question;

    @BindView(R.id.nextItem)
    Button nextItem;
    @BindView(R.id.prevItem)
    Button prevItem;


    List selectedWordsList;
    int wordCOunt;


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

        wordCOunt = -1;
        selectedWordsList = (List) getArguments().getSerializable("data");

        if (!nextItem.isShown()) {
            nextItem.setVisibility(View.VISIBLE);
        }
        showNextItem();
    }

    @OnClick(R.id.nextItem)
    public void showNextItem() {
        ((MathActivity)getActivity()).initiateRecording();
        wordCOunt++;
        showQue(selectedWordsList.get(wordCOunt).toString());
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
        ((MathActivity)getActivity()).initiateRecording();
        wordCOunt--;
        showQue(selectedWordsList.get(wordCOunt).toString());
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

    public List getWordsList() {
        return selectedWordsList;
    }

    public int getWordsCount() {
        return wordCOunt;
    }

    private void showQue(String msg) {
        question.setText(msg);
    }
}
