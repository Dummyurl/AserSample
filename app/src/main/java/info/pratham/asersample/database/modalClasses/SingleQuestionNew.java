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
    private String azure_Transcript;
    private String azure_Confidence;
    private String azure_Distance;
    private String azure_Scored_Labels;
    private String azure_Scored_Probabilities;

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

    public String getAzure_Transcript() {
        return azure_Transcript;
    }

    public void setAzure_Transcript(String azure_Transcript) {
        this.azure_Transcript = azure_Transcript;
    }

    public String getAzure_Confidence() {
        return azure_Confidence;
    }

    public void setAzure_Confidence(String azure_Confidence) {
        this.azure_Confidence = azure_Confidence;
    }

    public String getAzure_Distance() {
        return azure_Distance;
    }

    public void setAzure_Distance(String azure_Distance) {
        this.azure_Distance = azure_Distance;
    }

    public String getAzure_Scored_Labels() {
        return azure_Scored_Labels;
    }

    public void setAzure_Scored_Labels(String azure_Scored_Labels) {
        this.azure_Scored_Labels = azure_Scored_Labels;
    }

    public String getAzure_Scored_Probabilities() {
        return azure_Scored_Probabilities;
    }

    public void setAzure_Scored_Probabilities(String azure_Scored_Probabilities) {
        this.azure_Scored_Probabilities = azure_Scored_Probabilities;
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
