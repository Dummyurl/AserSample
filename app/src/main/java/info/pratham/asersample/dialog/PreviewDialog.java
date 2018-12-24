package info.pratham.asersample.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.R;
import info.pratham.asersample.expandableRecyclerView.RecyclerAdapter;
import info.pratham.asersample.interfaces.PreviewDialogListener;

/**
 * Created by PEF on 22/12/2018.
 */

public class PreviewDialog extends Dialog {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    Context context;
    PreviewDialogListener previewDialogListener;

    public PreviewDialog(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_Light_NoActionBar);
        this.context = context;
        previewDialogListener = (PreviewDialogListener) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_dialog);
        ButterKnife.bind(this);
        recyclerView.setAdapter(new RecyclerAdapter(recyclerView));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    /*@OnClick(R.id.submit)
    public void cancel() {
        dismiss();
        previewDialogListener.onSubmit();
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        previewDialogListener.onSubmit();
    }
}
