package omniChem.core;

import omniChem.core.converter.DataConverter;
import omniChem.core.dataSetCore.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class CoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
		//Make DB, this is just here to test passing datasets into a DB
		//Create table
		//Pass table to DB
		DataBase dataBaseTest = new DataBase();
		dataBaseTest.addTable(DBTestData.GetTestData());
		DataConverter converter = new DataConverter("src/main/resources/names_smiles.csv","testName");
		Table testTable = converter.getRecordArray();
		List<String> tables = dataBaseTest.getTableNames();
		for(String i : tables) {
			System.out.print("\n\n"+i);
			Table_Columns col =  dataBaseTest.getTableHeaders(i);
			for(String j : col.getNames()) {
				System.out.print("\n"+j);
			}
			for(String j : col.getTypes()) {
				System.out.print("\n"+j);
			}
		}


	}


}
