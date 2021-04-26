package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import edu.upenn.cit594.data.ParkingViolations;
import edu.upenn.cit594.ui.ErrorCheckerPrinter;


public class PopulationReader {
	
	public static String fileName;
	
	public Map<String, String> getPopulationData(){
		
		TreeMap<String, String> populationData = new TreeMap<>();

		// read in file provided by user
		BufferedReader readPopulationInputFile = null;

		try {
			File file = new File(fileName);
			if (file.canRead()) {
				readPopulationInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				ErrorCheckerPrinter.printFileReadError();
				return new TreeMap<String, String>();
			}
		} catch (FileNotFoundException e) {
			ErrorCheckerPrinter.printFileDoesNotExistError();
			return new TreeMap<String, String>();
		}
		
		String temp; 
		
		try {
			while ((temp = readPopulationInputFile.readLine()) != null) {
				String[] popArray = temp.split(" ");
				populationData.put(popArray[0], popArray[1]);
			}
		} catch (Exception e) {
			ErrorCheckerPrinter.printFileReadError();
			return new TreeMap<String, String>();
		}

		
		return populationData;
	}

}
