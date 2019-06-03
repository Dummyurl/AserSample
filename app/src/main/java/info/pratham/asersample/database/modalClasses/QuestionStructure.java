package info.pratham.asersample.database.modalClasses;

import info.pratham.asersample.utility.AserSample_Constant;

/**
 * Created by PEF on 11/12/2018.
 */

public class QuestionStructure {
    private String id;
    private String data;
    //in case of subtraction and in division
    private String answer;
    private String remainder;
    private String noOfMistakes;
    private boolean isSelected = false;
    private String isCorrect = AserSample_Constant.NOTATTEMPED;
    private transient boolean isPlaying = false;

   /* public QuestionStructure(String id, String data) {
        this.id = id;
        this.data = data;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        if (isCorrect == null) {
            this.isCorrect = AserSample_Constant.NOTATTEMPED;
        } else {
            this.isCorrect = isCorrect;
        }
    }

    @Override
    public String toString() {
        return data;
    }

    public String getNoOfMistakes() {
        return noOfMistakes;
    }

    public void setNoOfMistakes(String noOfMistakes) {
        this.noOfMistakes = noOfMistakes;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
