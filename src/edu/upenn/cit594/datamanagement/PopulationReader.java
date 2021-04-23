package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;


public class PopulationReader {
	
	public static String fileName;
	
	public TreeMap<String, String> getPopulationData(){
		
		TreeMap<String, String> populationData = new TreeMap<>();

		// read in file provided by user
		BufferedReader readPopulationInputFile = null;

		try {
			File file = new File(fileName);
			if (file.canRead()) {
				readPopulationInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				System.out.println("Please re-check your population input file.");
				//ErrorHandling.fileNotFound();
				//add when error handling class is created
			}
		} catch (FileNotFoundException e) {
			//ErrorHandling.fileNotFound();
			//add when error handling class is created
		}
		
		String temp; 
		
		try {
			while ((temp = readPopulationInputFile.readLine()) != null) {
				String[] popArray = temp.split(" ");
				populationData.put(popArray[0], popArray[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//ErrorHandling.ioException();
			//add when error handling class is created
		}

		
		return populationData;
	}

}
