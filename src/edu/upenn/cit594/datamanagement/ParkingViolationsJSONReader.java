package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.upenn.cit594.data.ParkingViolations;
import edu.upenn.cit594.ui.ErrorCheckerPrinter;

/**
 * This class reads parking violations data provided in json format. 
 * Note: the calculations in processor will filter out any unusable entries. 
 * @author Muizz Mullani and Raheel Bhimani
 * 
 */
public class ParkingViolationsJSONReader implements ParkingViolationsReader {

	protected String fileName;

	/**
	 * Constructor that stores the filename to be read
	 * @param fileName
	 */
	public ParkingViolationsJSONReader(String fileName) {
		this.fileName = fileName;
	}

	@Override
	/**
	 * Returns a list of parking violations as read from csv file
	 */
	public List<ParkingViolations> getParkingViolations() {

		List<ParkingViolations> parkingViolations = new ArrayList<ParkingViolations>();
		BufferedReader readJSONInputFile = null;

		try {
			File file = new File(fileName);
			if (file.canRead()) {
				readJSONInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				ErrorCheckerPrinter.printFileReadError(); //prints the error
				//returns empty list to trigger program exit
				return new ArrayList<ParkingViolations>();
			}
		} catch (FileNotFoundException e) {
			ErrorCheckerPrinter.printFileDoesNotExistError(); //prints the error
			//returns empty list to trigger program exit
			return new ArrayList<ParkingViolations>();
		}

		// parse through information brought in by file
		JSONParser jParser = new JSONParser();
		JSONArray jParserParkingViolations = null;
		try {
			jParserParkingViolations = (JSONArray) jParser.parse(readJSONInputFile);

			Iterator iter = jParserParkingViolations.iterator();

			while (iter.hasNext()) {
				JSONObject jParserViolation = (JSONObject) iter.next();
				String time = jParserViolation.get("date").toString();
				String fine = jParserViolation.get("fine").toString();
				String description = jParserViolation.get("violation").toString();
				String vehicleID = jParserViolation.get("plate_id").toString();
				String state = jParserViolation.get("state").toString();
				String violationID = jParserViolation.get("ticket_number").toString();
				String ZIPCode = jParserViolation.get("zip_code").toString();

				parkingViolations
						.add(new ParkingViolations(time, fine, description, vehicleID, state, violationID, ZIPCode));

			}
		} catch (Exception e) {
			ErrorCheckerPrinter.printFileReadError(); //prints the error
			//returns empty list to trigger program exit
			return new ArrayList<ParkingViolations>();
		}

		return parkingViolations;

	}

}
