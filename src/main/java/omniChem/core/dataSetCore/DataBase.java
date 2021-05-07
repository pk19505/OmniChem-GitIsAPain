package omniChem.core.dataSetCore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/***
 * Access point for the DB
 * class needs to be instantiated and then the runStatement command executed with a statement and an interface
 * the wrestleResults interface allows the results of the query to be extracted within context
 */
public class DataBase {

    /***
     * Subclass manages access port to the Database
     */
    private class DatabaseAccess {
        private ResultSet results = null;
        private Connection connect = null;
        private Statement state = null;
        private boolean result = false;

        /***
         *
         * @param query
         * @param wres
         * @throws Exception
         *
         * Method executes an SQL query given to it
         * it passes the results onto a class (which must be provided) which can then interpret them in context
         */
        public void runStatement(String query, WrestleResults wres) throws Exception {
            try {
                //open DB connection
                connect = DriverManager.getConnection("jdbc:h2:~/dataCore", "sa", "password");
                //executes a statement
                state = connect.createStatement();
                results = state.executeQuery(query);
                if (results != null) {
                    wres.getResults(results);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                //close DB connection
                close();
            }
        }

        /***
         * Function uses execute command to run statements with higher authority.
         * @param query
         * @throws Exception
         */
        public void executeStatement(String query) throws Exception {
            try {
                //open DB connection
                connect = DriverManager.getConnection("jdbc:h2:~/dataCore", "sa", "password");
                //executes a statement
                state = connect.createStatement();
                result = state.execute(query);
            } catch (Exception e) {
                throw e;
            } finally {
                //close DB connection
                close();
            }
        }

        /***
         * function closes all connections to the database
         */
        private void close() {
            try {
                if (results != null) {
                    results.close();
                }
                if (state != null) {
                    state.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
            }
        }

        /***
         * Function returns the result of calling an execute
         * @return
         */
        public boolean success() {
            return result;
        }
    }

    /***
     * Function adds an entity to the table given an SQL statement in string format
     */
    public void addEntity(String entityInstruction) {
        DatabaseAccess DB = new DatabaseAccess();
        try {
            DB.executeStatement(entityInstruction);

        } catch (Exception e) {
            System.out.print("\n" + e.getMessage());
        }
    }

    public void addEntity(Entity ent, String Table_name) {
        addEntity(ent.getSQL(Table_name));
    }


    /***
     * function adds a table to the database given it's SQL constructor string
     * @param tableInstruct
     */
    public void addTable(String tableInstruct) {
        DatabaseAccess DB = new DatabaseAccess();
        try {
            DB.executeStatement(tableInstruct);

        } catch (Exception e) {
            System.out.print("\n" + e.getMessage());
        }
    }

    /***
     * Function adds a table to the database given a table object
     * first it constructs the table, then it adds all the table entities
     * @param tab
     */
    public void addTable(@org.jetbrains.annotations.NotNull Table tab) {
        //System.out.print("Adding table");
        //creates table in DB
        addTable(tab.getSQL());
        DatabaseAccess DB = new DatabaseAccess();
        //adds entities to table
        List<Entity> entities = tab.getEntities();
        //if(DB.result){
        for (Entity i : entities) {
            try {
                DB.executeStatement(i.getSQL(tab.getName()));
            } catch (Exception e) {
                System.out.print("\n" + e.getMessage());
            }
        }
        //}
    }

    /***
     * function extracts a limited section of data from a database
     * @param name
     */
    public Table getTable(String name, int lower_bound, int upperBound) {
        String state = "SELECT * FROM " + name + " WHERE IDINT > "+lower_bound+" AND IDINT < "+upperBound+";";
        DatabaseAccess DB = new DatabaseAccess();
        GetTableData wrestle = new GetTableData();
        Table_Columns tableClolumns = getTableHeaders(name);
        wrestle.setColumns(tableClolumns);
        try {
            DB.runStatement(state, wrestle);
        } catch (Exception e) {
        }
        Table tab = wrestle.getTable();
        tab.setName(name);
        return tab;
    }

    /***
     * Function extracts an entire table from the database
     *
     * @param name
     * @return
     */
    public Table getWholeTable(String name) {
        String state = "SELECT * FROM " + name +";";
        DatabaseAccess DB = new DatabaseAccess();
        GetTableData wrestle = new GetTableData();
        Table_Columns tableClolumns = getTableHeaders(name);
        wrestle.setColumns(tableClolumns);
        try {
            DB.runStatement(state, wrestle);
        } catch (Exception e) {
        }
        Table tab = wrestle.getTable();
        tab.setName(name);
        return tab;
    }

    /***
     * function grabs all the table names and their column headers
     */
    public List<String> getTableNames() {
        DatabaseAccess DB = new DatabaseAccess();
        String statement = "SELECT TABLE_NAME \n" +
                "FROM INFORMATION_SCHEMA.TABLES\n" +
                "WHERE TABLE_TYPE = 'TABLE';";
        //create anonymous class to extract results
        TableNameResults Results = new TableNameResults();
        //run statement
        try {
            DB.runStatement(statement, Results);
        } catch (Exception e) {
        }

        return Results.getResults();
    }

    /***
     * Function returns an object containing the column names and variable types for a given table
     * @param Table
     * @return
     */
    public Table_Columns getTableHeaders(String Table) {
        String statement = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + Table + "';";
        DatabaseAccess DB = new DatabaseAccess();
        TableColumnCount wrestle = new TableColumnCount();
        try {
            DB.runStatement(statement, wrestle);
        } catch (Exception e) {
        }
        int number = wrestle.getResults();
        TableColumnMetrics metrics = new TableColumnMetrics(number);
        statement = "SELECT COLUMN_NAME, TYPE_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + Table + "';";
        try {
            DB.runStatement(statement, metrics);
        } catch (Exception e) {
        }
        Table_Columns col = new Table_Columns();
        col.setName(Table);
        col.setNames(metrics.getNames());
        col.setTypes(metrics.getTypes());
        return col;
    }

    /***
     * Function returns a list of tables and their headers (column names and data types)
     */
    public List<Table_Columns> getAllTableHeaders() {
        ArrayList<Table_Columns> tableHeaders = new ArrayList<>();
        List<String> tables = getTableNames();
        for (String t : tables) {
            tableHeaders.add(getTableHeaders(t));
        }
        return (List<Table_Columns>) tableHeaders.clone();
    }

    /***
     * Function performs a search of the database and creates a new table from the results
     * Take in an SQL query
     * modify it so that it creates a new table in the DB
     * run it but discard any results
     * return an OK token
     * caller can then get the table and display it
     */
    public void searchTable(String query) {
        DatabaseAccess DB = new DatabaseAccess();
        try {
            DB.executeStatement(query);
        } catch (Exception e) {System.out.print(e.getMessage());}

    }
    public void searchTable(search src) {
        searchTable(src.getSQL());
    }

    public void searchTable(String query, String name) throws Exception {
        query = "CREATE TABLE " + name + " AS " + query;
        DatabaseAccess DB = new DatabaseAccess();
        DB.executeStatement(query);
    }


    /***
     * Function deletes a table given the table object
     */
    public void dropTable(Table tab) {
        dropTable(tab.getName());
    }

    /***
     * Function deletes a table given the table name
     */
    public void dropTable(String tab) {
        String state = "DROP TABLE " + tab + ";";
        DatabaseAccess DB = new DatabaseAccess();
        try {
            DB.runStatement(state, null);
        } catch (Exception e) {
        }
    }

    /***
     * Private interface classes that extract the results from the various SQL queries
     */
    private interface WrestleResults {
        void getResults(ResultSet res) throws Exception;
    }

    /***
     * Interface implementation for extracting the names of each table in the DB
     */
    private class TableNameResults implements WrestleResults {
        ArrayList<String> results = new ArrayList<>();

        @Override
        public void getResults(ResultSet res) throws SQLException {
            while (res.next()) {
                results.add(res.getString("TABLE_NAME"));
            }
        }

        public List<String> getResults() {
            return results;
        }
    }

    /***
     * Interface implementation for extracting the number of columns a table has
     */
    private class TableColumnCount implements WrestleResults {
        int count;

        @Override
        public void getResults(ResultSet res) throws SQLException {
            while (res.next()) {
                count = res.getInt("COUNT(*)");
            }
        }

        public int getResults() {
            return count;
        }
    }

    /***
     * Interface implementation for extracting the names and vartypes of each column in a table
     */
    private class TableColumnMetrics implements WrestleResults {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        int count = 0;

        private TableColumnMetrics(int i) {
            count = i;
        }

        @Override
        public void getResults(ResultSet res) throws SQLException {
            while (res.next()) {
                names.add(res.getString("COLUMN_NAME"));
                types.add(res.getString("TYPE_NAME"));
            }
        }

        public List<String> getTypes() {
            return names;
        }

        public List<String> getNames() {
            return types;
        }
    }

    /***
     * Interface implementation for extracting data from a table query
     */
    private class GetTableData implements WrestleResults {

        private Table_Columns template;
        private Table table = new Table();

        private void setColumns(Table_Columns cols) {
            template = cols;
        }

        @Override
        public void getResults(ResultSet res) throws Exception {
            for (int i = 0; i < template.getNames().size(); i++) {
                DataPoint attribute = new DataPoint();
                attribute.setName(template.getNames().get(i));
                attribute.setVarType(template.getTypes().get(i));
                table.addAttribute(attribute);
            }
            while(res.next()) {
                Entity temp = new Entity();
                for (int i = 0; i < template.getNames().size(); i++) {
                    String type = template.getTypes().get(i);
                    String name = template.getNames().get(i);
                    temp.addAttribute(decodeType(type, name, res));
                }
                table.addEntity(temp);
            }
        }

        private DataPoint decodeType(String type, String name, ResultSet res) throws Exception{
            DataPoint temp = new DataPoint();
            temp.setVarType(type);
            temp.setName(name);
                if (type.contains("DOUBLE")) {
                    double val = res.getDouble(name);
                    temp.setValue(Double.toString(val));
                } else if (type.contains("INT")) {
                    int val = res.getInt(name);
                    temp.setValue(Integer.toString(val));
                } else if (type.contains("VARCHAR")) {
                    temp.setValue(res.getString(name));
                }
            return temp;
        }
        private Table getTable() {return table;}
    }


}