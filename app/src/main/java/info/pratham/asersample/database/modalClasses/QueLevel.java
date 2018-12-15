package info.pratham.asersample.database.modalClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PEF on 15/12/2018.
 */

public class QueLevel implements Serializable{
    private String level;
    private String subject;
    private int level_seq_cnt;
    private List<SingleQustion> questions = new ArrayList();
    private int mistakes = -1;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getLevel_seq_cnt() {
        return level_seq_cnt;
    }

    public void setLevel_seq_cnt(int level_seq_cnt) {
        this.level_seq_cnt = level_seq_cnt;
    }

    public List getQuestions() {
        return questions;
    }

    public void setQuestions(List questions) {
        this.questions = questions;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }
}
