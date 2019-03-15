package info.pratham.asersample.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.pratham.asersample.R;
import info.pratham.asersample.animation.EnlaegeView;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.database.modalClasses.SingleQustioNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.interfaces.RefreshRecycler;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

public class RecyclerViewCheckAnswerAdapter extends RecyclerView.Adapter<RecyclerViewCheckAnswerAdapter.MyViewHolder> implements RefreshRecycler {
    Context context;
    List<QuestionStructure> questioList;
    String level;

    public RecyclerViewCheckAnswerAdapter(Context context, List questioList, String level) {
        this.context = context;
        this.questioList = questioList;
        this.level = level;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (level.equals("Para") || level.equals("Story") || level.equals("Sentence"))
            view = LayoutInflater.from(context).inflate(R.layout.recycler_vertical_item, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        QuestionStructure questionStructure = questioList.get(position);
        holder.textView.setText(questionStructure.toString());
        if (questionStructure.isSelected()) {
            holder.isSelected_img.setVisibility(View.VISIBLE);

            Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
            List<SingleQustioNew> temp = studentNew.getSequenceList();
            //if question is old then remove old entry
            boolean flag = false;
            String answer = null;
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                    answer = temp.get(i).getAnswer();
                    flag = true;
                    break;
                }
            }
            if (flag) {
                if (answer != null && !answer.isEmpty()) {
                    holder.answer.setVisibility(View.VISIBLE);
                    holder.answer.setText("ans: " + answer);
                } else {
                    holder.answer.setVisibility(View.GONE);
                }
            } else {
                holder.answer.setVisibility(View.GONE);
            }

        } else {
            holder.isSelected_img.setVisibility(View.GONE);
        }

        if (questioList.get(position).isCorrect()) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_green));
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.delete.setVisibility(View.GONE);
        }
        holder.textView.setTextColor(context.getResources().getColor(R.color.black));
        //delete item

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.delete.setVisibility(View.GONE);
                QuestionStructure questionStructure = questioList.get(position);
                questionStructure.setCorrect(false);
                Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
                List<SingleQustioNew> temp = studentNew.getSequenceList();
                for (int i = 0; i < temp.size(); i++) {
                    if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                        temp.get(i).setCorrect(false);
                        break;
                    }
                }
            }
        });

        if (questionStructure.isSelected()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_green));
                    holder.delete.setVisibility(View.VISIBLE);
                    QuestionStructure questionStructure = questioList.get(position);
                    questionStructure.setCorrect(true);

                    //set correct to final list
                    Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
                    List<SingleQustioNew> temp = studentNew.getSequenceList();
                    for (int i = 0; i < temp.size(); i++) {
                        if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                            temp.get(i).setCorrect(true);
                            break;
                        }
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return questioList.size();
    }

    @Override
    public void refreshRecycler() {
        this.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView answer;
        ImageView delete;
        ImageView isSelected_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            delete = itemView.findViewById(R.id.delete);
            isSelected_img = itemView.findViewById(R.id.isSelected);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}
