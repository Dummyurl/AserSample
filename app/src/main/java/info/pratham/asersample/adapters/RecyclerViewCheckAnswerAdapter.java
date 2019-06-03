package info.pratham.asersample.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.database.modalClasses.SingleQustioNew;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.interfaces.RefreshRecycler;
import info.pratham.asersample.utility.AserSample_Constant;

public class RecyclerViewCheckAnswerAdapter extends RecyclerView.Adapter<RecyclerViewCheckAnswerAdapter.MyViewHolder> implements RefreshRecycler {
    Context context;
    List<QuestionStructure> qustionList;
    //List<QuestionStructure> qustionList;
    String level;

    public RecyclerViewCheckAnswerAdapter(Context context, List<QuestionStructure> qustionList, String level) {
        this.context = context;
        //  this.qustionList = qustionList;
        this.qustionList = qustionList;
        this.level = level;
    }

   /* @Override
    public int getItemViewType(int position) {
        QuestionStructure questionStructure = qustionList.get(position);
        if (questionStructure.isSelected()) {
            return 1;
        } else {
            return 0;
        }
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        /*     if (viewType == 1) {*/
        if (level.equals("Para") || level.equals("Story") || level.equals("Sentence"))
            view = LayoutInflater.from(context).inflate(R.layout.recycler_vertical_checking, parent, false);
        else
            view = LayoutInflater.from(context).inflate(R.layout.recycler_item_checking, parent, false);
       /* } else {
            view = LayoutInflater.from(context).inflate(R.layout.blank_item, parent, false);
        }*/
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        QuestionStructure questionStructure = qustionList.get(position);
        holder.textView.setText(questionStructure.toString());
        if (questionStructure.isSelected()) {
            //    holder.isSelected_img.setVisibility(View.VISIBLE);
            holder.checkingButtons.setVisibility(View.VISIBLE);
            Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
            List<SingleQustioNew> temp = studentNew.getSequenceList();
            //if question is old then remove old entry
            boolean flag = false;
            String answer = null;
            String noOfMistakes = null;
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                    answer = temp.get(i).getAnswer();
                    noOfMistakes = temp.get(i).getNoOfMistakes();
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
            if (level.equals("Para") || level.equals("Story") || level.equals("Sentence")) {
                if (noOfMistakes != null)
                    holder.numberOfMistakes.setText(noOfMistakes);
            }
        } else {
            //   holder.isSelected_img.setVisibility(View.GONE);
            holder.checkingButtons.setVisibility(View.GONE);
        }
        if (qustionList.get(position).getIsCorrect().equals(AserSample_Constant.CORRECT)) {
            holder.correct.setBackground(context.getResources().getDrawable(R.drawable.roundedcorner_green));
            holder.wrong.setBackgroundColor(context.getResources().getColor(R.color.white));
            // holder.delete.setVisibility(View.VISIBLE);
        } else if (qustionList.get(position).getIsCorrect().equals(AserSample_Constant.WRONG)) {
            holder.correct.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.wrong.setBackground(context.getResources().getDrawable(R.drawable.roundedcorner_red));
            // holder.delete.setVisibility(View.GONE);
        } else {
            holder.correct.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.wrong.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.textView.setTextColor(context.getResources().getColor(R.color.black));
        if (level.equals("Para") || level.equals("Story") || level.equals("Sentence")) {
            holder.numberOfMistakes.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Toast.makeText(context, "before text change", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Toast.makeText(context, "onTextChanged text change", Toast.LENGTH_LONG).show();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    setNoOfMistakes(position, s.toString());
                }
            });
        }

        //delete item
        holder.wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level.equals("Para") || level.equals("Story") || level.equals("Sentence")) {
                    String noOfMistakes = holder.numberOfMistakes.getText().toString().trim();
                    if (!noOfMistakes.isEmpty()) {
                        setAns(holder, position, false);
                    } else {
                        Toast.makeText(context, "Enter a number of mistakes", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setAns(holder, position, false);
                }
            }
        });

        if (questionStructure.isSelected()) {
            holder.correct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (level.equals("Para") || level.equals("Story") || level.equals("Sentence")) {
                        String noOfMistakes = holder.numberOfMistakes.getText().toString().trim();
                        if (!noOfMistakes.isEmpty()) {
                            setAns(holder, position, true);
                        } else {
                            Toast.makeText(context, "Enter a number of mistakes", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        setAns(holder, position, true);
                    }
                }
            });
        }
    }

    private void setNoOfMistakes(int position, String ans) {
        //Toast.makeText(context, "" + ans, Toast.LENGTH_SHORT).show();
        QuestionStructure questionStructure = qustionList.get(position);
        questionStructure.setNoOfMistakes(ans);
        Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
        List<SingleQustioNew> temp = studentNew.getSequenceList();
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                temp.get(i).setNoOfMistakes(ans);
                break;
            }
        }
    }

    private void setAns(MyViewHolder holder, int position, boolean ans) {
        //setWrong to list
        if (!ans) {
            holder.correct.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.wrong.setBackground(context.getResources().getDrawable(R.drawable.roundedcorner_red));
            //holder.delete.setVisibility(View.GONE);
            QuestionStructure questionStructure = qustionList.get(position);
            questionStructure.setIsCorrect(AserSample_Constant.WRONG);
            Student studentNew = AserSample_Constant.getAserSample_Constant().getStudent();
            List<SingleQustioNew> temp = studentNew.getSequenceList();
            for (int i = 0; i < temp.size(); i++) {
                if (temp.get(i).getQue_id().equals(questionStructure.getId())) {
                    temp.get(i).setCorrect(false);
                    break;
                }
            }
        } else {
            holder.correct.setBackground(context.getResources().getDrawable(R.drawable.roundedcorner_green));
            holder.wrong.setBackgroundColor(context.getResources().getColor(R.color.white));
            // holder.delete.setVisibility(View.VISIBLE);
            QuestionStructure questionStructure = qustionList.get(position);
            questionStructure.setIsCorrect(AserSample_Constant.CORRECT);

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
    }


    @Override
    public int getItemCount() {
        return qustionList.size();
    }

    @Override
    public void refreshRecycler() {
        this.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView answer;
        ImageView wrong;
        ImageView correct;
        // ImageView isSelected_img;
        LinearLayout checkingButtons;
        EditText numberOfMistakes;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            wrong = itemView.findViewById(R.id.wrong);
            correct = itemView.findViewById(R.id.correct);
            answer = itemView.findViewById(R.id.answer);
            //isSelected_img = itemView.findViewById(R.id.isSelected);
            checkingButtons = itemView.findViewById(R.id.checkingButtons);
            numberOfMistakes = itemView.findViewById(R.id.numberOfMistakes);
        }
    }
}
