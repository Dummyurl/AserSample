package info.pratham.asersample.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.interfaces.WordsListListener;
import info.pratham.asersample.utility.AserSampleUtility;

public class SelectWordsDialog extends Dialog {

    @BindView(R.id.txt_clear_changes_village)
    TextView clear_changes;
    @BindView(R.id.txt_message_village)
    TextView txt_message_village;
    @BindView(R.id.flowLayout)
    GridLayout flowLayout;

    Context context;
    List wordList;
    int selectCount;
    int count = 0;
    List<CheckBox> checkBoxes = new ArrayList<>();
    WordsListListener wordsListListener;

    public SelectWordsDialog(@NonNull Context context, List tempList, int selectCount) {
        super(context);
        this.wordList = tempList;
        this.context = context;
        this.wordsListListener = (WordsListListener) context;
        this.selectCount = selectCount;
    }

    public SelectWordsDialog(@NonNull Context context, Fragment fragment, List tempList, int selectCount) {
        super(context);
        this.wordList = tempList;
        this.context = context;
        this.wordsListListener = (WordsListListener) fragment;
        this.selectCount = selectCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_words_dialog);
        ButterKnife.bind(this);
        setTitle("Choose among the following");
        txt_message_village.setText("Select "+selectCount+" items among the following");
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        for (int i = 0; i < wordList.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(wordList.get(i).toString());
            checkBox.setTextSize(1, 25);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(param);
            flowLayout.addView(checkBox);
            checkBoxes.add(checkBox);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (count == selectCount && b) {
                        compoundButton.setChecked(false);
                        Toast.makeText(context, "You can select " + selectCount + " item(s) only", Toast.LENGTH_SHORT).show();
                    } else if (b) {
                        count++;
                    } else if (!b) {
                        count--;
                    }
                }
            });
        }
    }




   /* @OnClick(R.id.btn_close_village)
    public void closeDialog() {
        dismiss();
    }*/

    @OnClick(R.id.txt_clear_changes_village)
    public void clearChanges() {
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setChecked(false);
        }
    }

    @OnClick(R.id.txt_ok_village)
    public void ok() {
        List wordList = new ArrayList();
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                wordList.add(checkBoxes.get(i).getText());
            }
        }
        if (!wordList.isEmpty()) {
            if (wordList.size() != selectCount)
                AserSampleUtility.showToast((Activity) context, "Select at least " + selectCount + " item(s).");
            else {
                wordsListListener.getSelectedwords(wordList);
                dismiss();
            }
        } else
            AserSampleUtility.showToast((Activity) context, "Select at least " + selectCount + " item(s).");
    }

}

