package info.pratham.asersample.interfaces;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import info.pratham.asersample.database.modalClasses.QuestionStructure;

public interface RecordPrepairListner {
    public void onRecordingStarted(@NonNull Context context, QuestionStructure questionStructure, String level, boolean isAttemptedQue, RecyclerView.Adapter recyclerViewAdapter);
}
