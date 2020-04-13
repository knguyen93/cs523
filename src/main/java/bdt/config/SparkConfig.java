package bdt.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkConfig {
	public static final String APP_NAME = "CoronaSpark";
	public static final String MASTER_LOCAL = "local";

	public static JavaSparkContext getSparkContext() {
		SparkConf sparkConf = new SparkConf()
				.setAppName(APP_NAME)
				.setMaster(MASTER_LOCAL);
		
		return new JavaSparkContext(sparkConf);
	}
}
