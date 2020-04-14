package bdt.sparksql;

import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bdt.config.HBaseConfig;
import bdt.config.SparkConfig;
import bdt.hbase.HBaseRepository;
import bdt.model.CoronaRecord;
import bdt.model.HBCoronaRecord;

public class CoronaAnalysisApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoronaAnalysisApp.class);
	private static SparkSession sparkSession;
	
	public static void init() throws IOException {
		LOGGER.info("================== INTIAL APPLICATION DATA ====================");
		System.out.println("================== INTIAL APPLICATION DATA ====================");
		HBaseRepository db = HBaseRepository.getInstance();
		sparkSession = SparkSession.builder()
				.appName("Spark SQL")
				.master(SparkConfig.MASTER_LOCAL)
				.getOrCreate();
		
		sparkSession.createDataFrame(db.scanRecords(), HBCoronaRecord.class)
		.createOrReplaceTempView(HBaseConfig.TABLE_NAME);
	}

	public static void printTotalCasesByDate() {
		String query =  " SELECT date, COUNT(*) FROM " + HBaseConfig.TABLE_NAME 
					  + " GROUP BY date"
					  + " ORDER BY date DESC ";
		
		Dataset<Row> sqlDF = sparkSession.sql(query);
		sqlDF.show();
	}
	
	public static void printTotalCasesByCountry() {
		String query =  " SELECT country, COUNT(*) FROM " + HBaseConfig.TABLE_NAME 
				  + " GROUP BY country"
				  + " ORDER BY country DESC ";
		
		Dataset<Row> sqlDF = sparkSession.sql(query);
		sqlDF.show();
	}
	
	public static void printCasesForCountryByDates() {
		String query =  " SELECT country, date, COUNT(*) FROM " + HBaseConfig.TABLE_NAME 
				  + " GROUP BY country, date"
				  + " ORDER BY country DESC, date DESC ";
		
		Dataset<Row> sqlDF = sparkSession.sql(query);
		sqlDF.show();
	}
	
	public static void printCustomQuery(String queryStr) {
		Dataset<Row> sqlDF = sparkSession.sql(queryStr);
		sqlDF.show();
	}
	
	private static void printMenu() {
		System.out.println("======================================");
		System.out.println("Please select program:");
		System.out.println("1. Show total Cases by Date");
		System.out.println("2. Show total Cases by Country");
		System.out.println("3. Show total Cases by Country and Date");
		System.out.println("4. Enter custom query");
		System.out.println("Type 'exit' to stop program.");
		System.out.println("Your option: ");
	}
	
	public static void main(String[] args) {
		BasicConfigurator.configure();
		
		try (Scanner scanner = new Scanner(System.in)){
			init();
			
			while (true) {
				printMenu();
				String option = scanner.nextLine();

				switch (option) {
					case "1":
						printTotalCasesByDate();
						break;
					case "2":
						printTotalCasesByCountry();
						break;
					case "3":
						printCasesForCountryByDates();
						break;
					case "4":
						System.out.println("Enter your query: ");
						String queryStr = scanner.nextLine();
						printCustomQuery(queryStr);
						break;
					case "exit":
						System.exit(1);
					default:
				}
			}
			
		} catch (IOException e) {
			LOGGER.info("================== APPLICATION DIE ====================");
			LOGGER.error("An error occur while running CoronaAnalysisApp. " + e);
			System.exit(0);
		}
	}
}
