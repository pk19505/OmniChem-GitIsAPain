package omniChem.core.dataSetCore;

import javax.xml.crypto.Data;

/***
 * Class for data points (SMILE strings, CAS numbers, Melting points, finger-prints, etc.)
 *
 */
public class DataPoint {
    private String VarType;
    private String Name;
    private String Value;


    //Setters
    public void setVarType(String var) {
        this.VarType = var;
    }

    public void setName(String name) {
        this.Name = name;
    }

    //Non string values can be converted from strings at a later point
    public void setValue(String val) {
        this.Value = val;
    }

    //Getters
    public String getVarType(){
        return this.VarType;
    }

    public String getName(){
        return this.Name;
    }

    public String getValue(){
        return this.Value;
    }


    public DataPoint clone() {
        DataPoint dataPoint = new DataPoint();
        dataPoint.setName(this.Name);
        dataPoint.setVarType(this.VarType);
        dataPoint.setValue(this.Value);
        return dataPoint;
    }
}
