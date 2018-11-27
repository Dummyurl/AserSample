package info.pratham.asersample.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import info.pratham.asersample.database.dao.CRLdao;
import info.pratham.asersample.database.modalClasses.CRL;


@Database(entities = {CRL.class}, version = 3, exportSchema = false)
public abstract class AS_Database extends RoomDatabase {
    private static AS_Database DATABASEINSTANCE;

    public abstract CRLdao getCRLdao();

    public static AS_Database getDatabaseInstance(Context context) {
        if (DATABASEINSTANCE == null)
            DATABASEINSTANCE = Room.databaseBuilder(context.getApplicationContext(), AS_Database.class, "prathamDb").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        return DATABASEINSTANCE;
    }

    public static void destroyInstance() {
        DATABASEINSTANCE = null;
    }
}
