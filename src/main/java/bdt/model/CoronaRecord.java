package bdt.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Corona Record
 * 
 * @author khanhnguyen
 *
 */
public class CoronaRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private String state;
	private String country;
	private LocalDate date;
	private int confirmedCases;
	private int deathCases;
	private int recoveredCases;

	private CoronaRecord() {
	}

	private CoronaRecord(String state, String country, LocalDate date, int confirmedCases, int deathCases,
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + confirmedCases;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoronaRecord other = (CoronaRecord) obj;
		if (confirmedCases != other.confirmedCases)
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CoronaRecord [state=" + state + ", country=" + country + ", date=" + date + ", confirmedCases="
				+ confirmedCases + ", deathCases=" + deathCases + ", recoveredCases=" + recoveredCases + "]";
	}

	/**
	 * Corona Record Builder
	 * 
	 * @author khanhnguyen
	 *
	 */
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
			return new CoronaRecord(state,country,date,confirmedCases,deathCases,recoveredCases);
		}

		public CoronaRecordBuilder state(String state) {
			this.state = state;
			return this;
		}

		public CoronaRecordBuilder country(String country) {
			this.country = country;
			return this;
		}

		public CoronaRecordBuilder date(LocalDate date) {
			this.date = date;
			return this;
		}

		public CoronaRecordBuilder confirmedCases(int confirmedCases) {
			this.confirmedCases = confirmedCases;
			return this;
		}

		public CoronaRecordBuilder deathCases(int deathCases) {
			this.deathCases = deathCases;
			return this;
		}

		public CoronaRecordBuilder recoveredCases(int recoveredCases) {
			this.recoveredCases = recoveredCases;
			return this;
		}
	}
}
