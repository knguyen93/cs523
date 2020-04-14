package bdt.hbase;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bdt.config.HBaseConfig;
import bdt.model.CoronaRecord;
import bdt.model.HBCoronaRecord;
import scala.Tuple2;

public class HBaseRepository implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseRepository.class);
	public static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	public static final DateTimeFormatter FORMATER_2 = DateTimeFormatter.ofPattern("yyyyMMdd");

	private static HBaseRepository INSTANCE;

	private HBaseRepository() {
		init();
	}

	public static HBaseRepository getInstance() {
		if (INSTANCE == null)
			INSTANCE = new HBaseRepository();
		return INSTANCE;
	}

	private void init() {
		try (Admin admin = HBaseConfig.getHBaseConnection().getAdmin()) {
			
			HTableDescriptor table = new HTableDescriptor(TableName.valueOf(HBaseConfig.TABLE_NAME));
			table.addFamily(new HColumnDescriptor(HBaseConfig.COLUMN_FAMILY).setCompressionType(Algorithm.NONE));
			
			if (!admin.tableExists(table.getTableName())) {
				LOGGER.info("Creating table ...");
				admin.createTable(table);
				LOGGER.info("Table created!");
			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
		}
	}

	public CoronaRecord get(Connection connection, String key) throws IOException {
		try (Table tb = connection.getTable(TableName.valueOf(HBaseConfig.TABLE_NAME))) {
			Get g = new Get(Bytes.toBytes(key));
			return parseResult(tb.get(g));
		}
	}
	
	public void save(Configuration config, JavaRDD<CoronaRecord> record) throws MasterNotRunningException, Exception {
		Job job = Job.getInstance(config);
		job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, HBaseConfig.TABLE_NAME);
		job.setOutputFormatClass(TableOutputFormat.class);
		JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = record.mapToPair(new MyPair());
		hbasePuts.saveAsNewAPIHadoopDataset(job.getConfiguration());
	}
	
	public List<HBCoronaRecord> scanRecords() throws IOException {
		List<HBCoronaRecord> records = new ArrayList<>();
		if (!isTableExist(HBaseConfig.TABLE_NAME)) return records;
		LOGGER.info("================== SCANNING DATA=====================");
		System.out.println("================== SCANNING DATA=====================");
		Scan s = new Scan(); HBCoronaRecord record;
		try (Table table = HBaseConfig.getHBaseConnection().getTable(TableName.valueOf(HBaseConfig.TABLE_NAME)); 
				ResultScanner scanner = table.getScanner(s)) {
			Result result = scanner.next();
			System.out.println("================================== DEBBUG ====== " + result.toString());
			while(result != null) {
				record = parseResultHB(result);
				if (record != null) {
					records.add(record);
				}
				result = scanner.next();
			}
		}
		LOGGER.info("================== SCANNING DATA DONE !!! =====================");
		LOGGER.info("================== TOTAL DATA =====================: " + records.size());
		System.out.println("================== SCANNING DATA DONE !!! =====================");
		System.out.println("================== TOTAL DATA =====================: " + records.size());
		return records;
	}
	
	private CoronaRecord parseResult(Result result) {
		if (result.isEmpty()) {
			return null;
		}

		byte[] country = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_COUNTRY);
		byte[] state = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_STATE);
		byte[] date = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DATE);
		byte[] confirmedCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_CONFIRMED_CASES);
		byte[] recoveredCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_RECOVERED_CASES);
		byte[] deathCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DEATH_CASES);

		return new CoronaRecord(state.toString(), country.toString(), LocalDate.parse(date.toString(), FORMATER),
						Bytes.toInt(confirmedCases), Bytes.toInt(deathCases), Bytes.toInt(recoveredCases));
	}
	
	private HBCoronaRecord parseResultHB(Result result) {
		if (result.isEmpty()) {
			return null;
		}

		byte[] country = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_COUNTRY);
		byte[] state = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_STATE);
		byte[] date = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DATE);
		byte[] confirmedCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_CONFIRMED_CASES);
		byte[] recoveredCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_RECOVERED_CASES);
		byte[] deathCases = getValue(result, HBaseConfig.COLUMN_FAMILY, HBaseConfig.COL_DEATH_CASES);

		return new HBCoronaRecord(state.toString(), country.toString(), date.toString(),
						Bytes.toInt(confirmedCases), Bytes.toInt(deathCases), Bytes.toInt(recoveredCases));
	}

	private Put generatePut(String rowKey, CoronaRecord record) {
		Put put = new Put(Bytes.toBytes(rowKey));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_COUNTRY.getBytes(), parseValue(record.getCountry()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_STATE.getBytes(), parseValue(record.getState()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_DATE.getBytes(), parseValue(record.getDate()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_CONFIRMED_CASES.getBytes(), parseValue(record.getConfirmedCases()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_RECOVERED_CASES.getBytes(), parseValue(record.getRecoveredCases()));
		put.addImmutable(HBaseConfig.COLUMN_FAMILY.getBytes(), HBaseConfig.COL_DEATH_CASES.getBytes(), parseValue(record.getDeathCases()));
		return put;
	}
	
	private byte[] getValue(Result result, String columnFamily, String columnName) {
		return result.getValue(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));
	}
	
	private byte[] parseValue(String value) {
		return Optional.ofNullable(value).orElse("").getBytes();
	}
	
	private byte[] parseValue(int value) {
		return Bytes.toBytes(value);
	}
	
	private byte[] parseValue(LocalDate value) {
		return Optional.ofNullable(value).map(v -> v.format(FORMATER)).orElse("").getBytes();
	}
	
	private boolean isTableExist(String tableName) throws IOException {
		try (Admin admin = HBaseConfig.getHBaseConnection().getAdmin()) {
			return admin.tableExists(TableName.valueOf(tableName));
		}
	}
	
	class MyPair implements PairFunction<CoronaRecord, ImmutableBytesWritable, Put> {
		private static final long serialVersionUID = 1L;

		@Override
		public Tuple2<ImmutableBytesWritable, Put> call(CoronaRecord record) throws Exception {
			String date = record.getDate() != null ? record.getDate().format(FORMATER_2) : "";
			String key = Stream.of(record.getCountry(), record.getState(), date)
					.filter(StringUtils::isNotBlank)
					.map(v -> v.replaceAll("\\s+", ""))
					.collect(Collectors.joining("|"));
					Put put = generatePut(key, record);
					
					return new Tuple2<ImmutableBytesWritable, Put>(new ImmutableBytesWritable(), put);
		}
		
	};
}
