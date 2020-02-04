package info.pratham.asersample.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import info.pratham.asersample.R;
import info.pratham.asersample.activities.UpdateActivity;
import info.pratham.asersample.activities.ValidateActivity;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSample_Constant;

public class StudentsAdapter extends ArrayAdapter<Student> {

    private final Activity context;
    private final ArrayList<Student> students;

    public StudentsAdapter(Activity context, ArrayList<Student> students) {
        super(context, R.layout.students_adapter_view, students);
        this.context = context;
        this.students = students;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.students_adapter_view, null, true);

        TextView titleText = rowView.findViewById(R.id.title);
        TextView subtitleText = rowView.findViewById(R.id.subtitle);
        final ImageView info = rowView.findViewById(R.id.info);
        final ImageView completed = rowView.findViewById(R.id.completed);

//        final String deviceID = students.get(position).getDeviceID();
//        final String splitted[] = deviceID.split(":");
//        Log.d("pk**", "deviceId: " + deviceID + ": Length " + splitted.length);
//        if (splitted.length > 1) {
//            Log.d("pk**", "splitted: " + splitted[0] + ":" + splitted[1]);
//            completed.setVisibility(View.VISIBLE);
//            completed.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(completed, "Updated on : " +
//                            deviceID.substring(deviceID.indexOf(":") + 1) +
//                            " by " + AserSample_Constant.getCrlID(), Snackbar.LENGTH_LONG).show();
//                }
//            });
//        } else completed.setVisibility(View.GONE);

        final String validatedDate = students.get(position).getValidatedDate();
        if (validatedDate != null && !validatedDate.isEmpty()) {
            completed.setVisibility(View.VISIBLE);
            completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Snackbar.make(completed, "Updated on : " +
//                            deviceID.substring(deviceID.indexOf(":") + 1) +
//                            " by " + AserSample_Constant.getCrlID(), Snackbar.LENGTH_LONG).show();
                    Snackbar.make(completed, "Updated on : " + validatedDate +
                            " by " + AserSample_Constant.getCrlID(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else completed.setVisibility(View.GONE);

        String sName = students.get(position).getName();
        if (sName.isEmpty())
            titleText.setText(students.get(position).getId());
        else
            titleText.setText(students.get(position).getName());

//        todo uncomment this line to display student name and uncomment above 5 lines
//        titleText.setText(students.get(position).getName());
        subtitleText.setText(students.get(position).getFather());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(context, UpdateActivity.class);
                updateIntent.putExtra("STUDENT_SEQUENCE", students.get(position));
                updateIntent.putExtra("crl_id", ValidateActivity.currentCrl);
                Log.d("currentCRL**", "onClick: " + ValidateActivity.currentCrl);
                context.startActivity(updateIntent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student item = students.get(position);
                Snackbar.make(info, item.getName()
                        + " in class : " + item.getStudClass()
                        + " from village : " + item.getVillage(), Snackbar.LENGTH_LONG).show();
                /*
                Toast.makeText(context, item.getName()
                        + " in class : " + item.getStudClass()
                        + " from village : " +item.getVillage() , Toast.LENGTH_SHORT).show();*/
            }
        });

        return rowView;

    }

    ;
}