package info.pratham.asersample.networkManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.pratham.asersample.R;

/**
 * Created by PEF on 14/12/2018.
 */

public class ListViewAdapter extends BaseAdapter {
    Context context;
    List data;
    LayoutInflater inflater;


    public ListViewAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_item, null);
        }
        TextView textView = convertView.findViewById(R.id.tittle);
        ImageView imageView = convertView.findViewById(R.id.icon);
        textView.setText(data.get(position).toString());
       /* imageView.setImageResource(R.drawable.arrowdown);*/
        return convertView;
    }
}
