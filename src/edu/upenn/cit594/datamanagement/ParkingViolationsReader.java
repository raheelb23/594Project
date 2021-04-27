package edu.upenn.cit594.datamanagement;

import java.util.List;

import edu.upenn.cit594.data.ParkingViolations;
import edu.upenn.cit594.logging.Logger;

/**
 * The purpose of this interface is to provide flexibility in the implementation
 * of the reader classes and the processor classes.
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public interface ParkingViolationsReader {
	
	/**
	 * This returns a list of Parking Violations
	 * @return
	 */
	public List<ParkingViolations> getParkingViolations(Logger logging);
		

}
