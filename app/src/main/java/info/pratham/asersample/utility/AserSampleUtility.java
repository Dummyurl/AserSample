package info.pratham.asersample.utility;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import info.pratham.asersample.ASERApplication;
import info.pratham.asersample.R;
import info.pratham.asersample.activities.ValidateActivity;
import info.pratham.asersample.database.modalClasses.SingleQuestionNew;
import info.pratham.asersample.database.modalClasses.SingleQuestionNewValidation;
import info.pratham.asersample.database.modalClasses.Student;
import info.pratham.asersample.database.modalClasses.StudentValidation;
import info.pratham.asersample.fragments.SelectLanguageFragment;

/**
 * Created by PEF on 24/11/2018.
 */

public class AserSampleUtility {

    private static final String PRIMARY_VOLUME_NAME = "primary";

    public static void showFragment(Activity activity, Fragment fragment, String TAG) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static void removeFragment(Activity activity, String TAG) {
        activity.getFragmentManager().popBackStackImmediate();
        Fragment fragment = activity.getFragmentManager().findFragmentByTag(TAG);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment).commit();
            activity.getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    public static void showProblemAlert(String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Problem with the server !!");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    public static void showSuccessAlert(String msg, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Successful !!");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    public static void showProgressDialog(ProgressDialog progressDialog, String msg) {
        if (progressDialog != null) {
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public static void changeMessage(ProgressDialog progressDialog, String msg) {
        if (progressDialog != null) {
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
        }
    }

    public static void dismissProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }

    public static String currentTime() {
        return DateFormat.getDateTimeInstance().format(new Date());
    }

    public static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    public static StudentValidation convertStudentForValidation(Student student) {
        StudentValidation studentValidation = new StudentValidation();
        if (student != null) {
            studentValidation.setAgeGroup(student.getAgeGroup() != null ? student.getAgeGroup() : "");
            studentValidation.setDate(student.getDate() != null ? student.getDate() : "");
            studentValidation.setDeviceID(student.getDeviceID() != null ? student.getDeviceID() : "");
            studentValidation.setEnglishProficiency(student.getEnglishProficiency() != null ? student.getEnglishProficiency() : "");
            studentValidation.setFather(student.getFather() != null ? student.getFather() : "");
            studentValidation.setGender(student.getGender() != null ? student.getGender() : "");
            studentValidation.setId(student.getId() != null ? student.getId() : "");
            studentValidation.setMathematicsProficiency(student.getMathematicsProficiency() != null ? student.getMathematicsProficiency() : "");
            studentValidation.setName(student.getName() != null ? student.getName() : "");
            studentValidation.setNativeProficiency(student.getNativeProficiency() != null ? student.getNativeProficiency() : "");
            studentValidation.setStudClass(student.getStudClass() != null ? student.getStudClass() : "");
            studentValidation.setValidatedDate(student.getValidatedDate() != null ? student.getValidatedDate() : "");
            studentValidation.setVillage(student.getVillage() != null ? student.getVillage() : "");
            // converting the correct field to isCorrect for storage json
            List<SingleQuestionNew> singleQuestionNewList = student.getSequenceList();
            List<SingleQuestionNewValidation> singleQuestionNewValidations = convertNewListToValidationList(singleQuestionNewList);
            studentValidation.setSequenceList(singleQuestionNewValidations);
        }
        return studentValidation;
    }

    private static List<SingleQuestionNewValidation> convertNewListToValidationList(List<SingleQuestionNew> singleQuestionNewList) {
        List<SingleQuestionNewValidation> singleQuestionNewValidations = new ArrayList<>();
        if (singleQuestionNewList != null) {
            SingleQuestionNewValidation singleQuestionNewValidationObj;
            SingleQuestionNew singleQuestionNewObj;
            for (int i = 0; i < singleQuestionNewList.size(); i++) {
                singleQuestionNewObj = singleQuestionNewList.get(i);
                singleQuestionNewValidationObj = new SingleQuestionNewValidation();

                singleQuestionNewValidationObj.setAnswer(singleQuestionNewObj.getAnswer() != null ? singleQuestionNewObj.getAnswer() : "");
                singleQuestionNewValidationObj.setEndTime(singleQuestionNewObj.getEndTime() != null ? singleQuestionNewObj.getEndTime() : "");
                singleQuestionNewValidationObj.setGroundTruthDescription(singleQuestionNewObj.getGroundTruthDescription() != null ? singleQuestionNewObj.getGroundTruthDescription() : "");
                singleQuestionNewValidationObj.setNoiseDescription(singleQuestionNewObj.getNoiseDescription() != null ? singleQuestionNewObj.getNoiseDescription() : "");
                singleQuestionNewValidationObj.setNoOfMistakes(singleQuestionNewObj.getNoOfMistakes() != null ? singleQuestionNewObj.getNoOfMistakes() : "");
                singleQuestionNewValidationObj.setQue_id(singleQuestionNewObj.getQue_id() != null ? singleQuestionNewObj.getQue_id() : "");
                singleQuestionNewValidationObj.setQue_text(singleQuestionNewObj.getQue_text() != null ? singleQuestionNewObj.getQue_text() : "");
                singleQuestionNewValidationObj.setRecordingName(singleQuestionNewObj.getRecordingName() != null ? singleQuestionNewObj.getRecordingName() : "");
                singleQuestionNewValidationObj.setRemainder(singleQuestionNewObj.getRemainder() != null ? singleQuestionNewObj.getRemainder() : "");
                singleQuestionNewValidationObj.setRemarks(singleQuestionNewObj.getRemarks() != null ? singleQuestionNewObj.getRemarks() : "");
                singleQuestionNewValidationObj.setStartTime(singleQuestionNewObj.getStartTime() != null ? singleQuestionNewObj.getStartTime() : "");

                //here is the actual fucking change
                singleQuestionNewValidationObj.setIsCorrect(singleQuestionNewObj.isCorrect());
                singleQuestionNewValidations.add(singleQuestionNewValidationObj);
            }
        }
        return singleQuestionNewValidations;
    }

    /*public static String getProperty(String key, Context context) {
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception ex) {
            return null;
        }
    }

    @Nullable
    public static String getFullPathFromTreeUri(@Nullable final Uri treeUri, Context con) {
        if (treeUri == null) {
            return null;
        }
        String volumePath = AserSampleUtility.getVolumePath(AserSampleUtility.getVolumeIdFromTreeUri(treeUri),con);
        if (volumePath == null) {
            return File.separator;
        }
        if (volumePath.endsWith(File.separator)) {
            volumePath = volumePath.substring(0, volumePath.length() - 1);
        }

        String documentPath = AserSampleUtility.getDocumentPathFromTreeUri(treeUri);
        if (documentPath.endsWith(File.separator)) {
            documentPath = documentPath.substring(0, documentPath.length() - 1);
        }

        if (documentPath.length() > 0) {
            if (documentPath.startsWith(File.separator)) {
                return volumePath + documentPath;
            }
            else {
                return volumePath + File.separator + documentPath;
            }
        }
        else {
            return volumePath;
        }
    }

    private static String getVolumePath(final String volumeId, Context con) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return null;
        }

        try {
            StorageManager mStorageManager =
                    (StorageManager) con.getSystemService(Context.STORAGE_SERVICE);

            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");

            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getUuid = storageVolumeClazz.getMethod("getUuid");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
            Object result = getVolumeList.invoke(mStorageManager);

            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String uuid = (String) getUuid.invoke(storageVolumeElement);
                Boolean primary = (Boolean) isPrimary.invoke(storageVolumeElement);

                // primary volume?
                if (primary && PRIMARY_VOLUME_NAME.equals(volumeId)) {
                    return (String) getPath.invoke(storageVolumeElement);
                }

                // other volumes?
                if (uuid != null) {
                    if (uuid.equals(volumeId)) {
                        return (String) getPath.invoke(storageVolumeElement);
                    }
                }
            }

            // not found.
            return null;
        }
        catch (Exception ex) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getVolumeIdFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");

        if (split.length > 0) {
            return split[0];
        }
        else {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static String getDocumentPathFromTreeUri(final Uri treeUri) {
        final String docId = DocumentsContract.getTreeDocumentId(treeUri);
        final String[] split = docId.split(":");
        if ((split.length >= 2) && (split[1] != null)) {
            return split[1];
        }
        else {
            return File.separator;
        }
    }*/

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static void writeStudentInJson(Context context) {
        String path = ASERApplication.getInstance().getRootPath() + AserSample_Constant.getCrlID() + "/" + AserSample_Constant.getAserSample_Constant().getStudent().getId() + "/";
        String fileName = path + AserSample_Constant.getAserSample_Constant().getStudent().getId() + "INFO.json";
        Gson gson = new Gson();
        String studentJson = gson.toJson(AserSample_Constant.getAserSample_Constant().getStudent());


        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(studentJson);
            outputStreamWriter.flush();
            fileOutputStream.getFD().sync();
            outputStreamWriter.close();


            /*android.app.AlertDialog builder = new android.app.AlertDialog.Builder(context).create();
            builder.setMessage(studentJson);
            builder.setCancelable(false);
            builder.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.show();*/
        } catch (IOException e) {
            android.app.AlertDialog builder = new android.app.AlertDialog.Builder(context).create();
            builder.setTitle("Error");
            builder.setMessage("failed to store Test data please contact admin");
            builder.setCancelable(false);
            builder.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            builder.show();
            e.printStackTrace();
        }
    }

   /* public static void startCelebration(KonfettiView celebrationView){
        celebrationView.setVisibility(View.VISIBLE);

        celebrationView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1500L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f,50f, -50f, -50f)
                .stream(500, 1000L);
    }*/

    public static void setTestOrValidationDialog(final Activity mContext) {
        final Dialog testOrValidationDialog = new Dialog(mContext);
        testOrValidationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        testOrValidationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        testOrValidationDialog.setContentView(R.layout.custom_dilog);
        testOrValidationDialog.setCanceledOnTouchOutside(false);
        Button testDialogButton = testOrValidationDialog.findViewById(R.id.dia_btn_test);
        testDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testOrValidationDialog.dismiss();
                AserSampleUtility.showFragment(mContext, new SelectLanguageFragment(), SelectLanguageFragment.class.getSimpleName());
            }
        });
        Button validationDialogButton = testOrValidationDialog.findViewById(R.id.dia_btn_validation);
        validationDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testOrValidationDialog.dismiss();
                Intent validateActivity = new Intent(mContext, ValidateActivity.class);
                validateActivity.putExtra("CRL_NAME", AserSample_Constant.getCrlID());
                mContext.startActivity(validateActivity);
//               AserSampleUtility.showToast(mContext, "Validations");
            }
        });
        testOrValidationDialog.show();
    }
}
