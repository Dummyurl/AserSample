package info.pratham.asersample.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import info.pratham.asersample.R;
import info.pratham.asersample.activities.ValidateActivity;

import static info.pratham.asersample.activities.ValidateActivity.currentCrl;

public class CRLsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> crls;

    public CRLsAdapter(Activity context, ArrayList<String> crls) {
        super(context, R.layout.crls_adapter_view, crls);
        this.context = context;
        this.crls = crls;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.crls_adapter_view, null, true);

        TextView titleText = rowView.findViewById(R.id.crl_name);
        TextView subtitleText = rowView.findViewById(R.id.crl_id);
        final ImageView delete = rowView.findViewById(R.id.ic_delete);

        String crl = crls.get(position);
        String crl_id = crl.split("_")[0];
        String crl_name = crl.substring(crl.indexOf("_") + 1);

        // show delete icon if data exists of CRL
        if (context instanceof ValidateActivity) {
            if (((ValidateActivity) context).checkIfDataExist(crl))
                delete.setVisibility(View.VISIBLE);
            else delete.setVisibility(View.GONE);
        }

        //TODO uncomment these 2 lines to show CRL name and ID and comment below switch and line immediate below it
//        titleText.setText(crl_name);
//        subtitleText.setText("ID : " + crl_id);

        switch (crl_name) {
            case "aniljadhav305" :
                titleText.setText("Marathi_1");
                break;
            case "mh_prajeetkumar" :
                titleText.setText("Hindi_1");
                break;
            case "up_sanjeet" :
                titleText.setText("Hindi_2");
                break;
        }
        subtitleText.setText("");

        if (crl.equals(currentCrl))
            rowView.setBackgroundColor(context.getResources().getColor(R.color.light_blue));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ValidateActivity) {
                    ((ValidateActivity) context).setStudentDataChanges(crls.get(position));
                    notifyDataSetChanged();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ValidateActivity) {
                    ((ValidateActivity) context).deleteDataForCRL(crls.get(position));
                }
            }
        });
        return rowView;
    }
}