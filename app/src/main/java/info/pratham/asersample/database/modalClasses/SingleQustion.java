package info.pratham.asersample.database.modalClasses;

/**
 * Created by PEF on 15/12/2018.
 */

public class SingleQustion {
    private int que_seq_cnt;
    private String que_id;
    private String recordingName;

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
}