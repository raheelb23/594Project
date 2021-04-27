package edu.upenn.cit594.processor;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.upenn.cit594.data.MarketValueLivableArea;
import edu.upenn.cit594.data.ParkingViolations;
import edu.upenn.cit594.data.PropertyValues;
import edu.upenn.cit594.datamanagement.ParkingViolationsReader;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertyValuesCSVReader;
import edu.upenn.cit594.datamanagement.PropertyValuesReader;
import edu.upenn.cit594.logging.Logger;

/**
 * This class is the main brain that does calculations as
 * user enters their choices, which is passed in by the UI.
 * The class memorizes calculations for easy retrieval on subsequent calls and
 * for calcs where similar data is used (#3 and #4, for example).
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class MainProcessor {
	
	protected PopulationReader population;
	protected ParkingViolationsReader violations;
	protected PropertyValuesReader properties;
	protected List<ParkingViolations> violationsList;
	protected List<PropertyValues> propertiesList;
	protected Map<String, String> populationList;
	
	//for memoization - if a calculation is called, results are stored,
	//so it can be used for subsequent calls
	protected int totalPopulation = -1;
	protected String finesPerResidenceMaxPop = "-1";
	protected Map<String, String> totalFinesPerCapita = new TreeMap<String, String>();
	protected Map<Integer, MarketValueLivableArea> marketValueLivableArea = new HashMap<Integer, MarketValueLivableArea>();
	protected Map<String, Double> totalFines = new HashMap<String, Double>();
	
	/**
	 * the constructor initializes and obtains data from the readers
	 * @param violations
	 */
	public MainProcessor(ParkingViolationsReader violations, Logger logging) {
		this.violations = violations;
		population = new PopulationReader();
		properties = new PropertyValuesCSVReader();
		
		violationsList = violations.getParkingViolations(logging);
		propertiesList = properties.getPropertyValues(logging);
		populationList = population.getPopulationData(logging);
	}
	
	/**
	 * This method processes total population as provided in the population file.
	 * @return totalPopulation
	 */
	public int getTotalPopulation() {
		if (totalPopulation > -1) return totalPopulation; //check if call made prev.
		
		totalPopulation = 0; //first time calculation so set population to zero
		
		//iterate over population map and extract population, if it exists in the file
		for(Map.Entry<String, String> entry : populationList.entrySet()) {
			try {
				totalPopulation += Integer.parseInt(entry.getValue()); //store population count
			}
			catch (Exception e) {
				continue; //for any entries that are not valid, skip over
			}
		}
		
		return totalPopulation;
	}
	
	/**
	 * This method processes choice #2 for total fines per capita
	 * @return
	 */
	public Map<String, String> getTotalFinesPerCapita() {
		
		if(totalFinesPerCapita.isEmpty() == false) return totalFinesPerCapita; //check if call made prev.
		
		Map<String, Double> temporary = new TreeMap<String, Double>(); //set-up a temp map
		
		//Iterate over parking violations list and store in temp map for valid entries
		for(ParkingViolations entry : violationsList) {
			try {
				String ZIPcode= entry.getZIPCode();
				String state = entry.getState();
				if(ZIPcode == null || state == null) continue;
				if(ZIPcode.isEmpty() || ZIPcode.length() < 5 | state.isEmpty()) continue;
				if(state.equalsIgnoreCase("PA") == false) continue;
				
				if(temporary.containsKey(ZIPcode)) {
					temporary.put(ZIPcode,temporary.get(ZIPcode)+Double.parseDouble(entry.getFine()));
				}
				else {
					temporary.put(ZIPcode,Double.parseDouble(entry.getFine()));
				}
			}
			catch (Exception e) {
				continue; //for any entries that are not valid, skip over
			}
		}
		
		/*now iterate over each zip in the temp map and in the totalFinesPerCapita map store
		 * the answer to choice #2 and also in the totalFines store the total fines for that zip.
		 * This latter part helps with calculation for #6. Strategic decision to speed up that calc.
		*/
		for(Map.Entry<String, Double> entry : temporary.entrySet()) {
			String ZIPcode = entry.getKey();
			if(populationList.containsKey(ZIPcode) == false) continue;
			double ZIPpopulation = Double.parseDouble(populationList.get(ZIPcode));
			double answer = entry.getValue() / ZIPpopulation;
			String answerString = truncate(answer, 4);
			totalFinesPerCapita.put(ZIPcode, answerString);
			
			if(totalFines.containsKey(ZIPcode)) continue; 
			else {
				totalFines.put(ZIPcode, entry.getValue());
			}
		}
		
		return totalFinesPerCapita;
	}
	
	/**
	 * This method implements strategy whereby if the user inputs choice #3 or #4
	 * it will calculate and store both aspects using the MarketValueLivableArea
	 * processor. It does this on the basis of MarketValueLivableArea data structure.
	 * @param ZIP
	 * @param choice
	 * @return
	 */
	public String getMarketValueLivableArea(int ZIP, int choice) {
		
		MarketValueLivableArea details;
		
		//check if the calculation has been done for this ZIP code before
		if(marketValueLivableArea.containsKey(ZIP)) {
			if(choice == 3) return marketValueLivableArea.get(ZIP).getAvgMarketValue();
			if(choice == 4) return marketValueLivableArea.get(ZIP).getAvgLivableArea();
			return "";
		}
		else {
			MarketValueLivableAreaProcessor strategy = new MarketValueLivableAreaProcessor(ZIP, propertiesList);
			details = strategy.calculate(); //ask the helper processor to provide MarketValueLivableArea object
			marketValueLivableArea.put(ZIP, details);
		}
		
		//since both choice #3 and #4 are similar, calculate and store both
		double answerChoice3 = 0;
		String answerStringChoice3;

		answerChoice3 = details.getMarketValue() * 1.0 / details.getTotalHomesMV();
		if(Double.isNaN(answerChoice3)) answerChoice3 = 0;
	
		double answerChoice4 = 0;
		String answerStringChoice4;
	
		answerChoice4 = details.getLivableArea() * 1.0 / details.getTotalHomesLA();
		if(Double.isNaN(answerChoice4)) answerChoice4 = 0;

		answerStringChoice3 = truncate(answerChoice3, 0);
		answerStringChoice4 = truncate(answerChoice4, 0);
		details.setAvgMarketValue(answerStringChoice3);
		details.setAvgLivableArea(answerStringChoice4);
		
		if(choice == 3) return answerStringChoice3;
		if(choice == 4) return answerStringChoice4;
		
		return "";
	}
	
	/**
	 * This method calculates and returns the market value per capita. It uses
	 * the feature of MarketValueLivableArea object which stores the total
	 * market value of a ZIP.  
	 * @param ZIP
	 * @return
	 */
	public String getMarketValuePerCapita(int ZIP) {
		
		//if the provided ZIP is not in the population list, 
		//this calculation can stop right away
		if(populationList.containsKey(Integer.toString(ZIP)) == false) return "0";
		
		MarketValueLivableArea details;
		
		//check if calculation has been done before
		if(marketValueLivableArea.containsKey(ZIP)) {
			details = marketValueLivableArea.get(ZIP);
			if (details.getMarketValuePerCapita() != null) {
				return details.getMarketValuePerCapita();
			}
		}
		else {
			getMarketValueLivableArea(ZIP, 3);
			details = marketValueLivableArea.get(ZIP);
		}
		
		//calculate answer and store the results
		double answer = 0;
		String answerString;
		
	
		double ZIPpopulation = Double.parseDouble(populationList.get(Integer.toString(ZIP)));
		answer = details.getMarketValue() * 1.0 / ZIPpopulation;
		
		if(Double.isNaN(answer)) answer = 0;
		
		answerString = truncate(answer, 0);
		details.setMarketValuePerCapita(answerString);
		return answerString;
		
	}
	
	/**
	 * This method is the custom calculation. It determines the most
	 * populous ZIP and then calculates total fines (in PA) over the number of 
	 * residences that have livable area. 
	 * @return
	 */
	public String getFinesPerResidenceMaxPopCounty() {
		//check if calculation already done
		if(finesPerResidenceMaxPop.equals("-1") == false) return finesPerResidenceMaxPop;
		
		MarketValueLivableArea details;
		double fineDetails = 0;
		int ZIP = -1;
		int maximum = -1;
		
		//find the most populous ZIP by iterating over population map
		for(Map.Entry<String, String> population : populationList.entrySet()) {
			
			int num = 0;
			try {
				num = Integer.parseInt(population.getValue());
			}
			catch (Exception e) {
				continue;
			}
			
			if(num > maximum) {
				ZIP = Integer.parseInt(population.getKey());
				maximum = num;
			}
		}
		
		//check to see if market value livable area has been calculated
		//it already has total number of residences. if not, perform that
		//calc so results can be stored and retrieved. 
		if(marketValueLivableArea.containsKey(ZIP)) {
			details  = marketValueLivableArea.get(ZIP);
		}
		else {
			MarketValueLivableAreaProcessor strategy = new MarketValueLivableAreaProcessor(ZIP, propertiesList);
			details = strategy.calculate();
			marketValueLivableArea.put(ZIP, details);
		}
		
		//check to see if total fines is available. if not, perform that
		//calc so results can be stored and retrieved. 
		String ZIPcode = Integer.toString(ZIP);
		if(totalFines.containsKey(ZIPcode)) {
			fineDetails = totalFines.get(ZIPcode);
		}
		else {
			getTotalFinesPerCapita();
			fineDetails = totalFines.get(ZIPcode);
			
		}
		
		//perform the calculation, store the result, and return answer
		double answer = 0;
		String answerString;
		
		answer = fineDetails * 1.0 / details.getTotalHomesLA();
		
		if(Double.isNaN(answer)) answer = 0;
		
		answerString = truncate(answer, 4);
		finesPerResidenceMaxPop = answerString;
		return answerString;
	}
	
	/**
	 * Helper method to truncate responses and return a string representation
	 * @param answer
	 * @param decimals
	 * @return
	 */
	private String truncate(double answer, int decimals) {
		
		if (answer == 0) return "0";
		
		String answerString;
		
		if(decimals == 0) {
			DecimalFormat decimalFormat = new DecimalFormat("#,##0");
			decimalFormat.setRoundingMode(RoundingMode.DOWN);
			answerString = decimalFormat.format(answer);
			return answerString;
		}
		
		//Do the following if only decimals are greater than 0
		answerString = Double.toString(answer);
		
		int decimalIndex = answerString.indexOf(".");
		
		
		answerString = answerString.substring(0, decimalIndex+decimals+1);
		
		//check if trailing zeroes are needed
		int variance = answerString.length() - (decimalIndex + 1); 
		
		if(variance < decimals) {
			for(int i = 0 ; i < decimals - variance ; i++) {
				answerString.concat("0");
			}
		}
		
		return answerString;
	
	}
	

}
