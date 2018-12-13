package info.pratham.asersample.database.modalClasses;

/**
 * Created by PEF on 13/12/2018.
 */

public class MathQueAns {
    int attemp;
    String id;
    String que;
    String ans;

    public MathQueAns(int attemp, String id, String que, String ans) {
        this.attemp = attemp;
        this.id = id;
        this.que = que;
        this.ans = ans;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public int getAttemp() {
        return attemp;
    }

    public void setAttemp(int attemp) {
        this.attemp = attemp;
    }
}
