package info.pratham.asersample.database.modalClasses;

import java.io.Serializable;

/**
 * Created by PEF on 15/12/2018.
 */

public class SingleQuestionNewValidation implements Serializable {
    /* private int que_seq_cnt;*/
    private String que_id;
    private String que_text;
    private String answer;
    private String remainder;
    private String startTime;
    private String endTime;
    private boolean isCorrect;
    private String noOfMistakes;
    private String recordingName;
    private String noiseDescription;
    private String remarks;
    private String groundTruthDescription;

    public String getQue_id() {
        return que_id;
    }

    public void setQue_id(String que_id) {
        this.que_id = que_id;
    }

    public String getQue_text() {
        return que_text;
    }

    public void setQue_text(String que_text) {
        this.que_text = que_text;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getNoOfMistakes() {
        return noOfMistakes;
    }

    public void setNoOfMistakes(String noOfMistakes) {
        this.noOfMistakes = noOfMistakes;
    }

    public String getRecordingName() {
        return recordingName;
    }

    public void setRecordingName(String recordingName) {
        this.recordingName = recordingName;
    }

    public String getNoiseDescription() {
        return noiseDescription;
    }

    public void setNoiseDescription(String noiseDescription) {
        this.noiseDescription = noiseDescription;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getGroundTruthDescription() {
        return groundTruthDescription;
    }

    public void setGroundTruthDescription(String groundTruthDescription) {
        this.groundTruthDescription = groundTruthDescription;
    }
}
