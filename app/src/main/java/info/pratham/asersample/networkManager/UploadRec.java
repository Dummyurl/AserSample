package info.pratham.asersample.networkManager;

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
import info.pratham.asersample.utility.AserSample_Constant;

public class UploadRec extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.sync)
    ImageButton synk;

    List<String> fileList;
    private StorageReference mStorageRef;
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_rec);
        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getLocalData();
    }

    private void getLocalData() {
        List<File> files = new ArrayList<>();
        String path = ASERApplication.getRootPath() + AserSample_Constant.crlID + "/";
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
        for (String path : fileList) {
            if (zipFileAtPath(ASERApplication.getRootPath() + AserSample_Constant.crlID + "/" + path, ASERApplication.getRootPath() + AserSample_Constant.crlID + "/" + path + ".zip")) {
                uploadImageToStorage(path + ".zip");
            }
        }
    }

    public void uploadImageToStorage(String fileUri) {
        mStorageRef = FirebaseStorage.getInstance().getReference();

//        getPredictions("");
        Uri file = Uri.fromFile(new File(ASERApplication.getRootPath() + AserSample_Constant.crlID + "/" + fileUri));

        StorageReference riversRef = mStorageRef.child("StudentRecordings/" + AserSample_Constant.crlID + "/" + fileUri);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String path = taskSnapshot.getMetadata().getPath();
                        path = path.replace(".zip", "");
                        File fdelete = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + path);
                        if (fdelete.exists()) {
                            deleteRecursive(fdelete);
                          /*  if (fdelete.isDirectory()) {
                                String[] children = fdelete.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(fdelete, children[i]).delete();
                                }
                            }*/
                        }
                    /*    File file = new File(ASERApplication.getRootPath() + AserSample_Constant.crlID + "/" +path);
                        file.delete();
                        if (file.exists()) {
                            try {
                                file.getCanonicalFile().delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (file.exists()) {
                                getApplicationContext().deleteFile(file.getName());
                            }
                        }

                        String path1 = path.replace(".zip", "");
                        File file1 = new File(ASERApplication.getRootPath() + AserSample_Constant.crlID + "/" +path1);
                        file1.delete();
                        if (file1.exists()) {
                            try {
                                file1.getCanonicalFile().delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (file1.exists()) {
                                getApplicationContext().deleteFile(file1.getName());
                            }
                        }*/
                        getLocalData();
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
