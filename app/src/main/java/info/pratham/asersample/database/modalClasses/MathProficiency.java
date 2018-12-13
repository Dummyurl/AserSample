package info.pratham.asersample.database.modalClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PEF on 06/12/2018.
 */

public class MathProficiency {
    private String mathProficiency;

    private String subtrtaction_mistake;
    private String division_mistake;
    private String oneToNine_mistake;
    private String tenToNinetyNine_mistake;

    private List<MathQueAns> subrtaction1 = new ArrayList();
    private List<MathQueAns> subrtaction2 = new ArrayList();
    private List<MathQueAns> division = new ArrayList();

    public List<MathQueAns> getSubrtaction1() {
        return subrtaction1;
    }

  /*  public void setSubrtaction1(List<MathQueAns> subrtaction1) {
        this.subrtaction1 = subrtaction1;
    }*/

    public void addSubrtaction1(MathQueAns mathQueAns) {
        this.subrtaction1.add(mathQueAns);
    }

    public List<MathQueAns> getSubrtaction2() {
        return subrtaction2;
    }

    /* public void setSubrtaction2(List<MathQueAns> subrtaction2) {
         this.subrtaction2 = subrtaction2;
     }*/
    public void addSubrtaction2(MathQueAns mathQueAns) {
        this.subrtaction2.add(mathQueAns);
    }

    public List<MathQueAns> getDivision() {
        return division;
    }

    public void addDivision(MathQueAns mathQueAns) {
        this.division.add(mathQueAns);
    }
    /*public void setDivision(List<MathQueAns> division) {
        this.division = division;
    }*/

    public String getMathProficiency() {
        return mathProficiency;
    }

    public void setMathProficiency(String mathProficiency) {
        this.mathProficiency = mathProficiency;
    }

    public String getSubtrtaction_mistake() {
        return subtrtaction_mistake;
    }

    public void setSubtrtaction_mistake(String subtrtaction_mistake) {
        this.subtrtaction_mistake = subtrtaction_mistake;
    }

    public String getDivision_mistake() {
        return division_mistake;
    }

    public void setDivision_mistake(String division_mistake) {
        this.division_mistake = division_mistake;
    }

    public String getOneToNine_mistake() {
        return oneToNine_mistake;
    }

    public void setOneToNine_mistake(String oneToNine_mistake) {
        this.oneToNine_mistake = oneToNine_mistake;
    }

    public String getTenToNinetyNine_mistake() {
        return tenToNinetyNine_mistake;
    }

    public void setTenToNinetyNine_mistake(String tenToNinetyNine_mistake) {
        this.tenToNinetyNine_mistake = tenToNinetyNine_mistake;
    }
}
