package info.pratham.asersample.database.modalClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import info.pratham.asersample.utility.AserSample_Constant;

/**
 * Created by PEF on 15/12/2018.
 */

public class SingleQuestionNew implements Serializable {
    /* private int que_seq_cnt;*/
    private String que_id;
    private String que_text;
    private String answer;
    private String remainder;
    private String startTime;
    private String endTime;
    @SerializedName("correct")
    private boolean isCorrect;
    private String noOfMistakes;
    private String recordingName;
    private String noiseDescription;
    private String remarks;
    private String groundTruthDescription;

    @SerializedName("stt_Transcript")
    private String stt_Transcript;
    @SerializedName("stt_Confidence")
    private String stt_Confidence;
    @SerializedName("edit_Distance")
    private String edit_Distance;
    @SerializedName("model_Scored_Labels")
    private String model_Scored_Labels;
    @SerializedName("model_Scored_Probabilities")
    private String model_Scored_Probabilities;

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

    public void setCorrect(boolean correct) {
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

    public String getStt_Transcript() {
        return stt_Transcript;
    }

    public void setStt_Transcript(String stt_Transcript) {
        this.stt_Transcript = stt_Transcript;
    }

    public String getStt_Confidence() {
        return stt_Confidence;
    }

    public void setStt_Confidence(String stt_Confidence) {
        this.stt_Confidence = stt_Confidence;
    }

    public String getEdit_Distance() {
        return edit_Distance;
    }

    public void setEdit_Distance(String edit_Distance) {
        this.edit_Distance = edit_Distance;
    }

    public String getModel_Scored_Labels() {
        return model_Scored_Labels;
    }

    public void setModel_Scored_Labels(String model_Scored_Labels) {
        this.model_Scored_Labels = model_Scored_Labels;
    }

    public String getModel_Scored_Probabilities() {
        return model_Scored_Probabilities;
    }

    public void setModel_Scored_Probabilities(String model_Scored_Probabilities) {
        this.model_Scored_Probabilities = model_Scored_Probabilities;
    }

/*  public int getQue_seq_cnt() {
        return que_seq_cnt;
    }

    public void setQue_seq_cnt(int que_seq_cnt) {
        this.que_seq_cnt = que_seq_cnt;
    }*/

    /*public String getQue_id() {
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

    public String getRemainder() {
        return remainder;
    }

    public void setRemainder(String remainder) {
        this.remainder = remainder;
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
    }*/
}
