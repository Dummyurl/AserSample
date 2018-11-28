package info.pratham.asersample.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import java.util.List;

import info.pratham.asersample.database.modalClasses.CRL;

/**
 * Created by PEF on 27/11/2018.
 */
@Dao
public interface CRLdao {
    @Insert
    public void insertCRL(List<CRL> crl);
}
