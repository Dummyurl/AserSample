package info.pratham.asersample.utility;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.alhazmy13.catcho.library.Catcho;
import net.alhazmy13.catcho.library.error.CatchoError;

import info.pratham.asersample.BaseActivity;
import info.pratham.asersample.database.modalClasses.Modal_Log;


public class CatchoTransparentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CatchoError error = (CatchoError) getIntent().getSerializableExtra(Catcho.ERROR);
        Modal_Log log = new Modal_Log();
        log.setCurrentDateTime(/*TODO currentDateTime*/"date");
        log.setErrorType("ERROR");
        log.setExceptionMessage(error.toString());
        log.setExceptionStackTrace(error.getError());
        log.setMethodName("NO_METHOD");
        log.setGroupId("group");
        log.setDeviceId("");
        databaseInstance.getLogsdao().insertLog(log);
        finish();
    }
}
