package info.pratham.asersample.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import info.pratham.asersample.interfaces.RefreshRecycler;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.ListConstant;

public class RecyclerVerticalAdapter extends RecyclerView.Adapter<RecyclerVerticalAdapter.MyViewHolder> implements RefreshRecycler {
    Context context;
    List<QuestionStructure> questioList;
    String level;
    EnlaegeView enlaegeView;

    public RecyclerVerticalAdapter(Context context, List questioList, String level) {
        this.context = context;
        this.questioList = questioList;
        this.level = level;
    }

    @NonNull
    @Override
    public RecyclerVerticalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_vertical_item, parent, false);
        return new RecyclerVerticalAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerVerticalAdapter.MyViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.textView.setText(Html.fromHtml(questioList.get(position).toString(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.textView.setText(Html.fromHtml(questioList.get(position).toString()));
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler);
        holder.itemView.startAnimation(animation);


        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);
        holder.textView.setText(questioList.get(position).toString());
        if (questioList.get(position).isSelected()) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.blueLight));
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.delete.setVisibility(View.GONE);
        }
        holder.textView.setTextColor(context.getResources().getColor(R.color.black));
        //delete item
        holder.delete.setOnClickListener(new View.OnClickListener() {
            QuestionStructure questionStructure = questioList.get(holder.getAdapterPosition());

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("do you want delete it ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (level) {
                                    //NATIVE LANGUAGE
                                    case "Para":
                                        ListConstant.Para_cnt--;
                                        break;
                                    case "Story":
                                        ListConstant.Story_cnt--;
                                        break;
                                    case "Sentence":
                                        ListConstant.Sentence_cnt--;
                                        break;

                                }
                                questionStructure.setSelected(false);
                                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.delete.setVisibility(View.GONE);
                                List<SingleQustioNew> temp = AserSample_Constant.getAserSample_Constant().getStudent().getSequenceList();
                                for (int i = 0; i < temp.size(); i++) {
                                    if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                                        temp.remove(i);
                                        break;
                                    }
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            QuestionStructure questionStructure = questioList.get(holder.getAdapterPosition());

            @Override
            public void onClick(View v) {
                final boolean isAttempted = questionStructure.isSelected();
                if (isAttempted) {
                    new AlertDialog.Builder(context)
                            .setMessage("Do you want to replace it?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    switch (level) {
                                        //NATIVE LANGUAGE
                                        case "Para":
                                            if (isAttempted || ListConstant.Para_cnt < ListConstant.ONE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "Story":
                                            if (isAttempted || ListConstant.Story_cnt < ListConstant.ONE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        //English
                                        case "Sentence":
                                            if (isAttempted || ListConstant.Sentence_cnt < ListConstant.ONE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    switch (level) {
                        //NATIVE LANGUAGE
                        case "Para":
                            if (isAttempted || ListConstant.Para_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Story":
                            if (isAttempted || ListConstant.Story_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Sentence":
                            if (isAttempted || ListConstant.Sentence_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            }
        });
    }

    public void openEnlaegeView(RecyclerVerticalAdapter.MyViewHolder holder, QuestionStructure questionStructure) {
        boolean isAttemptedQue = false;
        if (questionStructure.isSelected()) {
            isAttemptedQue = true;
        }
        questionStructure.setSelected(true);
        holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.blueLight));
        holder.delete.setVisibility(View.VISIBLE);
        enlaegeView = new EnlaegeView(context, questionStructure, level, isAttemptedQue, this);
        enlaegeView.show();
    }

    @Override
    public int getItemCount() {
        return questioList.size();
    }

    @Override
    public void refreshRecycler() {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
