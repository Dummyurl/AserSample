package info.pratham.asersample.database.modalClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Question {
    @NonNull
    @PrimaryKey
    private String language;
    private String dataJson;

    @NonNull
   /* public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(@NonNull int questionId) {
        this.questionId = questionId;
    }*/

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
}
