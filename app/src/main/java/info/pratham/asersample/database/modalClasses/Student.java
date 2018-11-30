package info.pratham.asersample.database.modalClasses;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by PEF on 28/11/2018.
 */
@IgnoreExtraProperties
public class Student {
    public String name;
    public String father;
    public String village;
    public String studClass;
    public String ageGroup;
    public Student() {
    }

    public Student(String name, String father, String village, String studClass, String ageGroup) {
        this.name = name;
        this.father = father;
        this.village = village;
        this.studClass = studClass;
        this.ageGroup = ageGroup;
    }
}
