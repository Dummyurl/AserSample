package info.pratham.asersample.utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.pratham.asersample.ASERApplication;

/**
 * Created by PEF on 30/11/2018.
 */

public class AserSample_Constant {
    public static String selectedLanguage;
    public static JSONObject sample;


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

    public static String getStory(JSONObject sample, String subElement) {
        String story = "";
        try {
            story = sample.getString(subElement);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return story;
    }

    public static String getPara(JSONObject sample, String subElement) {
        String para = "";
        int randomNo;
        try {
            JSONArray paraArray = sample.getJSONArray(subElement);
            randomNo = ASERApplication.getRandomNumber(0, paraArray.length());
            para = paraArray.get(randomNo).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return para;
    }

    public static JSONArray getMathOperation(JSONObject sample, String subElement) {
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
}
