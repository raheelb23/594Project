package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.upenn.cit594.data.ParkingViolations;

public class ParkingViolationsJSONReader implements ParkingViolationsReader {

	protected String fileName;

	public ParkingViolationsJSONReader(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public List<ParkingViolations> getParkingViolations() {
		
		List<ParkingViolations> parkingViolations = new ArrayList<ParkingViolations>();
		BufferedReader readJSONInputFile = null;
		
		try {
			File file = new File(fileName);
			if (file.canRead()) {
				readJSONInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				System.out.println("Please re-check your json input file.");
				//ErrorHandling.fileNotFound();
				//add later
			}
		} catch (FileNotFoundException e) {
			//ErrorHandling.fileNotFound();
			//add later
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

						parkingViolations.add(new ParkingViolations(time, fine, description, vehicleID, state, violationID, ZIPCode));

					}
				} catch (IOException e) {
					e.printStackTrace();
					//ErrorHandling.ioException();
					//add later
				} catch (ParseException e) {
					e.printStackTrace();
					//ErrorHandling.parseException();
					//add later
				}

				return parkingViolations;

}

}
