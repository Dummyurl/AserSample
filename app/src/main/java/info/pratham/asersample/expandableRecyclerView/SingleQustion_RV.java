package info.pratham.asersample.expandableRecyclerView;

/**
 * Created by PEF on 15/12/2018.
 */

public class SingleQustion_RV extends Entity {
    private int que_seq_cnt;
    private String que_id;
    private String que_text;
    private String recordingName;
    private String answer;


    private String parentID;

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    @Override
    public boolean isParent() {
        return false;
    }


    public int getQue_seq_cnt() {
        return que_seq_cnt;
    }

    public void setQue_seq_cnt(int que_seq_cnt) {
        this.que_seq_cnt = que_seq_cnt;
    }

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }

    public String getRecordingName() {
        return recordingName;
    }

    public void setRecordingName(String recordingName) {
        this.recordingName = recordingName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQue_text() {
        return que_text;
    }

    public void setQue_text(String que_text) {
        this.que_text = que_text;
    }
}
