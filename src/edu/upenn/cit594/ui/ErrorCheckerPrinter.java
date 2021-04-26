package edu.upenn.cit594.ui;

/**
 * This class checks for errors in the initial arguments 
 * provided by user. It also provides functionality to 
 * print file errors and exit the program. 
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class ErrorCheckerPrinter {

	public static boolean exitProgram = false; //allows for graceful exit w/o using System.exit
	
	/**
	 * checks initial list of arguments provided by user
	 * @param args
	 */
	public static void errorChecker(String[] args) {
		if(args.length != 5) {
			System.out.println("Usage: not enough arguments supplied. The program will now exit.");
			exitProgram = true;
		}
		if(args[0].equals("json") == false && args[0].equals("csv") == false) {
			System.out.println("Usage: file format provided is not supported. The program will now exit.");
			exitProgram = true;
		}
	}
	
	public static void printFileReadError() {
		System.out.println("Reader: the specified file cannot be read. The program will now exit.");
		exitProgram = true;
	}
	
	public static void printFileDoesNotExistError() {
		System.out.println("File: one or more of the specified files does not exist. The program will now exit.");
		exitProgram = true;
	}
	
	public static void printUIErrors() {
		System.out.println("User Error: Entry Not Valid. The program will now exit.");
	}
}
