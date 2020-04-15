package bdt.kafka;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
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
import bdt.sparksql.CoronaAnalysisApp;
import bdt.utils.RecordParser;
import kafka.serializer.StringDecoder;
import lombok.extern.log4j.Log4j;

@Log4j
public class KConsumer {

	public static void startConsumer() throws InterruptedException {
		JavaSparkContext sc = SparkConfig.getSparkContext();
		Map<String, String> kafkaParams = KafkaConfig.generateKafkaParams();
		Set<String> topicName = Collections.singleton(KafkaConfig.TOPIC_NAME);
		Configuration hadoopConf = sc.hadoopConfiguration();
		HBaseRepository repo = HBaseRepository.getInstance();

		try (JavaStreamingContext streamingContext = new JavaStreamingContext(sc, new Duration(5000))) {
			JavaPairInputDStream<String, String> kafkaSparkPairInputDStream = KafkaUtils.createDirectStream(
					streamingContext, String.class,
					String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topicName);
			
			JavaDStream<CoronaRecord> recoredRDDs = kafkaSparkPairInputDStream
					.map(RecordParser::parse)
					.filter(record -> record != null);
			
			recoredRDDs.foreachRDD(rdd -> {
				if (!rdd.isEmpty()) {
					log.info("=========================== RECEIVED LINE : [[[[" + rdd + "]]]]");
					log.info("=========================== RECEIVED LINE : [[[[" + rdd.first() + "]]]]");
					log.info("=========================== RECEIVED LINE getCountry: [[[[" + rdd.first().getCountry() + "]]]]");
					log.info("=========================== RECEIVED LINE getCountry: [[[[" + rdd.first().toString() + "]]]]");
					String line = rdd.toString();
					if (line != null && line.length() > 2 && line.length() < 10) {
						log.info("=========================== RECEIVED TOKEN : " + line);
					}
					if ("$$$".equals(line)) {
						CoronaAnalysisApp.init();
						CoronaAnalysisApp.generateTotalCasesPilot();
					} else {
						repo.save(hadoopConf, rdd);
					}
				}
			});

			streamingContext.start();
			streamingContext.awaitTermination();
		}
	}
}
