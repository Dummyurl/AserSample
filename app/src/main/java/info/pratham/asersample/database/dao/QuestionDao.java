package info.pratham.asersample.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import info.pratham.asersample.database.modalClasses.Question;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuestions(List<Question> questions);

    @Query("SELECT * FROM Question")
    List<Question> getAllQuestions();

    @Query("DELETE FROM Question")
    void deleteAllQuestions();

    @Query("SELECT dataJson FROM Question where language=:lang")
    String getLanguageQuestions(String lang);
}
