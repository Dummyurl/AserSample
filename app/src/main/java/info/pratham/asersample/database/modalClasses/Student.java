package info.pratham.asersample.database.modalClasses;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by PEF on 28/11/2018.
 */
@IgnoreExtraProperties
public class Student implements Serializable {
    private String id;
    private String name;
    private String father;
    private String village;
    private String studClass;
    private String ageGroup;

    private NativeLanguageProficiency nativeLanguage = new NativeLanguageProficiency();
    private MathProficiency mathematics = new MathProficiency();
    private EnglishProficiency english = new EnglishProficiency();

    private String date;
    private String deviceID;


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

    public NativeLanguageProficiency getNativeLanguage() {
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
