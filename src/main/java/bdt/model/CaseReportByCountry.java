package bdt.model;

import java.io.Serializable;

/**
 * Corona Record
 * 
 * @author khanhnguyen
 *
 */
public class CaseReportByCountry implements Serializable, CaseReport {
	private static final long serialVersionUID = 1L;
	private String country;
	private String date;
	private long count;

	public CaseReportByCountry() {
	}

	public CaseReportByCountry(String country, long count, String date) {
		this.country = country;
		this.count = count;
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
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "CaseReportByCountry [country=" + country + ", count=" + count + "]";
	}

}
