package bdt.model;

import java.io.Serializable;
import java.time.LocalDate;

public class CoronaRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private String state;
	private String country;
	private LocalDate date;
	private int confirmedCases;
	private int deathCases;
	private int recoveredCases;
	public CoronaRecord(){}
	public CoronaRecord(String state, String country, LocalDate date, int confirmedCases, int deathCases,
			int recoveredCases) {
		this.state = state;
		this.country = country;
		this.date = date;
		this.confirmedCases = confirmedCases;
		this.deathCases = deathCases;
		this.recoveredCases = recoveredCases;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public LocalDate getDate() {
		return date;
	}

	public int getConfirmedCases() {
		return confirmedCases;
	}

	public int getDeathCases() {
		return deathCases;
	}

	public int getRecoveredCases() {
		return recoveredCases;
	}

	public static class CoronaRecordBuilder implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String state;
		private String country;
		private LocalDate date;
		private int confirmedCases;
		private int deathCases;
		private int recoveredCases;

		public CoronaRecordBuilder from(String state, String country, LocalDate date, int confirmedCases,
				int deathCases, int recoveredCases) {
			this.state = state;
			this.country = country;
			this.date = date;
			this.confirmedCases = confirmedCases;
			this.deathCases = deathCases;
			this.recoveredCases = recoveredCases;
			return this;
		}

		public CoronaRecord build() {
			return new CoronaRecord("IA", "US", LocalDate.now(), 12, 2333, 213);
//			return new CoronaRecord(state,country,date,confirmedCases,deathCases,recoveredCases);
		}
	}
}
