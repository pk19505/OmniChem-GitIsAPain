package omniChem.core.dataSetCore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/***
 * Class is a table and as such holds an array list of entites and a dictionary of pairs
 */
public class Table {
    private ArrayList<DataPoint> attributes = new ArrayList<DataPoint>();
    private ArrayList<Entity> entities = new ArrayList<>();
    private String name;

    /***
     * Adds an entity to the list of entities
     * @param ent
     */
    public void addEntity(Entity ent) {
        Entity ent2 = ent.clone();
        entities.add(ent2);
    }

    /***
     * setter for table name
     * @param Name
     */
    public void setName(String Name) {
        this.name = Name;
    }

    /***
     * Getter for table name
     * @return
     */
    public String getName() {
        return this.name;
    }

    /***
     * Function adds DataPoint to list of fields
     * @param type
     */
    public void addAttribute(DataPoint type) {
        attributes.add(type);
    }

    /***
     * Function returns a copy of the list of entities in this class
     * @return
     */
    public List<Entity> getEntities() {
        return Collections.unmodifiableList((List<Entity>) this.entities.clone());
    }

    /***
     * Function returns a SQL statement that will create this table in the DB
     * @return
     */
    public String getSQL() {
        String statement = "CREATE TABLE " + this.name + " (IDint int NOT NULL AUTO_INCREMENT";

        for(DataPoint field : attributes) {
            statement = statement + ", " + field.getName() + " " + field.getVarType();
        }
        statement = statement + ");";
        return statement;
    }

}
