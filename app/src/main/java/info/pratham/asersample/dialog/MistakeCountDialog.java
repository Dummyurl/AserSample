package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.interfaces.MistakeCountListener;

/**
 * Created by PEF on 18/12/2018.
 */

public class MistakeCountDialog extends Dialog {

    @BindView(R.id.mistake_cnt)
    EditText et_mistake_cnt;
    @BindView(R.id.level)
    TextView level;

    MistakeCountListener mistakeCountListener;
    Context context;
    String levelString;

    public MistakeCountDialog(@NonNull Context context, String level) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mistake_count_dialog);
        mistakeCountListener = (MistakeCountListener) context;
        this.context = context;
        levelString = level;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        level.setText(levelString + " mistakes");
    }

    @OnClick(R.id.submit_mistake)
    public void submitMistake() {
        String text = et_mistake_cnt.getText().toString().trim();
        if (!text.isEmpty()) {
            dismiss();
            mistakeCountListener.getMistakeCount(Integer.parseInt(text));
        } else {
            Toast.makeText(context, "Enter A Mistake count", Toast.LENGTH_SHORT).show();
        }
    }
}
