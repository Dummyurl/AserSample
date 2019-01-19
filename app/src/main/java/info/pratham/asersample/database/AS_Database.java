package info.pratham.asersample.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import info.pratham.asersample.database.dao.CRLDao;
import info.pratham.asersample.database.dao.LogDao;
import info.pratham.asersample.database.dao.QuestionDao;
import info.pratham.asersample.database.modalClasses.CRL;
import info.pratham.asersample.database.modalClasses.Modal_Log;
import info.pratham.asersample.database.modalClasses.Question;


@Database(entities = {CRL.class, Question.class, Modal_Log.class}, version = 1, exportSchema = false)
public abstract class AS_Database extends RoomDatabase {
    private static AS_Database DATABASEINSTANCE;

    public static AS_Database getDatabaseInstance(Context context) {
        if (DATABASEINSTANCE == null)
            DATABASEINSTANCE = Room.databaseBuilder(context.getApplicationContext(), AS_Database.class, "AS_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        return DATABASEINSTANCE;
    }

    public static void destroyInstance() {
        DATABASEINSTANCE = null;
    }

    public abstract CRLDao getCRLdao();

    public abstract QuestionDao getQuestiondao();

    public abstract LogDao getLogsdao();
}
