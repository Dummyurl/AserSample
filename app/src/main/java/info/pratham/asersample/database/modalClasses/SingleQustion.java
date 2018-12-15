package info.pratham.asersample.database.modalClasses;

import java.io.Serializable;

/**
 * Created by PEF on 15/12/2018.
 */

public class SingleQustion  implements Serializable {
    private int que_seq_cnt;
    private String que_id;
    private String recordingName;
    private String answer;

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
}
