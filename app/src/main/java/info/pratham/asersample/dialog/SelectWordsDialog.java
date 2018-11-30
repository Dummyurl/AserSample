package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.interfaces.WordsListListener;

public class SelectWordsDialog extends Dialog {

    @BindView(R.id.txt_clear_changes_village)
    TextView clear_changes;
    @BindView(R.id.txt_message_village)
    TextView txt_message_village;
    @BindView(R.id.flowLayout)
    GridLayout flowLayout;

    Context context;
    List wordList;
    List<CheckBox> checkBoxes = new ArrayList<>();
    WordsListListener wordsListListener;

    public SelectWordsDialog(@NonNull Context context, List tempList) {
        super(context);
        this.wordList = tempList;
        this.context = context;
        this.wordsListListener = (WordsListListener) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_words_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        for (int i = 0; i < wordList.size(); i++) {
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(wordList.get(i).toString());
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.FILL_HORIZONTAL);
            checkBox.setLayoutParams(param);
            flowLayout.addView(checkBox);
            checkBoxes.add(checkBox);
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
        wordsListListener.getSelectedwords(wordList);
        dismiss();
    }

}

