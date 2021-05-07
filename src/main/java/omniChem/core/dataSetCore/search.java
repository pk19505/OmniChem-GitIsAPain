package omniChem.core.dataSetCore;

import java.util.ArrayList;

/***
 * POJO for accumulating data and then producing a search query from said data
 */
public class search {

    /***
     * Private class used for holding various conditions applied to the search
     */
    private class clause {
        String column;
        String function;
        String value;
        String prefix;
        String postfix;
    }
    private String name;
    private String join;
    private String joinTable;
    private clause joinClause = null;
    private ArrayList<String> tables = new ArrayList<>();
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<clause> clauses = new ArrayList<>();

    public void setJoin(String join) {
        this.join = join;
    }

    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }

    public void setJoinClause(String columnOne, String columnTwo){
        clause temp = new clause();
        temp.prefix = columnOne;
        temp.postfix = columnTwo;
        this.joinClause = temp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTable(String table) {
        tables.add(table);
    }

    public void addColumn(String column) {
        columns.add(column);
    }

    public void addClause(String prefix, String column, String function, String value, String postfix) {
        clause temp = new clause();
        temp.column = column;
        temp.function = function;
        temp.value = value;
        temp.postfix = postfix;
        temp.prefix = prefix;
        clauses.add(temp);
    }

    /***
     * Method converts the class into an SQL query in string format
     * @return
     */
    public String getSQL() {
        String query = "CREATE TABLE " + this.name + "AS SELECT (";
        //add all the chosen columns
        for(String i : columns) {
            query = query + i + ", ";
        }
        query.substring(0, query.length() - 2);
        query = query + ") FROM ";
        //add all the chosen tables
        for(String i : tables) {
            query = query + i +", ";
        }
        query.substring(0, query.length() - 2);
        if(!clauses.isEmpty()) {
            query = query + " WHERE ";
            for(clause i : clauses) {
                String command = i.prefix +" "+ i.column +i.function+i.value+" "+i.postfix+" ";
                query = query + command;
            }
        }
        if(joinClause != null) {
            String joinStatement = this.join +" "+this.joinTable+" ON " +joinClause.prefix+"="+joinClause.postfix;
        }
        query = query + ";";
        return query;
    }

}
