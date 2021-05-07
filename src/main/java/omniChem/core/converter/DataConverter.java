package omniChem.core.converter;

import omniChem.core.dataSetCore.DataPoint;
import omniChem.core.dataSetCore.Entity;
import omniChem.core.dataSetCore.Table;
import omniChem.core.dataSetCore.DataBase;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

import static java.lang.Math.max;

public class DataConverter {
     private Table recordArray;
  public DataConverter(String args,String name) {
    try{
      File inputFile;
      inputFile = new File(args);

   Scanner lineAtScanner = new Scanner(inputFile);

   String getAtt = lineAtScanner.nextLine();
   int attributes = 1; //How many columns there are


      for (int i = 0; i < getAtt.length(); i++) {
        if (getAtt.charAt(i) == ',') {
          attributes++;
          }
        }

        int totalLines = 1; // The first line has been read
        var columnSizes = new int[attributes];
        var separated = new String[attributes];
        while (lineAtScanner.hasNextLine()) {
          String input = lineAtScanner.nextLine();
          separated = input.split(","); // use att int to create right size array
          for (int i = 0; i < attributes; i++){  columnSizes[i] = max(columnSizes[i],separated[i].length());}
          totalLines++;
        }
        lineAtScanner.close();

        int counter = 1;

        Table recordArray = new Table();
        recordArray.setName(name);

        Scanner basicScanner = new Scanner(inputFile);

        //First Line (Headers)
        String headers = basicScanner.nextLine();
        String[] headerArray = headers.split(",");


      //Adding attributes to the table
      for (int i = 0; i < attributes; i++){
        columnSizes[i] = max(columnSizes[i],headerArray[i].length());
        DataPoint attribute = new DataPoint();
        attribute.setVarType("varchar(" + columnSizes[i] + ")");
        attribute.setName(headerArray[i]);
        attribute.setValue("''");
        recordArray.addAttribute(attribute);
      }
      DataBase access = new DataBase();
      access.addTable(recordArray);

        //Making an entity out of each row and adding it to the table
        while (counter < totalLines) {

          String input = basicScanner.nextLine();

          separated = input.split(",");
          Entity cell = new Entity();
          for (int i = 0; i < attributes; i++){//Populating entity
            DataPoint attribute = new DataPoint();
            attribute.setVarType("String");
            attribute.setName(headerArray[i]);
            attribute.setValue("'" + separated[i] + "'");
            cell.addAttribute(attribute);
            columnSizes[i] = max(columnSizes[i],separated[i].length());
          }
          //recordArray.addEntity(cell);
          access.addEntity(cell,name);
          counter++;
        }
        basicScanner.close();
        setRecordArray(recordArray);

        } catch (FileNotFoundException err) {
        System.out.println("You need to pass in a file");
        }

        }
        public void setRecordArray(Table table){

            DataBase access = new DataBase();
            this.recordArray = access.getWholeTable(table.getName());
        }
        public Table getRecordArray(){
            return this.recordArray;
        }

        }
