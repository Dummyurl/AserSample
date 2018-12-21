package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.interfaces.LevelFinishListner;

/**
 * Created by PEF on 21/12/2018.
 */

public class EndOfLevelDialog extends Dialog {
    @BindView(R.id.surveyTV)
    TextView survey;
    String msg;
    LevelFinishListner levelFinishListner;

    public EndOfLevelDialog(@NonNull Context context, String msg) {
        super(context, R.style.Theme_AppCompat_Light_NoActionBar);
        this.msg = msg;
        levelFinishListner = (LevelFinishListner) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_of_level_dialog);
        ButterKnife.bind(this);
        survey.setText(msg);
    }

    @OnClick(R.id.endOfSurveyButton)
    public void endOfSurvey() {
        levelFinishListner.onLevelFinish();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 50);
    }
}
