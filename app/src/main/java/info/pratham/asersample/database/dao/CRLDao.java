package info.pratham.asersample.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import info.pratham.asersample.database.modalClasses.CRL;

/**
 * Created by PEF on 27/11/2018.
 */
@Dao
public interface CRLDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCrl(List<CRL> crlList);

    @Query("SELECT * FROM CRL WHERE UserName=:user AND Password=:pass")
    public CRL checkUserValidation(String user, String pass);
}
