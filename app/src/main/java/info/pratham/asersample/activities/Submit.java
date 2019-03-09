package info.pratham.asersample.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import info.pratham.asersample.R;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.ListConstant;

public class Submit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        AserSampleUtility.writeStudentInJson(this);
        ListConstant.clearFields();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("fragment", "SelectLanguageFragment");
        startActivity(intent);
    }
}
