package info.pratham.asersample.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import info.pratham.asersample.R;
import info.pratham.asersample.activities.UpdateActivity;
import info.pratham.asersample.activities.ValidateActivity;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.utility.AserSample_Constant;

public class StudentsRecyclerAdapter extends RecyclerView.Adapter<StudentsRecyclerAdapter.RecyclerViewHolder> {

    private final Activity context;
    private final ArrayList<Student> students;

    public StudentsRecyclerAdapter(Activity context, ArrayList<Student> students) {
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.students_adapter_view, parent, false);
        return new RecyclerViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
        final String validatedDate = students.get(position).getValidatedDate();
        if (validatedDate != null && !validatedDate.isEmpty()) {
            holder.completed.setVisibility(View.VISIBLE);
            holder.completed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Snackbar.make(completed, "Updated on : " +
//                            deviceID.substring(deviceID.indexOf(":") + 1) +
//                            " by " + AserSample_Constant.getCrlID(), Snackbar.LENGTH_LONG).show();
                    Snackbar.make(holder.completed, "Updated on : " + validatedDate +
                            " by " + AserSample_Constant.getCrlID(), Snackbar.LENGTH_LONG).show();
                }
            });
        } else holder.completed.setVisibility(View.GONE);

        String sName = students.get(position).getName();
        if (sName.isEmpty())
            holder.titleText.setText(students.get(position).getId());
        else
            holder.titleText.setText(students.get(position).getName());

//        todo uncomment this line to display student name and uncomment above 5 lines
//        titleText.setText(students.get(position).getName());
        holder.subtitleText.setText(students.get(position).getFather());

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateIntent = new Intent(context, UpdateActivity.class);
                updateIntent.putExtra("STUDENT_SEQUENCE", students.get(holder.getAdapterPosition()));
                updateIntent.putExtra("crl_id", ValidateActivity.currentCrl);
                Log.d("currentCRL**", "onClick: " + ValidateActivity.currentCrl);
                context.startActivity(updateIntent);
            }
        });
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student item = students.get(holder.getAdapterPosition());
                Snackbar.make(holder.info, item.getName()
                        + " in class : " + item.getStudClass()
                        + " from village : " + item.getVillage(), Snackbar.LENGTH_LONG).show();
                /*
                Toast.makeText(context, item.getName()
                        + " in class : " + item.getStudClass()
                        + " from village : " +item.getVillage() , Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView subtitleText;
        final ImageView info;
        final ImageView completed;
        LinearLayout rowView;

        public RecyclerViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.title);
            subtitleText = view.findViewById(R.id.subtitle);
            info = view.findViewById(R.id.info);
            completed = view.findViewById(R.id.completed);
            rowView = view.findViewById(R.id.rowLL);
        }

    }
}