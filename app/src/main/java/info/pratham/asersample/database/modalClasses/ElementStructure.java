package info.pratham.asersample.database.modalClasses;

/**
 * Created by PEF on 11/12/2018.
 */

public class ElementStructure {
    String id;
    String data;

    public ElementStructure(String id, String data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
