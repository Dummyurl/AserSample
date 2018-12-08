package info.pratham.asersample.networkManager;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.pratham.asersample.ASERApplication;
import info.pratham.asersample.R;
import info.pratham.asersample.utility.AserSampleUtility;
import info.pratham.asersample.utility.AserSample_Constant;

public class UploadRec extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.sync)
    ImageButton syncBtn;

    List<String> fileList;
    private StorageReference mStorageRef;
    ArrayAdapter arrayAdapter;

    ProgressDialog progressDialog;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_rec);
        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        getLocalData();
    }

    private void getLocalData() {
        String path = ASERApplication.getInstance().getRootPath() + AserSample_Constant.crlID + "/";
        File directory = new File(path);
        fileList = new ArrayList();
        File[] subFolderList = directory.listFiles();

        if (subFolderList != null)
            for (File file : subFolderList) {
                if (file.isFile()) {
                    //   fileList.add(file);
                } else if (file.isDirectory()) {
                    fileList.add(file.getName());
                }
            }

        updateUI(fileList);
    }

    private void updateUI(List fileList) {
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fileList);
        listView.setAdapter(arrayAdapter);

    }


    public boolean zipFileAtPath(String sourcePath, String toLocation) {
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin;
            origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                entry.setTime(sourceFile.lastModified()); // to keep modification time after unzipping
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void zipSubFolder(ZipOutputStream out, File folder,
                              int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                entry.setTime(file.lastModified()); // to keep modification time after unzipping
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    @OnClick(R.id.sync)
    public void syncAll() {
        if (fileList.isEmpty()) {
            AserSampleUtility.showToast(this, "Nothing to push");
        } else {
            cnt = 0;
            AserSampleUtility.showProgressDialog(progressDialog);
            for (String path : fileList) {
                if (zipFileAtPath(ASERApplication.getInstance().getRootPath() + AserSample_Constant.crlID + "/" + path, ASERApplication.getInstance().getRootPath() + AserSample_Constant.crlID + "/" + path + ".zip")) {
                    uploadImageToStorage(path + ".zip");
                }
            }
        }
    }

    public void uploadImageToStorage(String fileUri) {
        mStorageRef = FirebaseStorage.getInstance().getReference();

//        getPredictions("");
        Uri file = Uri.fromFile(new File(ASERApplication.getInstance().getRootPath() + AserSample_Constant.crlID + "/" + fileUri));

        StorageReference riversRef = mStorageRef.child("StudentRecordings/" + AserSample_Constant.crlID + "/" + fileUri);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        cnt++;
                        String path = taskSnapshot.getMetadata().getPath();
                        path = path.replace(".zip", "");
                        File fdelete = new File(ASERApplication.getInstance().getRootPathForDeletion() + "/" + path);
                        if (fdelete.exists()) {
                            deleteRecursive(fdelete);
                        }
                        getLocalData();
                        if (cnt >= fileList.size()) {
                            AserSampleUtility.dismissProgressDialog(progressDialog);
                        }
                        // Get a URL to the uploaded content
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        //  Log.v("success", downloadUrl.toString());
                        //  removeData(name, fatherName, isLast);
                        Toast.makeText(UploadRec.this, "Uploaded" + path, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        cnt++;
                        if (cnt >= fileList.size()) {
                            AserSampleUtility.dismissProgressDialog(progressDialog);
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

//                        e.printStackTrace();
                        Log.v("Failure", "failed");
                    }
                });

    }

    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();

    }
}
