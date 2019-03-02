package info.pratham.asersample.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.QuestionStructure;

public class RecyclerVerticalAdapter extends RecyclerView.Adapter<RecyclerVerticalAdapter.MyViewHolder> {
    Context context;
    List<QuestionStructure> questioList;

    public RecyclerVerticalAdapter(Context context, List questioList) {
        this.context = context;
        this.questioList = questioList;
    }

    @NonNull
    @Override
    public RecyclerVerticalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_vertical_item, parent, false);
        return new RecyclerVerticalAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerVerticalAdapter.MyViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.textView.setText(Html.fromHtml(questioList.get(position).toString(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.textView.setText(Html.fromHtml(questioList.get(position).toString()));
        }
    }

    @Override
    public int getItemCount() {
        return questioList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
