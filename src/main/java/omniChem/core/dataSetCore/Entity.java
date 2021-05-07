package omniChem.core.dataSetCore;

import java.util.ArrayList;

/***
 * class represents rows of table and so has variable sized arrays of data types
 *
 */

public class Entity {

    /***
     * List holds a series of DataPoints for each entity, a data-point being some value
     */
    private ArrayList<DataPoint> attributes = new ArrayList<DataPoint>();

    /***
     * Function adds a DataPoint to an entity
     * @param type: the datapoint to add to the list
     */
    public void addAttribute(DataPoint type) {
        attributes.add(type);
    }

    /***
     *
     * @param tableName: the table this entity is a part of
     * @return: an SQL statement that will add this entity to said table
     */
    public String getSQL(String tableName) {
        String statement = "INSERT INTO " + tableName +" (";
        String data = "VALUES (";
        boolean first = true;
        for(DataPoint field : attributes) {
            if(first) {
                statement = statement + field.getName();
                data = data + field.getValue();
                first = false;
            }
            else {
                statement = statement + ", " + field.getName();
                data = data + ", " + field.getValue();
            }
        }
        statement = statement+ ") ";
        data = data + ");";
        statement = statement + data;
        //System.out.print("\n"+statement);
        return statement;
    }

    public Entity clone() {
        Entity entity = new Entity();
        entity.attributes = (ArrayList<DataPoint>) this.attributes.clone();
        return entity;
    }

    public void clear() {
        this.attributes = new ArrayList<DataPoint>();
    }

}
