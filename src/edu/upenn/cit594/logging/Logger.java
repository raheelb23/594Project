package edu.upenn.cit594.logging;

import java.io.File;
import java.io.PrintWriter;

import edu.upenn.cit594.ui.ErrorCheckerPrinter;

/**
 * This class implements Singleton design to create a logging function,
 * which stores user provided inputs with time stamps.  
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class Logger {

	private PrintWriter out;
	private static String filename;
	
	/**
	 * private Singleton constructor to create logger 
	 * @param filename
	 */
	private Logger(String filename) {
		try {
			out = new PrintWriter(new File(filename));
		}
		catch (Exception e) {
			ErrorCheckerPrinter.printFileDoesNotExistError(); //print error
			//Initiate graceful exit sequence
			ErrorCheckerPrinter.exitProgram = true;
		}
		
	}
	
	/**
	 * create a null Singleton logger object
	 */
	private static Logger logger = null;
	
	/**
	 * instantiates and returns single instance of Logger
	 * @return
	 */
	public static Logger getInstance() {
		return logger = new Logger(getFilename());
	}
	
	/**
	 * Allows user input to provide filename
	 * @param file
	 */
	public static void setFilename(String file) {
		filename = file;
	}
	
	/**
	 * Allows user provided filename to be returned for use in single instance
	 * @return
	 */
	public static String getFilename() {
		return filename;
	}
	
	/**
	 * Method takes in a String output and writes to logging file 
	 * @param output
	 */
	public void logString(String output) {
		
		out.println(System.currentTimeMillis() + " " + output);
		out.flush();
		
	}
	
	/**
	 * Method takes in an array of String and writes to logging file
	 * @param output
	 */
	public void logStringArray(String[] output) {
		
		out.print(System.currentTimeMillis() + " ");
				
		for(String element : output) {
			out.print(element + " ");
		}
		out.println();
		out.flush();
		
	}
}
