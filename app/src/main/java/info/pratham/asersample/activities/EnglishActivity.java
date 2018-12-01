package info.pratham.asersample.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.utility.AserSample_Constant;

public class EnglishActivity extends BaseActivity {

    @BindView(R.id.question)
    TextView tv_question;

    JSONObject sample;
    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        sample = .

    }
}
