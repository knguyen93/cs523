package bdt.model;

import java.io.Serializable;

/**
 * Corona Record
 * 
 * @author khanhnguyen
 *
 */
public class CaseReportByCountry extends CaseReport implements Serializable {
	private static final long serialVersionUID = 1L;
	private String country;
	private long count;

	public CaseReportByCountry() {
	}

	public CaseReportByCountry(String country, long count) {
		this.country = country;
		this.count = count;
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
				.append(count)
				.toString();
	}

}
