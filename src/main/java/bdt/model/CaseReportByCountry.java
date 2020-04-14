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
	private int count;

	public CaseReportByCountry() {
	}

	public CaseReportByCountry(String country, int count, String date) {
		this.country = country;
		this.count = count;
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
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
