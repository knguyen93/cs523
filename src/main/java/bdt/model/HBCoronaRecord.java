package bdt.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Corona Record
 * 
 * @author khanhnguyen
 *
 */
public class HBCoronaRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private String state;
	private String country;
	private String date;
	private int confirmedCases;
	private int deathCases;
	private int recoveredCases;

	public HBCoronaRecord() {
	}

	public HBCoronaRecord(String state, String country, String date, int confirmedCases, int deathCases,
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

	public String getDate() {
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
	
	public void setState(String state) {
		this.state = state;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setConfirmedCases(int confirmedCases) {
		this.confirmedCases = confirmedCases;
	}

	public void setDeathCases(int deathCases) {
		this.deathCases = deathCases;
	}

	public void setRecoveredCases(int recoveredCases) {
		this.recoveredCases = recoveredCases;
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
		HBCoronaRecord other = (HBCoronaRecord) obj;
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
}
