package info.pratham.asersample.expandableRecyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import info.pratham.asersample.R;
import info.pratham.asersample.utility.AserSample_Constant;

/**
 * Created by Hamza Fetuga on 5/29/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CHILD = 0;
    public static final int PARENT = 1;
    static List<QueLevel_RV> datas = new ArrayList<>();
    static List<Integer> children = new ArrayList<>();
    static RecyclerAdapter recyclerAdapter;
    public RecyclerView recyclerView;
    static List<Entity> general;

    public RecyclerAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerAdapter = this;
        String studentJson = getJson();
        this.datas = ToList(studentJson);
        general = new ArrayList<>();
        setParent();
        Log.d("number", datas.size() + "");
    }

    private String getJson() {
        Gson gson = new Gson();
        String studentJson = gson.toJson(AserSample_Constant.getAserSample_Constant().getStudent().getSequenceList());
        return studentJson;
    }


    public void setParent() {
        for (int x = 0; x < datas.size(); x++) {
            QueLevel_RV mQueLevel = datas.get(x);
            general.add(mQueLevel);
            children.add(0);
            mQueLevel.setChildrenVisible(false);
            List<SingleQustion_RV> singleQustion_rvs = mQueLevel.getQuestions();
            for (int y = 0; y < singleQustion_rvs.size(); y++) {
                SingleQustion_RV s = singleQustion_rvs.get(y);
                s.setParentID(String.valueOf(x));
            }
        }
    }

    public List<QueLevel_RV> ToList(String dataAsJSON) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<QueLevel_RV>>() {
        }.getType();
        return gson.fromJson(dataAsJSON, listType);
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView seq_cnt;
        public TextView mistake_cnt;
        public TextView subject;
        public TextView level;

        public ParentViewHolder(View itemView) {
            super(itemView);

            seq_cnt = (TextView) itemView.findViewById(R.id.seqCnt);
            mistake_cnt = (TextView) itemView.findViewById(R.id.mistak_cnt);
            subject = (TextView) itemView.findViewById(R.id.subject);
            level = (TextView) itemView.findViewById(R.id.level);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int id = getLayoutPosition();
            int aid = getAdapterPosition();

            Log.d("positions", id + " " + aid);

            QueLevel_RV person = (QueLevel_RV) general.get(id);

            if (person.getQuestions().size() != 0) {

                //collapse list
                if (person.isChildrenVisible()) {
                    person.setChildrenVisible(false);
                    List<SingleQustion_RV> singleQustion_rv = person.getQuestions();
                    for (int i = id + 1; i < (id + 1 + singleQustion_rv.size()); i++) {
                        general.remove(id + 1);
                    }
                    Log.d("general size is ", general.size() + "");

                    recyclerAdapter.notifyItemRangeRemoved(id + 1, singleQustion_rv.size());
                }
                //expand list
                else {
                    person.setChildrenVisible(true);
                    List<SingleQustion_RV> singleQustion_rv = person.getQuestions();
                    int index = 0;

                    for (int i = id + 1; i < (id + 1 + singleQustion_rv.size()); i++) {
                        general.add(i, singleQustion_rv.get(index));
                        index++;
                    }


                    recyclerAdapter.notifyItemRangeInserted(id + 1, singleQustion_rv.size());
                }

                //fix this
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerAdapter.recyclerView.
                        getLayoutManager()).findLastCompletelyVisibleItemPosition();

                if ((id + 1) < general.size()) {
                    if ((id + 1) > lastVisibleItemPosition) {
                        recyclerAdapter.recyclerView.scrollToPosition(id + 1);
                    }
                }


            }
        }
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView queText;
        public TextView qid;
        public TextView q_cnt;

        public ChildViewHolder(View itemView) {
            super(itemView);

            queText = (TextView) itemView.findViewById(R.id.queText);
            qid = (TextView) itemView.findViewById(R.id.qid);
            q_cnt = (TextView) itemView.findViewById(R.id.q_cnt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           /* int id = 0;
            int pid = 0;
            if (this.viewID == null) {
                TextView id_text = (TextView) view.findViewById(R.id.fid);
                TextView pid_text = (TextView) view.findViewById(R.id.pid);
                id = Integer.parseInt(id_text.getText().toString().trim());
                pid = Integer.parseInt(pid_text.getText().toString().trim());
            } else {
                id = Integer.parseInt(this.viewID.getText().toString());
                pid = Integer.parseInt(this.parentID.getText().toString());
            }

            Log.d("child id clicked", id + "");
            Log.d("child pid clicked", pid + "");

            Toast.makeText(view.getContext(), "" + getLayoutPosition(), Toast.LENGTH_SHORT).show();*/
        }
    }

    @Override
    public int getItemViewType(int position) {

        //int parentSize = datas.size();
//        int size = datas.size();
//        int upperBound = 0;
//        int lowerBound = 0;
//        for (int x = 0; x < size; x++) {
//            Person p = datas.get(x);
//            upperBound += p.getFriends().length + 1;
//
//            if (position == lowerBound) {
//                return PARENT;
//            }
//
//            if (position < upperBound) {
//                return CHILD;
//            }
//
//            lowerBound = upperBound;
//        }
//        return 0;

        if (general.get(position).isParent()) {
            Log.d("viewtype = ", "parent at " + position);
            return PARENT;
        } else {
            Log.d("viewtype = ", "child at " + position);
            return CHILD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == CHILD) {
            // Inflate the custom layout
            View itemView = inflater.inflate(R.layout.child_item, parent, false);

            // Return a new holder instance
            return new ChildViewHolder(itemView);
        } else {
            // Inflate the custom layout
            View itemView = inflater.inflate(R.layout.list_item, parent, false);

            // Return a new holder instance
            return new ParentViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == CHILD) {
           /* TextView viewID = ((ChildViewHolder) holder).viewID;
            TextView parentID = ((ChildViewHolder) holder).parentID;
            TextView text = ((ChildViewHolder) holder).text;*/

            SingleQustion_RV singleQustion_rv = (SingleQustion_RV) general.get(position);

            ((ChildViewHolder) holder).queText.setText(singleQustion_rv.getQue_text());
            ((ChildViewHolder) holder).qid.setText(singleQustion_rv.getQue_id());
            ((ChildViewHolder) holder).q_cnt.setText(""+singleQustion_rv.getQue_seq_cnt());
        } else {
            QueLevel_RV queLevel_rv = (QueLevel_RV) general.get(position);
            ((ParentViewHolder) holder).seq_cnt.setText("Seqeunce count : " + queLevel_rv.getLevel_seq_cnt());
            ((ParentViewHolder) holder).mistake_cnt.setText("Mistakes : " + queLevel_rv.getMistakes());
            ((ParentViewHolder) holder).level.setText("Level : " + queLevel_rv.getLevel());
            ((ParentViewHolder) holder).subject.setText("Subject : " + queLevel_rv.getSubject());
        }

        //Person person = datas.get(position);

    }

    @Override
    public int getItemCount() {
        Log.d("general size = ", general.size() + "");
        return general.size();
    }


}
