package info.pratham.asersample.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.CRL;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

/**
 * Created by PEF on 24/11/2018.
 */

public class LoginFragment extends BaseFragment {

    @BindView(R.id.username)
    android.support.design.widget.TextInputEditText userName;
    @BindView(R.id.password)
    android.support.design.widget.TextInputEditText password;
    @BindView(R.id.loginSubmitButton)
    Button loginSubmitButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @Override
    public void onResume() {
        super.onResume();
        userName.setText("amolmoghe");
        password.setText("pratham@123");
    }

    @OnClick(R.id.loginSubmitButton)
    public void onSubmit() {


        String user = userName.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (user.equals("admin") && pass.equals("admin")) {
            AserSampleUtility.showFragment(getActivity(), new PullCRl(), PullCRl.class.getSimpleName());
        } else {
            // assign push logic
            CRL loggedCrl = databaseInstance.getCRLdao().checkUserValidation(user, pass);
            if (loggedCrl != null) {
                AserSample_Constant.setCrlID(loggedCrl.getCRLId() + "_" + loggedCrl.getUserName());
                String id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                AserSample_Constant.setDeviceID(id);
                getCrlsData();
            } else {
                //userNAme and password may be wrong
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Invalid Credentials");
                alertDialog.setIcon(R.drawable.ic_error_outline_black_24dp);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        userName.setText("");
                        password.setText("");
                        userName.requestFocus();
                    }
                });
                alertDialog.show();
            }
        }
    }

    public void getCrlsData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        AserSampleUtility.showProgressDialog(progressDialog, "Authenticating...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final HashMap map = new HashMap<>();

        db.collection("Admins")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            AserSampleUtility.dismissProgressDialog(progressDialog);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                map.put(document.getId(), document.getData());
                            }

                            if (map.isEmpty()) {
                                // Data unavailable on server
                                AserSampleUtility.showProblemAlert("Problem in getting data for crls. Please contact the administrator!", getActivity());
                            } else {
                                // update question data in DB
                                HashMap CRL_IDS = ((HashMap) map.get("CRL_IDS"));
                                ArrayList<String> MH_Ids = (ArrayList<String>) CRL_IDS.get("CRLs");
                                if (MH_Ids.contains(userName.getText().toString()))
                                    AserSampleUtility.setTestOrValidationDialog(getActivity());
                                else
                                    AserSampleUtility.showFragment(getActivity(), new SelectLanguageFragment(), SelectLanguageFragment.class.getSimpleName());
                            }
                        } else {
                            AserSampleUtility.dismissProgressDialog(progressDialog);
                            AserSampleUtility.showProblemAlert("Problem in getting data for crls. Please contact the administrator!", getActivity());
                        }
                    }
                });
    }
}
