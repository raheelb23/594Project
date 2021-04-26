package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.ParkingViolationsCSVReader;
import edu.upenn.cit594.datamanagement.ParkingViolationsJSONReader;
import edu.upenn.cit594.datamanagement.ParkingViolationsReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertyValuesCSVReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.MainProcessor;
import edu.upenn.cit594.ui.ErrorCheckerPrinter;
import edu.upenn.cit594.ui.UserInterface;

/**
 * This class sets-up the project to run by coordinating amongst 
 * the UI, Processor, Data, and Data Management layers.
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class MainProgram {
	
	/**
	 * Main method to run the entire program
	 * @param args
	 */
	public static void main(String[] args) {
		
		ErrorCheckerPrinter.errorChecker(args);
		
		if(ErrorCheckerPrinter.exitProgram) return;
		
		//Set-up reader depending on type of input
		ParkingViolationsReader violationsReader = null;
		if(args[0].equalsIgnoreCase("csv")) {
			violationsReader = new ParkingViolationsCSVReader(args[1]);
		}
		else if(args[0].equalsIgnoreCase("json")) {
			violationsReader = new ParkingViolationsJSONReader(args[1]);
		}
		
		//Set-up remaining readers' and logger filenames
		PropertyValuesCSVReader.fileName = args[2];
		PopulationReader.fileName = args[3];
		Logger.setFilename(args[4]);
		
		//Pass in violationsReader to MainProcessor
		MainProcessor mainProcessor = new MainProcessor(violationsReader);
		
		if(ErrorCheckerPrinter.exitProgram) return;
		
		//Pass mainProcessor to UserInterface and begin program
		UserInterface interactive = new UserInterface(mainProcessor);
		interactive.start(args);
	}

}
