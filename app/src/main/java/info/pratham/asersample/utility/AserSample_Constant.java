package info.pratham.asersample.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.pratham.asersample.ASERApplication;
import info.pratham.asersample.database.modalClasses.Student;

/**
 * Created by PEF on 30/11/2018.
 */

public class AserSample_Constant {
    public static String selectedLanguage;
    public static JSONObject sample;
    public static String crlID;
    private static AserSample_Constant aserSample_constant = null;
    private static String deviceID;
    Student student;
    public static String CORRECT = "right";
    public static String WRONG = "wrong";
    public static String NOTATTEMPED = "notChecked";


    /*StudentNew studentNew;


    public StudentNew getStudentNew() {
        return studentNew;
    }

    public void setStudentNew(StudentNew studentNew) {
        this.studentNew = studentNew;
    }
*/
    private AserSample_Constant() {
    }

    public static String getCrlID() {
        return crlID;
    }

    public static void setCrlID(String crlID) {
        AserSample_Constant.crlID = crlID;
    }

    public static String getDeviceID() {
        return deviceID;
    }

    public static void setDeviceID(String deviceID) {
        AserSample_Constant.deviceID = deviceID;
    }

    public static AserSample_Constant getAserSample_Constant() {
        if (aserSample_constant == null) {
            aserSample_constant = new AserSample_Constant();
        }
        return (aserSample_constant);
    }

    public static JSONArray getWords(JSONObject sample, String subElement) {
        JSONArray wordsArray;

        try {
            wordsArray = sample.getJSONArray(subElement);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return wordsArray;
    }

    public static JSONObject getStory(JSONObject sample, String subElement) {
        JSONArray story;
        JSONObject obj;
        try {
            story = sample.getJSONArray(subElement);
            obj = story.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public static JSONObject getPara(JSONObject sample, String subElement) {
        JSONObject para;
        int randomNo;
        try {
            JSONArray paraArray = sample.getJSONArray(subElement);
            randomNo = ASERApplication.getRandomNumber(0, paraArray.length());
            para = paraArray.getJSONObject(randomNo);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return para;
    }

    public static JSONArray getMathOperation(JSONObject sample, String subElement) {
        JSONArray dataArray;
        try {
            JSONObject mathObject = sample.getJSONObject("Math");
            dataArray = mathObject.getJSONArray(subElement);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return dataArray;
    }

    public static JSONArray getMathNumberRecognition(JSONObject sample, String subElement) {
        JSONArray dataArray;
        try {
            JSONObject mathObject = sample.getJSONObject("Math").getJSONObject("Number Recognition");
            dataArray = mathObject.getJSONArray(subElement);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return dataArray;
    }

    public static JSONArray getEnglishDataByLevel(JSONObject sample, String subElement) {
        JSONArray lettersArray;
        try {
            lettersArray = sample.getJSONObject("English").getJSONArray(subElement);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return lettersArray;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
