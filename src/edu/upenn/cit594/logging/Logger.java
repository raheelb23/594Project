package edu.upenn.cit594.logging;

//check the imports
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

//import edu.upenn.cit594.ui.ErrorCheckerPrinter;

/**
 * This class implements Singleton design to create a logging function,
 * which stores flu tweets by state in a file specified by the user. 
 * @author muizz.mullani
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
			e.printStackTrace();
			//ErrorCheckerPrinter.printFileDoesNotExistError();
		}
		
	}
	
	/**
	 * create a null logger object
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
	 * Method takes in a map of tweets and writes to logging file which is 
	 * provided by the Main class
	 * @param tweets
	 */
	public void logString(String output) {
		
		out.println(System.currentTimeMillis() + " " + output);
		out.flush();
		
	}
	
	/**
	 * Method takes in a map of tweets and writes to logging file which is 
	 * provided by the Main class
	 * @param tweets
	 */
	public void logStringArray(String[] output) {
		
		out.print(System.currentTimeMillis() + " ");
				
		for(String element : output) {
			out.print(element + " ");
		}
		out.println();
		out.flush();
		
	}
	
	public void close() {
		out.close();
	}
}
