package entity;
import java.io.Serializable;
public class Usage implements Serializable{
	private String studentID;
	private int date;
	private int dayUsage;
	
	public Usage(String studentID, int date, int dayUsage) {
		this.studentID = studentID;
		this.date = date;
		this.dayUsage = dayUsage;
	}

	/**
	 * @return the studentID
	 */
	public String getStudentID() {
		return studentID;
	}

	/**
	 * @param studentID the studentID to set
	 */
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	/**
	 * @return the date
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(int date) {
		this.date = date;
	}

	/**
	 * @return the dayUsage
	 */
	public int getDayUsage() {
		return dayUsage;
	}

	/**
	 * @param dayUsage the dayUsage to set
	 */
	public void setDayUsage(int dayUsage) {
		this.dayUsage = dayUsage;
	}
	
}
