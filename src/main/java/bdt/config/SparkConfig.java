package bdt.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkConfig {
	public static final String APP_NAME = "CoronaSpark";
	public static final String MASTER_LOCAL = "local";

	private static final SparkConf sparkConf = new SparkConf()
			.setAppName(APP_NAME)
			.setMaster(MASTER_LOCAL);
	private static final JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
	
	public static JavaSparkContext getSparkContext() {
		return sparkContext;
	}
}
