package info.pratham.asersample.database.modalClasses;

/**
 * Created by PEF on 11/12/2018.
 */

public class QuestionStructure {
    String id;
    String data;
    //in case of subtraction and in division
    String answer;
    String remainder;
    boolean isSelected = false;

    public QuestionStructure(String id, String data) {
        this.id = id;
        this.data = data;
    }

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

    @Override
    public String toString() {
        return data;
    }
}
