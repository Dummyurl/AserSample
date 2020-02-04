package info.pratham.asersample.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;

public class OtpLoginActivity extends AppCompatActivity {

    @BindView(R.id.et_phone_number)
    EditText etPhonenumber;
    @BindView(R.id.et_otp)
    EditText etOTP;
    @BindView(R.id.btn_generate_otp)
    Button btnGenerateOTP;

    String phoneNumber, otp;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        startFirebaseLogin();
    }

    @OnClick(R.id.btn_sign_in)
    public void signIn() {
        otp = etOTP.getText().toString();
        if (!otp.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
            signInWithPhone(credential);
        } else
            Toast.makeText(OtpLoginActivity.this, "Empty OTP", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_generate_otp)
    public void generateOtp() {
        phoneNumber = etPhonenumber.getText().toString();
        if (!phoneNumber.contains("+91"))
            phoneNumber = "+91" + phoneNumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,           // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,               // Unit of timeout
                this,      // Activity (for callback binding)
                mCallback);                     // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            startActivity(new Intent(OtpLoginActivity.this,SignedIn.class));
//                            finish();
                            Toast.makeText(OtpLoginActivity.this, "Correct OTP", Toast.LENGTH_SHORT).show();
                        } else {
                            etOTP.setError(task.getException().getMessage());
                            Toast.makeText(OtpLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void startFirebaseLogin() {
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(OtpLoginActivity.this, "verification completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("pk****", "onVerificationFailed: " + e.getMessage());
                Toast.makeText(OtpLoginActivity.this, "verification failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(verificationId, forceResendingToken);
                verificationCode = verificationId;
                Toast.makeText(OtpLoginActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
