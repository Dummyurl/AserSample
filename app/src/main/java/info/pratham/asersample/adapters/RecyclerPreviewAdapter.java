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
import info.pratham.asersample.database.modalClasses.SingleQustion;
import info.pratham.asersample.interfaces.RefreshRecycler;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

public class RecyclerPreviewAdapter extends RecyclerView.Adapter<RecyclerPreviewAdapter.MyViewHolder> {
    Context context;
    List<SingleQustioNew> questioList;

    public RecyclerPreviewAdapter(Context context, List questioList) {
        this.context = context;
        this.questioList = questioList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.preview_item_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler);
        holder.itemView.startAnimation(animation);
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);
        if (questioList.get(position).isCorrect()) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_green));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.question.setText("QUE : " + questioList.get(position).getQue_text());
        holder.questionid.setText("ID : " + questioList.get(position).getQue_id());
        if (questioList.get(position).getAnswer() != null && !questioList.get(position).getAnswer().isEmpty()) {
            holder.answer.setText("ANS : " + questioList.get(position).getAnswer());
        }
        holder.start_time.setText("START : " + questioList.get(position).getStartTime());
        holder.end_time.setText("END : " + questioList.get(position).getEndTime());

    }


    @Override
    public int getItemCount() {
        return questioList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView question;
        TextView questionid;
        TextView answer;
        TextView start_time;
        TextView end_time;

        public MyViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            questionid = itemView.findViewById(R.id.questionid);
            answer = itemView.findViewById(R.id.answer);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);
        }
    }
}
