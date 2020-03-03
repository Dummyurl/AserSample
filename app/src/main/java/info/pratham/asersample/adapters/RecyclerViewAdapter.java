package info.pratham.asersample.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
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

import java.io.File;
import java.util.Iterator;
import java.util.List;

import info.pratham.asersample.ASERApplication;
import info.pratham.asersample.R;
import info.pratham.asersample.animation.EnlargeView;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.database.modalClasses.SingleQuestionNew;
import info.pratham.asersample.interfaces.RecordPrepairListner;
import info.pratham.asersample.interfaces.RefreshRecycler;
import info.pratham.asersample.utility.AserSample_Constant;
import info.pratham.asersample.utility.AudioUtil;
import info.pratham.asersample.utility.ListConstant;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements RefreshRecycler, RecordPrepairListner {
    Context context;
    List<QuestionStructure> questioList;
    String level;
    EnlargeView enlargeView;

    public RecyclerViewAdapter(Context context, List questioList, String level) {
        this.context = context;
        this.questioList = questioList;
        this.level = level;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler);
        holder.itemView.startAnimation(animation);


        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);
        holder.textView.setText(questioList.get(position).toString());
        if (questioList.get(position).isSelected()) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.blueLight));
            holder.delete.setVisibility(View.VISIBLE);
            if (level.equals("Subtraction") || level.equals("Division")) {
                holder.audioControll.setVisibility(View.GONE);
            } else {
                holder.audioControll.setVisibility(View.VISIBLE);
            }
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.delete.setVisibility(View.GONE);
            holder.audioControll.setVisibility(View.GONE);
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
                                    case "Words":
                                        ListConstant.Words_cnt--;
                                        break;
                                    case "Letters":
                                        ListConstant.Letters_cnt--;
                                        break;
                                    case "Capital":
                                        ListConstant.Capital_cnt--;
                                        break;
                                    case "Small":
                                        ListConstant.Small_cnt--;
                                        break;
                                    case "word":
                                        ListConstant.engWord_cnt--;
                                        break;
                                    //MATHEMATICS
                                    case "Single":
                                        ListConstant.Single_cnt--;
                                        break;
                                    case "Double":
                                        ListConstant.Double_cnt--;
                                        break;
                                    case "Subtraction":
                                        ListConstant.Subtraction_cnt--;
                                        break;
                                    case "Division":
                                        ListConstant.Division_cnt--;
                                        break;
                                }
                                questionStructure.setSelected(false);
                                questionStructure.setIsCorrect(AserSample_Constant.NOTATTEMPED);
                                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
                                holder.delete.setVisibility(View.GONE);
                                holder.audioControll.setVisibility(View.GONE);

                                //delete recording file
                                String currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                                        AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/" + questionStructure.getId() + ".mp3";
                                File file = new File(currentFilePath);
                                if (file.exists()) {
                                    file.delete();
                                }
                                List<SingleQuestionNew> temp = AserSample_Constant.getAserSample_Constant().getStudent().getSequenceList();
                                /*for (int i = 0; i < temp.size(); i++) {
                                    if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                                        temp.remove(i);
                                        break;
                                    }
                                }*/
                                // Iterator to traverse the list
                                Iterator iterator = temp.iterator();
                                while (iterator.hasNext()) {
                                    SingleQuestionNew singleQuestionNew = (SingleQuestionNew) iterator.next();
                                    if (singleQuestionNew.getQue_id().equals(questionStructure.getId())) {
                                        iterator.remove();
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
        holder.audioControll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionStructure questionStructure = questioList.get(holder.getAdapterPosition());
                String currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                        AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/" + questionStructure.getId() + ".mp3";
                AudioUtil.playRecording(currentFilePath, holder.audioControll, context);

                if (!questionStructure.isPlaying()) {
                    questionStructure.setPlaying(true);
                    AudioUtil.playRecording(currentFilePath, holder.audioControll, context);
                } else {
                    questionStructure.setPlaying(false);
                    AudioUtil.stopPlayingAudio();
                }
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
                                        case "Words":
                                            if (isAttempted || ListConstant.Words_cnt < ListConstant.FIVE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "Letters":
                                            if (isAttempted || ListConstant.Letters_cnt < ListConstant.FIVE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "Capital":
                                            if (isAttempted || ListConstant.Capital_cnt < ListConstant.FIVE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "Small":
                                            if (isAttempted || ListConstant.Small_cnt < ListConstant.FIVE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "word":
                                            if (isAttempted || ListConstant.engWord_cnt < ListConstant.FIVE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        //MATHEMATICS
                                        case "Single":
                                            if (isAttempted || ListConstant.Single_cnt < ListConstant.FIVE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "Double":
                                            if (isAttempted || ListConstant.Double_cnt < ListConstant.FIVE) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "Subtraction":
                                            if (isAttempted || ListConstant.Subtraction_cnt < ListConstant.TWO) {
                                                openEnlaegeView(holder, questionStructure);
                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case "Division":
                                            if (isAttempted || ListConstant.Division_cnt < ListConstant.ONE) {
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
                        case "Words":
                            if (isAttempted || ListConstant.Words_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Letters":
                            if (isAttempted || ListConstant.Letters_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Capital":
                            if (isAttempted || ListConstant.Capital_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Small":
                            if (isAttempted || ListConstant.Small_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "word":
                            if (isAttempted || ListConstant.engWord_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        //MATHEMATICS
                        case "Single":
                            if (isAttempted || ListConstant.Single_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Double":
                            if (isAttempted || ListConstant.Double_cnt < ListConstant.FIVE) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Subtraction":
                            if (isAttempted || ListConstant.Subtraction_cnt < ListConstant.TWO) {
                                openEnlaegeView(holder, questionStructure);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.Upper_Limit_reached), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Division":
                            if (isAttempted || ListConstant.Division_cnt < ListConstant.ONE) {
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

    public void openEnlaegeView(MyViewHolder holder, QuestionStructure questionStructure) {
        boolean isAttemptedQue = false;
        if (questionStructure.isSelected()) {
            isAttemptedQue = true;
        }
        String currentFilePath = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" +
                AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";
        File file = new File(currentFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        if (level.equals("Subtraction") || level.equals("Division")) {
            enlargeView = new EnlargeView(context, questionStructure, level, isAttemptedQue, RecyclerViewAdapter.this);
            enlargeView.show();
        } else {
            AudioUtil.startRecording(this, currentFilePath +/* recordingIndex + "_" + AserSample_Constant.getAserSample_Constant().getStudent().getId() + "_" +*/ questionStructure.getId() + ".mp3", context, questionStructure, level, isAttemptedQue, this);
        }
        questionStructure.setSelected(true);
        holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.blueLight));
        holder.delete.setVisibility(View.VISIBLE);
        if (level.equals("Subtraction") || level.equals("Division")) {
            holder.audioControll.setVisibility(View.GONE);
        } else {
            holder.audioControll.setVisibility(View.VISIBLE);
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

    @Override
    public void onRecordingStarted(@NonNull final Context context, final QuestionStructure questionStructure, final String level, final boolean isAttemptedQue, RecyclerView.Adapter recyclerViewAdapter) {
       /* enlargeView = new EnlargeView(context, questionStructure, level, isAttemptedQue, this);
        enlargeView.show();*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enlargeView = new EnlargeView(context, questionStructure, level, isAttemptedQue, RecyclerViewAdapter.this);
                enlargeView.show();
            }
        }, 100);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView delete;
        ImageView audioControll;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            delete = itemView.findViewById(R.id.delete);
            audioControll = itemView.findViewById(R.id.audioControll);
        }
    }

    public void closeEnlargeView() {
        if (enlargeView != null) {
            if (enlargeView.isShowing()) {
                enlargeView.closeBtb();
            }
        }
    }
}
