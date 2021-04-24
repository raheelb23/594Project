package edu.upenn.cit594.ui;

import java.util.Map;
import java.util.Scanner;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.MainProcessor;

public class UserInterface {
	
	protected MainProcessor processor;
	protected Logger logging;

	public UserInterface(MainProcessor processor) {
		this.processor = processor;
		logging = Logger.getInstance();
	}
	
	private void displayMenu() {
		System.out.println();
		System.out.println("Please select from the following. Enter a number for your choice." + "\n");
		System.out.println("0 - Exit the program");
		System.out.println("1 - Calculate total population for all ZIP Codes");
		System.out.println("2 - Calculate total parking fines per capita for each ZIP Code");
		System.out.println("3 - Calculate average market value for residences in a specified ZIP Code");
		System.out.println("4 - Calculate average total livable area for residences in a specified ZIP Code");
		System.out.println("5 - Calculate total residential market value per capita for a specified ZIP Code");
		System.out.println("6 - ");
		System.out.println();
		System.out.print("Enter your choice: ");
	}
	
	public void start(String[] args) {
		
		//send args to logger
		//get processors going
		//processors will do initial checks and readings
		
		
		boolean exit = false;
		Scanner in = new Scanner(System.in);
		
		while(!exit) {
			displayMenu();
			String line = in.nextLine();
			int choice;
			
			try {
				choice = Integer.parseInt(line);
				//log choice
				
				if(choice == 0) exit = exitSequence();
				else if(choice == 1) choice1();
				else if(choice == 2) choice2();
				else if(choice == 3 || choice == 4 || choice == 5) {
					int ZIP = interim(in);
					if (ZIP == -1) {
						exit = true;
						in.close();
						continue;
					}
					if (choice == 3) choice3(ZIP);
					if (choice == 4) choice4(ZIP);
					if (choice == 5) choice5(ZIP);
					
				}
				else if(choice == 6) choice6();
				else {
					System.out.println("User Input Error");
					exit = exitSequence();
				}
				
			}
			catch (Exception e) {
				//direct to error printing and exit
			}
		}
		in.close();
	}
	
	private boolean exitSequence() {
		System.out.println("The program will now exit. Thank you!");
		return true;
	}
	
	/**
	 * This method displays to the console the total population 
	 * for all of the ZIP Codes in the population input file.
	 */
	private void choice1() {
		System.out.println(processor.getTotalPopulation());
	}
	
	/**
	 * This method displays the total fines per capita for each ZIP Code 
	 */
	private void choice2() {
		//send to processor and receive structure
		//display ZIP code " " total fines per capita for that ZIP code
		
		Map<String, Double> totalFinesPerCapita = processor.getTotalFinesPerCapita();
		
		for(Map.Entry<String, Double> entry : totalFinesPerCapita.entrySet()) {
			System.out.println(entry + " " + entry.getValue());
		}
	}
	
	/**
	 * Helper method to obtain zip code from user
	 * @param in
	 * @return
	 */
	private int interim(Scanner in) {
		try {
			int zip = in.nextInt();
			//log zip
			return zip;
		}
		catch (Exception e) {
			System.out.println("User input error. Returning to menu");
			return -1;
		}
	}
	/**
	 * This method displays the average market value for residences in that ZIP Code
	 * @param ZIP
	 */
	private void choice3(int ZIP) {
		
		//sent to processor and receive answer
		//display answer
		
	}
	
	/**
	 * This method displays the average total livable area for residences in that ZIP Code
	 * @param ZIP
	 */
	private void choice4(int ZIP) {
		
		//sent to processor and receive answer
		//display answer
	}
	
	/**
	 * This method displays the total market value per capita for that ZIP Code
	 * @param ZIP
	 */
	private void choice5(int ZIP) {
		
		//sent to processor and receive answer
		//display answer
		
	}
	
	private void choice6() {
		
	}
}
