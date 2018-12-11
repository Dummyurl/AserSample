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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONArray;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.CRL;
import info.pratham.asersample.networkManager.NetworkManager;
import info.pratham.asersample.utility.AserSampleUtility;

/**
 * Created by PEF on 27/11/2018.
 */

public class PullCRl extends BaseFragment {
    @BindView(R.id.stateSpinner)
    Spinner stateSpinner;
    private static final String TAG = "pullCRL";
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
        loadSpinner();
        progressDialog = new ProgressDialog(getActivity());
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
            NetworkManager networkManager = new NetworkManager(getActivity());
            networkManager.getQuestionData();
            pullCRL("http://www.swap.prathamcms.org/api/UserList?programId=1&statecode=" + statesCodes[stateSpinner.getSelectedItemPosition()]);
        } else {
            showToast(" Please select a state");
        }
    }

    private void pullCRL(String URL) {
        AserSampleUtility.showProgressDialog(progressDialog);
        AndroidNetworking.get(URL)
                .build()
                .getAsObjectList(CRL.class, new ParsedRequestListener<List<CRL>>() {
                    @Override
                    public void onResponse(List<CRL> crlsList) {
                        // do anything with response
                        Log.d(TAG, "userList size : " + crlsList.size());
                        databaseInstance.getCRLdao().insertCrl(crlsList);
                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        AserSampleUtility.showFragment(getActivity(), new LoginFragment(), LoginFragment.class.getSimpleName());
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error

                        AserSampleUtility.dismissProgressDialog(progressDialog);
                        AserSampleUtility.showToast(getActivity(), "NO Intenet connection");
                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();
        AserSampleUtility.dismissProgressDialog(progressDialog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AserSampleUtility.dismissProgressDialog(progressDialog);
    }
}
