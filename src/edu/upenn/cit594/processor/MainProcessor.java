package edu.upenn.cit594.processor;

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
	protected Map<String, Double> totalFinesPerCapita = new TreeMap<String, Double>();
	protected Map<Integer, MarketValueLivableArea> marketValueLivableArea = new HashMap<Integer, MarketValueLivableArea>();
	
	public MainProcessor(ParkingViolationsReader violations) {
		this.violations = violations;
		population = new PopulationReader();
		properties = new PropertyValuesCSVReader();
		
		violationsList = violations.getParkingViolations();
		propertiesList = properties.getPropertyValues();
		populationList = population.getPopulationData();
	}
	
	public int getTotalPopulation() {
		if (totalPopulation > -1) return totalPopulation; //check if call made prev.
		
		totalPopulation = 0; //first time calculation so set population to zero
		
		for(Map.Entry<String, String> entry : populationList.entrySet()) {
			try {
				totalPopulation += Integer.parseInt(entry.getValue());
			}
			catch (Exception e) {
				continue;
			}
		}
		
		return totalPopulation;
	}
	
	public Map<String, Double> getTotalFinesPerCapita() {
		
		if(totalFinesPerCapita.isEmpty() == false) return totalFinesPerCapita; //check if call made prev.
		
		Map<String, Double> temporary = new TreeMap<String, Double>();
		
		for(ParkingViolations entry : violationsList) {
			try {
				String ZIPcode= entry.getZIPCode();
				String state = entry.getState();
				if(ZIPcode == null || state == null) continue;
				if(ZIPcode.isEmpty() || state.isEmpty()) continue;
				if(state.equalsIgnoreCase("PA") == false) continue;
				
				if(temporary.containsKey(ZIPcode)) {
					temporary.put(ZIPcode,temporary.get(ZIPcode)+Double.parseDouble(entry.getFine()));
				}
				else {
					temporary.put(ZIPcode,Double.parseDouble(entry.getFine()));
				}
			}
			catch (Exception e) {
				continue;
			}
		}
		
		for(Map.Entry<String, Double> entry : temporary.entrySet()) {
			String ZIPcode = entry.getKey();
			if(populationList.containsKey(ZIPcode) == false) continue;
			double ZIPpopulation = Double.parseDouble(populationList.get(ZIPcode));
			double answer = entry.getValue() / ZIPpopulation;
			
			String answerString = truncate(answer, 4);
			totalFinesPerCapita.put(ZIPcode, Double.parseDouble(answerString));
		}
		
		return totalFinesPerCapita;
	}
	
	public int getMarketValueLivableArea(int ZIP, int choice) {
		
		MarketValueLivableArea details;
		
		if(marketValueLivableArea.containsKey(ZIP)) {
			details = marketValueLivableArea.get(ZIP);
		}
		else {
			MarketValueLivableAreaProcessor strategy = new MarketValueLivableAreaProcessor(ZIP, propertiesList);
			details = strategy.calculate();
			marketValueLivableArea.put(ZIP, details);
		}
		
		System.out.println(marketValueLivableArea.get(ZIP).getMarketValue());
		
		double answer = 0;
		String answerString;
		
		if(choice == 3) {
			try {
				answer = details.getMarketValue() * 1.0 / details.getTotalHomes();
			}
			catch (Exception e) {
				return 0;
			}
		}
		if(choice == 4) {
			try {
				answer = details.getLivableArea() * 1.0 / details.getTotalHomes();
			}
			catch (Exception e) {
				return 0;
			}
		}

		answerString = truncate(answer, 0);
		return Integer.parseInt(answerString);
		
	}
	
	public int getMarketValuePerCapita(int ZIP) {
		
		if(populationList.containsKey(Integer.toString(ZIP)) == false) return 0;
		
		MarketValueLivableArea details;
		
		if(marketValueLivableArea.containsKey(ZIP)) {
			details = marketValueLivableArea.get(ZIP);
		}
		else {
			getMarketValueLivableArea(ZIP, 3);
			details = marketValueLivableArea.get(ZIP);
		}
		
		double answer = 0;
		String answerString;
		
		try {
			double ZIPpopulation = Double.parseDouble(populationList.get(Integer.toString(ZIP)));
			answer = details.getMarketValue() * 1.0 / ZIPpopulation;
		}
		catch (Exception e) {
			return 0;
		}
		
		answerString = truncate(answer, 0);
		return Integer.parseInt(answerString);
		
	}
	
	private String truncate(double answer, int decimals) {
		String answerString = Double.toString(answer);
		
		int decimalIndex = answerString.indexOf(".");
		
		if (decimals == 0) return answerString.substring(0, decimalIndex);
		
		answerString = answerString.substring(0, decimalIndex+decimals+1);
		System.out.println(answerString);
		return answerString;
	
	}
	

}
