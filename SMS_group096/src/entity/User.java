package entity;

import java.util.Calendar;
import java.io.Serializable;

public class User implements Serializable{
	private String studentID;
	private String email;
	private String name;
	private int status;//2 means no using. 1 means using.
	private Calendar cld;
	private int[] dayUsingTime;
	private boolean fine;

	public User(String id, String email, String name) {
		this.email = email;
		this.name = name;
		this.studentID = id;
		this.dayUsingTime= new int[2];
		this.dayUsingTime[0]=0;
		this.dayUsingTime[1]=0;
		this.fine=false;
		this.status=2;
	}

	/**
	 * @return the dayUsingTime
	 */
	public int[] getDayUsingTime() {
		return dayUsingTime;
	}

	/**
	 * @param dayUsingTime the dayUsingTime to set
	 */
	public void setDayUsingTime(int date, int dayTime) {
		this.dayUsingTime[0]=date;
		this.dayUsingTime[1]=dayTime;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * @return the cld
	 */
	public Calendar getCld() {
		return cld;
	}

	/**
	 * @param cld the cld to set
	 */
	public void setCld(Calendar cld) {
		this.cld = cld;
	}

	/**
	 * @return the fine
	 */
	public boolean isFine() {
		return fine;
	}

	/**
	 * @param fine the fine to set
	 */
	public void setFine(boolean fine) {
		this.fine = fine;
	}
	
	@Override
	public String toString() {
		return "User [studentID=" + studentID + ", email=" + email + ", name=" + name + ", status=" + status + "]";
	}

	
}
