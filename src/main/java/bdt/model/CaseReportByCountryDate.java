package bdt.model;

import java.io.Serializable;

/**
 * Corona Record
 * 
 * @author khanhnguyen
 *
 */
public class CaseReportByCountryDate extends CaseReport implements Serializable {
	private static final long serialVersionUID = 1L;
	private String country;
	private String date;
	private long count;

	public CaseReportByCountryDate() {
	}

	public CaseReportByCountryDate(String country, String date, long count) {
		this.country = country;
		this.count = count;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(country)
				.append(",")
				.append(date)
				.append(",")
				.append(count)
				.toString();
	}

}
