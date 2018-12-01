package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.R;

/**
 * Created by PEF on 01/12/2018.
 */

public class ProficiencyDialog extends Dialog {
    List optionList;
    Context context;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;

    public ProficiencyDialog(@NonNull Context context, List optionList) {
        super(context);
        this.optionList = optionList;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proficiency_dialog);
        setTitle("SELECT PROFICIENCY");
        ButterKnife.bind(this);
        showOption();
    }

    private void showOption() {
        for (int i = 0; i < optionList.size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(optionList.get(i).toString());
            radioButton.setTextSize(20);
            radioButton.setPadding(10, 5, 10, 5);
            radioButton.setGravity(View.TEXT_ALIGNMENT_CENTER);
            View view = new View(context);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1));
            view.setBackgroundResource(R.color.light_blue);
            radiogroup.addView(radioButton);
            radiogroup.addView(view);
        }
    }
}
