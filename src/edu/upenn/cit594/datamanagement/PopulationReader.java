package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

import edu.upenn.cit594.ui.ErrorCheckerPrinter;

/**
 * This class reads the population data from a specified file. 
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class PopulationReader {
	
	public static String fileName;
	
	/**
	 * This method returns a map as it reads in the population data
	 * @return
	 */
	public Map<String, String> getPopulationData(){
		
		TreeMap<String, String> populationData = new TreeMap<>();

		// read in file provided by user
		BufferedReader readPopulationInputFile = null;

		try {
			File file = new File(fileName);
			if (file.canRead()) {
				readPopulationInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				ErrorCheckerPrinter.printFileReadError(); //prints the error
				//returns empty map to trigger program exit
				return new TreeMap<String, String>();
			}
		} catch (FileNotFoundException e) {
			ErrorCheckerPrinter.printFileDoesNotExistError(); //prints the error
			//returns empty map to trigger program exit
			return new TreeMap<String, String>();
		}
		
		String temp; 
		
		try {
			while ((temp = readPopulationInputFile.readLine()) != null) {
				String[] popArray = temp.split(" ");
				populationData.put(popArray[0], popArray[1]);
			}
		} catch (Exception e) {
			ErrorCheckerPrinter.printFileReadError(); //prints the error
			//returns empty map to trigger program exit
			return new TreeMap<String, String>();
		}

		
		return populationData;
	}

}
