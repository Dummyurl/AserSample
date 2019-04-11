package info.pratham.asersample.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.CRL;
import info.pratham.asersample.interfaces.QuestionDataCompleteListener;
import info.pratham.asersample.networkManager.NetworkManager;
import info.pratham.asersample.utility.AserSampleUtility;

import static info.pratham.asersample.utility.AserSampleUtility.showProblemAlert;

/**
 * Created by PEF on 27/11/2018.
 */

public class PullCRl extends BaseFragment implements QuestionDataCompleteListener {
    private static final String TAG = "pullCRL";
    @BindView(R.id.stateSpinner)
    Spinner stateSpinner;
    String[] statesCodes;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pull_crl_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        statesCodes = getResources().getStringArray(R.array.india_states_shortcode);
        progressDialog = new ProgressDialog(getActivity());
        loadSpinner();
    }

    private void loadSpinner() {
        String states[] = getResources().getStringArray(R.array.india_states);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, states);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(arrayAdapter);
    }

    @OnClick(R.id.btn_pull)
    public void pullData() {
        if (stateSpinner.getSelectedItemPosition() > 0) {
            final NetworkManager networkManager = new NetworkManager(getActivity(), this);
            AserSampleUtility.showProgressDialog(progressDialog);
            networkManager.getQuestionData(progressDialog);
        } else {
            showToast("Please select a state");
        }
    }

    public void pullCRL() {
        AserSampleUtility.showProgressDialog(progressDialog);
        String URL = "http://www.swap.prathamcms.org/api/UserList?programId=1&statecode=" + statesCodes[stateSpinner.getSelectedItemPosition()];
        AndroidNetworking.get(URL)
                .build()
                .getAsObjectList(CRL.class, new ParsedRequestListener<List<CRL>>() {
                    @Override
                    public void onResponse(List<CRL> crlsList) {
                        Log.d(TAG, "userList size : " + crlsList.size());
                        databaseInstance.getCRLdao().insertCrl(crlsList);
                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        AserSampleUtility.showFragment(getActivity(), new LoginFragment(), LoginFragment.class.getSimpleName());
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        showProblemAlert("Problem in getting CRL data!", getActivity());
//                        AserSampleUtility.showToast(getActivity(), "NO Internet connection");
                    }
                });

    }

   /* @Override
    public void onPause() {
        super.onPause();
        AserSampleUtility.dismissProgressDialog(progressDialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AserSampleUtility.dismissProgressDialog(progressDialog);
    }*/

    @Override
    public void startPushingCrl() {
        pullCRL();
    }
}
