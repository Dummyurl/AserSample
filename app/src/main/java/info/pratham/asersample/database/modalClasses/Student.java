package info.pratham.asersample.database.modalClasses;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PEF on 28/11/2018.
 */
@IgnoreExtraProperties
public class Student {
    private String id;
    private String name;
    private String father;
    private String village;
    private String studClass;
    private String ageGroup;
    private String date;
    private String deviceID;

    private String nativeProficiency;
    private String mathematicsProficiency;
    private String englishProficiency;

    @SerializedName("sequenceList")
    private List<QueLevel> sequenceList = new ArrayList();


    public Student() {
    }

    public Student(String id, String name, String father, String village, String studClass, String ageGroup, String date, String deviceID) {
        this.id = id;
        this.name = name;
        this.father = father;
        this.village = village;
        this.studClass = studClass;
        this.ageGroup = ageGroup;
        this.date = date;
        this.deviceID = deviceID;
    }

    public String getNativeProficiency() {
        return nativeProficiency;
    }

    public void setNativeProficiency(String nativeProficiency) {
        this.nativeProficiency = nativeProficiency;
    }

    public String getMathematicsProficiency() {
        return mathematicsProficiency;
    }

    public void setMathematicsProficiency(String mathematicsProficiency) {
        this.mathematicsProficiency = mathematicsProficiency;
    }

    public String getEnglishProficiency() {
        return englishProficiency;
    }

    public void setEnglishProficiency(String englishProficiency) {
        this.englishProficiency = englishProficiency;
    }

    // todo remove
  /*  private NativeLanguageProficiency nativeLanguage = new NativeLanguageProficiency();
    private MathProficiency mathematics = new MathProficiency();
    private EnglishProficiency english = new EnglishProficiency();*/

    public List getSequenceList() {
        return sequenceList;
    }

    public void setSequenceList(List sequenceList) {
        this.sequenceList = sequenceList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getStudClass() {
        return studClass;
    }

    public void setStudClass(String studClass) {
        this.studClass = studClass;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    /* public NativeLanguageProficiency getNativeLanguage() {
         return nativeLanguage;
     }

     public void setNativeLanguage(NativeLanguageProficiency nativeLanguage) {
         this.nativeLanguage = nativeLanguage;
     }

     public MathProficiency getMathematics() {
         return mathematics;
     }

     public void setMathematics(MathProficiency mathematics) {
         this.mathematics = mathematics;
     }

     public EnglishProficiency getEnglish() {
         return english;
     }

     public void setEnglish(EnglishProficiency english) {
         this.english = english;
     }
 */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
