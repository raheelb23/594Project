package edu.upenn.cit594.datamanagement;

import java.util.List;
import java.util.Map;

import edu.upenn.cit594.data.ParkingViolations;

/**
 * The purpose of this interface is to provide flexibility in the implementation
 * of the reader classes and the processor classes.
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public interface ParkingViolationsReader {
	
	/**
	 * This returns a map of Parking Violations
	 * @return
	 */
	public Map<String, List<ParkingViolations>> getParkingViolations();
		

}
