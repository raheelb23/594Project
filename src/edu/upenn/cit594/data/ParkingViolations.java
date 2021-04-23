package edu.upenn.cit594.data;

public class ParkingViolations {

	private String time;
	private String fine;
	private String description;
	private String vehicleID;
	private String state;
	private String violationID;
	private String ZIPCode;
	
	public ParkingViolations(String time, String fine, String description, String vehicleID, String state, String violationID, String ZIPCode) {
		this.time = time;
		this.fine = fine;
		this.description = description;
		this.vehicleID = vehicleID;
		this.state = state;
		this.violationID = violationID;
		this.ZIPCode = ZIPCode;
	}
	
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @return the fine
	 */
	public String getFine() {
		return fine;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @return the vehicleID
	 */
	public String getVehicleID() {
		return vehicleID;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @return the violationID
	 */
	public String getViolationID() {
		return violationID;
	}
	/**
	 * @return the zIPCode
	 */
	public String getZIPCode() {
		return ZIPCode;
	}
	
	
}
