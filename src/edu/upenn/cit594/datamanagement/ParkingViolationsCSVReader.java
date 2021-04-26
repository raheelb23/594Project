package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.data.ParkingViolations;
import edu.upenn.cit594.ui.ErrorCheckerPrinter;

public class ParkingViolationsCSVReader implements ParkingViolationsReader {

	protected String fileName;

	public ParkingViolationsCSVReader(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<ParkingViolations> getParkingViolations() {

		List<ParkingViolations> parkingViolations = new ArrayList<ParkingViolations>();
		BufferedReader readCSVInputFile = null;

		try {
			File file = new File(fileName);
			if (file.canRead()) {
				readCSVInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				ErrorCheckerPrinter.printFileReadError();
				return new ArrayList<ParkingViolations>();
			}
		} catch (FileNotFoundException e) {
			ErrorCheckerPrinter.printFileDoesNotExistError();
			return new ArrayList<ParkingViolations>();
		}

		try {
			// parse through information brought in by file
			String violation;

			while ((violation = readCSVInputFile.readLine()) != null) {
				String[] violationArray = violation.split(",");
				String time = violationArray[0];
				String fine = violationArray[1];
				String description = violationArray[2];
				String vehicleID = violationArray[3];
				String state = violationArray[4];
				String violationID = violationArray[5];
				String ZIPCode = "";
				if(violationArray.length == 7) ZIPCode = violationArray[6];

				parkingViolations.add(new ParkingViolations(time, fine, description, vehicleID, state, violationID, ZIPCode));
			}

		} catch (Exception e) {
			ErrorCheckerPrinter.printFileReadError();
			return new ArrayList<ParkingViolations>();
		} 

		return parkingViolations;
	}

}
