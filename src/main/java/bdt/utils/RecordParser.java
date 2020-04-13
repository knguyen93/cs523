package bdt.utils;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
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
	public static final DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter FORMATER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	private static final String DELIMETER = "\t";
	private static final CSVParser PARSER = new CSVParserBuilder().withSeparator(',').withQuoteChar('"').build();

	public static CoronaRecord parse(String line) {
		try {
			String[] fields = PARSER.parseLine(line);

			if (fields.length < 6)
				return null;

			// Headers: Province/State, Country/Region, Last Update, Confirmed, Deaths, Recovered, Latitude, Longitude

			String state = fields[0];
			String country = fields[1];
			LocalDate date = parseDate(fields[2]);
			int confirmedCases = StringUtils.isNotBlank(fields[3]) ? Integer.parseInt(fields[3]) : 0;
			int deathCases = StringUtils.isNotBlank(fields[4]) ? Integer.parseInt(fields[4]) : 0;
			int recoveredCases = StringUtils.isNotBlank(fields[5]) ? Integer.parseInt(fields[5]) : 0;

			return new CoronaRecord.CoronaRecordBuilder()
					.from(state, country, date, confirmedCases, deathCases, recoveredCases).build();
		} catch (Exception e) {
			LOGGER.warn("Cannot parse record. [" + line + "]" + e);
			return null;
		}
	}

	public static LocalDate parseDate(String value) {
		if (StringUtils.isBlank(value))
			return null;

		try {
			return LocalDate.parse(value, FORMATER);
		} catch (DateTimeParseException e) {
			try {
				return LocalDate.parse(value, FORMATER_2);
			} catch (DateTimeParseException ex) {
				return null;
			}
		}
	}

	public static CoronaRecord parse(Tuple2<String, String> tuple2) {
		return parse(tuple2._2());
	}

	public static String joinFields(String[] fields) {
		return Stream.of(fields).collect(Collectors.joining(DELIMETER));
	}

	public static List<String> parseRecords(String data) throws IOException {
		try (CSVReader csvReader = new CSVReaderBuilder(new StringReader(data)).withSkipLines(1).withCSVParser(PARSER)
				.build()) {
			List<String[]> lines = csvReader.readAll();

			return lines.stream().map(RecordParser::joinFields).collect(Collectors.toList());
		}
	}

	/**
	 * Test parsing record
	 * 
	 * @param args [input file] [skip records] [limit records]
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		try {
			String input = args != null && args.length > 0 ? args[0] : "test.csv";
			int skipRecords = args != null && args.length > 1 ? Integer.parseInt(args[1]) : 1;
			int limitRecords = args != null && args.length > 2 ? Integer.parseInt(args[2]) : 200;

			List<String> lines = Files.readAllLines(Paths.get(input));

			lines.stream().skip(skipRecords).map(RecordParser::parse).limit(limitRecords).map(CoronaRecord::toString)
					.forEach(LOGGER::info);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
