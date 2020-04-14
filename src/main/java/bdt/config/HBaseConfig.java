package bdt.config;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseConfig.class);

	public static final String TABLE_NAME = "corona_cases";
	public static final String COLUMN_FAMILY = "cc";

	public static final String COL_STATE = "state";
	public static final String COL_COUNTRY = "country";
	public static final String COL_DATE = "date";
	public static final String COL_CONFIRMED_CASES = "confirmedCases";
	public static final String COL_DEATH_CASES = "deathCases";
	public static final String COL_RECOVERED_CASES = "recoveredCases";
	
	public static final String CATALOG = "{\n" + 
			"    \"table\":{\"namespace\":\"default\",\"name\":\"corona_cases\"},\n" + 
			"    \"rowkey\":\"key\",\n" + 
			"    \"columns\":{\n" + 
			"        \"key\":{\"cf\":\"rowkey\",\"col\":\"key\",\"type\":\"string\"},\n" + 
			"        \"country\":{\"cf\":\"cc\",\"col\":\"country\",\"type\":\"string\"},\n" + 
			"        \"state\":{\"cf\":\"cc\",\"col\":\"state\",\"type\":\"string\"},\n" + 
			"        \"confirmedCases\":{\"cf\":\"cc\",\"col\":\"confirmedCases\",\"type\":\"int\"},\n" + 
			"        \"deathCases\":{\"cf\":\"cc\",\"col\":\"deathCases\",\"type\":\"string\"},\n" + 
			"        \"recoveredCases\":{\"cf\":\"cc\",\"col\":\"recoveredCases\",\"type\":\"string\"},\n" + 
			"        \"date\":{\"cf\":\"cc\",\"col\":\"date\",\"type\":\"string\"}\n" + 
			"    }\n" + 
			"}";
	
	private static Connection connection;
	
	public static Connection getHBaseConnection() {
		if (connection == null) {
			try {
				Configuration config = HBaseConfiguration.create();
				connection = ConnectionFactory.createConnection(config);
			} catch (IOException e) {
				LOGGER.error("Cannot create HBase connection. " + e);
				System.exit(0);
			}
		}
		
		return connection;
	}
}
