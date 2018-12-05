package info.pratham.asersample.database.modalClasses;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by PEF on 28/11/2018.
 */
@IgnoreExtraProperties
public class Student implements Serializable {
    public String id;
    public String name;
    public String father;
    public String village;
    public String studClass;
    public String ageGroup;

    public String nativeLanguageProficiency;
    public String mathProficiency;
    public String englishProficiency;

    public Student() {
    }

    public Student(String id, String name, String father, String village, String studClass, String ageGroup) {
        this.id = id;
        this.name = name;
        this.father = father;
        this.village = village;
        this.studClass = studClass;
        this.ageGroup = ageGroup;
    }

    public String getNativeLanguageProficiency() {
        return nativeLanguageProficiency;
    }

    public void setNativeLanguageProficiency(String nativeLanguageProficiency) {
        this.nativeLanguageProficiency = nativeLanguageProficiency;
    }

    public String getMathProficiency() {
        return mathProficiency;
    }

    public void setMathProficiency(String mathProficiency) {
        this.mathProficiency = mathProficiency;
    }

    public String getEnglishProficiency() {
        return englishProficiency;
    }

    public void setEnglishProficiency(String englishProficiency) {
        this.englishProficiency = englishProficiency;
    }
}
