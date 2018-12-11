package info.pratham.asersample.database.modalClasses;

/**
 * Created by PEF on 06/12/2018.
 */

public class NativeLanguageProficiency {
    private String proficiency;
    private String story_mistake;
    private String paragragh_mistake;
    private String word_mistake;
    private String letter_mistake;


    private String[] story_que;
    private String[] paragragh_que;
    private String[] word_que;
    private String[] letter_que;

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public String getStory_mistake() {
        return story_mistake;
    }

    public void setStory_mistake(String story_mistake) {
        this.story_mistake = story_mistake;
    }

    public String getParagragh_mistake() {
        return paragragh_mistake;
    }

    public void setParagragh_mistake(String paragragh_mistake) {
        this.paragragh_mistake = paragragh_mistake;
    }

    public String getWord_mistake() {
        return word_mistake;
    }

    public void setWord_mistake(String word_mistake) {
        this.word_mistake = word_mistake;
    }

    public String getLetter_mistake() {
        return letter_mistake;
    }

    public void setLetter_mistake(String letter_mistake) {
        this.letter_mistake = letter_mistake;
    }
}
