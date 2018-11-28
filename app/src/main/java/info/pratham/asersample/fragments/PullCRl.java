package info.pratham.asersample.fragments;

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
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.BaseFragment;
import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.CRL;

/**
 * Created by PEF on 27/11/2018.
 */

public class PullCRl extends BaseFragment {
    @BindView(R.id.stateSpinner)
    Spinner stateSpinner;
    private static final String TAG = "pullCRL";
    String[] statesCodes;

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
            pullCRL("http://www.swap.prathamcms.org/api/UserList?programId=1&statecode=" + statesCodes[stateSpinner.getSelectedItemPosition()]);
        } else {
            showToast(" Select A state");
        }
    }

    private void pullCRL(String URL) {
        AndroidNetworking.get("http://www.swap.prathamcms.org/api/UserList?programId=1&statecode=MH")
                .build()
                .getAsObjectList(CRL.class, new ParsedRequestListener<List<CRL>>() {
                    @Override
                    public void onResponse(List<CRL> crlsList) {
                        // do anything with response
                        Log.d(TAG, "userList size : " + crlsList.size());
                      //  databaseInstance.getCRLdao().
                        //databaseInstance
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                    }
                });
    }

}
