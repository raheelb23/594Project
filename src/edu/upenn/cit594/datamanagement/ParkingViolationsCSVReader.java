package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.data.ParkingViolations;

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
				System.out.println("Please re-check your csv input file.");
				// ErrorHandling.fileNotFound();
				// add later
			}
		} catch (FileNotFoundException e) {
			// ErrorHandling.fileNotFound();
			// add later
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
				String ZIPCode = violationArray[6];

				parkingViolations.add(new ParkingViolations(time, fine, description, vehicleID, state, violationID, ZIPCode));
			}

		} catch (Exception e) {
			System.out.println("Please re-check your csv input file.");
			// ErrorHandling.ioException();
			// add later
		}

		return parkingViolations;
	}

}
