package info.pratham.asersample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.R;
import info.pratham.asersample.fragments.math.CalculationFragment;
import info.pratham.asersample.utility.AserSampleUtility;

public class MathActivity extends BaseActivity {

    String currentLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
        AserSampleUtility.showFragment(this, new CalculationFragment(), CalculationFragment.class.getSimpleName());
        showSubtraction();
    }

    private void showSubtraction() {
        currentLevel = getString(R.string.Subtraction);

    }

    @OnClick(R.id.markProficiency)
    public void markProficiency() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage("Is This Ok");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentLevel) {
                    case "Subtraction":
                        showDivision();
                        //todo open Division
                        break;
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentLevel) {
                    case "Subtraction":
                        //todo open Number Recognition
                        break;
                }
            }
        });
        dialog.show();
    }

    private void showDivision() {
    }
}
