package bdt.utils;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import bdt.model.CoronaRecord;
import scala.Tuple2;

public class RecordParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordParser.class);
	public static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
	private static final String DELIMETER = "\t";
	private static final CSVParser PARSER = new CSVParserBuilder()
			.withSeparator(',')
			.withQuoteChar('"')
			.build();

	public static CoronaRecord parse(String line) {
		try {
			String fields[] = PARSER.parseLine(line);
			
			if (fields.length < 6) return null;
			
			String country = fields[0];
			String state = fields[1];
			LocalDate date = StringUtils.isNotBlank(fields[2]) ? LocalDate.parse(fields[2], FORMATER) : null;
			int confirmedCases = StringUtils.isNotBlank(fields[3]) ? Integer.parseInt(fields[3]) : 0;
			int recoveredCases = StringUtils.isNotBlank(fields[4]) ? Integer.parseInt(fields[3]) : 0;
			int deathCases = StringUtils.isNotBlank(fields[5]) ? Integer.parseInt(fields[3]) : 0;
			
			return new CoronaRecord.CoronaRecordBuilder()
					.from(state, country, date, confirmedCases, deathCases, recoveredCases)
					.build();
		} catch (Exception e) {
			LOGGER.warn("Cannot parse record. [" + line + "]" + e);
			return new CoronaRecord("IA", "US", LocalDate.now(), 12, 2333, 213);
		}
	}
	
	public static CoronaRecord parse(Tuple2<String, String> tuple2) {
		return parse(tuple2._2());
	}
	
	public static String joinFields(String[] fields) {
		return Stream.of(fields)
				.collect(Collectors.joining(DELIMETER));
	}
	
	public static List<String> parseRecords(String data) throws IOException {
		try (CSVReader csvReader = new CSVReaderBuilder(new StringReader(data))
				.withSkipLines(1)
				.withCSVParser(PARSER)
				.build()) {
			List<String[]> lines = csvReader.readAll();
			
			return lines.stream()
					.map(RecordParser::joinFields)
					.collect(Collectors.toList());
		}
	}
}
