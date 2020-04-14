package bdt.model;

import java.io.Serializable;

/**
 * Corona Record
 * 
 * @author khanhnguyen
 *
 */
public class CaseReportByDate  extends CaseReport implements Serializable {
	private static final long serialVersionUID = 1L;
	private String date;
	private long count;

	public CaseReportByDate() {
	}

	public CaseReportByDate(String date, long count) {
		this.date = date;
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(date)
				.append(",")
				.append(count)
				.toString();
	}
}
