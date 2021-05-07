package omniChem.core.dataSetCore;

import java.util.ArrayList;
import java.util.List;

public class Table_Columns {
    public String getName() {
        return name;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    private String name = new String();
    private List<String> names = new ArrayList<>();
    private List<String> types = new ArrayList<>();

}
