package bdt;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.BasicConfigurator;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import bdt.config.KafkaConfig;
import bdt.config.SparkConfig;
import bdt.hbase.HBaseRepository;
import bdt.model.CoronaRecord;
import bdt.utils.RecordParser;
import kafka.serializer.StringDecoder;

public class Application {

	public static void main(String[] args) throws InterruptedException {
		BasicConfigurator.configure();
		startStreaming();
	}

	private static void startStreaming() throws InterruptedException {
		JavaSparkContext sc = SparkConfig.getSparkContext();
		Map<String, String> kafkaParams = KafkaConfig.generateKafkaParams();
		Set<String> topicName = Collections.singleton(KafkaConfig.TOPIC_NAME);
		Configuration hadoopConf = sc.hadoopConfiguration();
		HBaseRepository repo = HBaseRepository.getInstance();

		try (JavaStreamingContext streamingContext = new JavaStreamingContext(sc, new Duration(5000));) {
//			KafkaUtils
//					.createDirectStream(streamingContext, String.class, String.class, StringDecoder.class,
//							StringDecoder.class, kafkaParams, topicName)
//					.map(RecordParser::parse)
//					.filter(record -> record != null)
//					.foreachRDD(record -> repo.save(hadoopConf, record));

			JavaPairInputDStream<String, String> kafkaSparkPairInputDStream = KafkaUtils.createDirectStream(
					streamingContext, String.class,
					String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topicName);
			
			JavaDStream<CoronaRecord> recoredRDDs = kafkaSparkPairInputDStream.map(RecordParser::parse).filter(record -> record != null);
			recoredRDDs.foreachRDD(rdd -> {
				if (!rdd.isEmpty()) {
					repo.save(hadoopConf, rdd);
				}
			});

			streamingContext.start();
			streamingContext.awaitTermination();
		}
	}
}
