package info.pratham.asersample.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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
    List<JSONObject> wordList;
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
      //  getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    /*    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;*/

        setTitle("Selection dialog");
        txt_message_village.setText("Select " + selectCount + " item(s) among the following");
        setCanceledOnTouchOutside(false);
        try {
            setCancelable(false);
            for (int i = 0; i < wordList.size(); i++) {
                CheckBox checkBox = new CheckBox(context);
                checkBox.setText(wordList.get(i).getString("data"));
                checkBox.setTag(wordList.get(i).getString("id"));
                checkBox.setTextSize(1, 35);
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
        } catch (JSONException e) {
            e.printStackTrace();
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
        List<JSONObject> wordList = new ArrayList();
        JSONObject jsObj;
        try {
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    jsObj = new JSONObject();
                    jsObj.put("id", checkBoxes.get(i).getTag().toString());
                    jsObj.put("data", checkBoxes.get(i).getText().toString());
                    wordList.add(jsObj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

