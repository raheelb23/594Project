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
	
	public Map<String, String> getTotalFinesPerCapita() {
		
		if(totalFinesPerCapita.isEmpty() == false) return totalFinesPerCapita; //check if call made prev.
		
		Map<String, Double> temporary = new TreeMap<String, Double>();
		
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
				continue;
			}
		}
		
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
	
	public String getMarketValueLivableArea(int ZIP, int choice) {
		
		MarketValueLivableArea details;
		
		if(marketValueLivableArea.containsKey(ZIP)) {
			if(choice == 3) return marketValueLivableArea.get(ZIP).getAvgMarketValue();
			if(choice == 4) return marketValueLivableArea.get(ZIP).getAvgLivableArea();
			return "";
		}
		else {
			MarketValueLivableAreaProcessor strategy = new MarketValueLivableAreaProcessor(ZIP, propertiesList);
			details = strategy.calculate();
			marketValueLivableArea.put(ZIP, details);
		}
		
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
	
	public String getMarketValuePerCapita(int ZIP) {
		
		if(populationList.containsKey(Integer.toString(ZIP)) == false) return "0";
		
		MarketValueLivableArea details;
		
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
		
		double answer = 0;
		String answerString;
		
	
		double ZIPpopulation = Double.parseDouble(populationList.get(Integer.toString(ZIP)));
		answer = details.getMarketValue() * 1.0 / ZIPpopulation;
		
		if(Double.isNaN(answer)) answer = 0;
		
		answerString = truncate(answer, 0);
		details.setMarketValuePerCapita(answerString);
		return answerString;
		
	}
	
	public String getFinesPerResidenceMaxPopCounty() {
		if(Integer.parseInt(finesPerResidenceMaxPop) > -1) return finesPerResidenceMaxPop;
		
		MarketValueLivableArea details;
		double fineDetails = 0;
		int ZIP = -1;
		int maximum = -1;
		
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
		
		
		if(marketValueLivableArea.containsKey(ZIP)) {
			details  = marketValueLivableArea.get(ZIP);
		}
		else {
			MarketValueLivableAreaProcessor strategy = new MarketValueLivableAreaProcessor(ZIP, propertiesList);
			details = strategy.calculate();
			marketValueLivableArea.put(ZIP, details);
		}
		
		String ZIPcode = Integer.toString(ZIP);
		if(totalFines.containsKey(ZIPcode)) {
			fineDetails = totalFines.get(ZIPcode);
		}
		else {
			getTotalFinesPerCapita();
			fineDetails = totalFines.get(ZIPcode);
			
		}
		
		double answer = 0;
		String answerString;
		
		answer = fineDetails * 1.0 / details.getTotalHomesLA();
		
		if(Double.isNaN(answer)) answer = 0;
		
		answerString = truncate(answer, 4);
		finesPerResidenceMaxPop = answerString;
		return answerString;
	}
	
	private String truncate(double answer, int decimals) {
		
		if (answer == 0) return "0";
		
		String answerString;
		
		if(decimals == 0) {
			DecimalFormat decimalFormat = new DecimalFormat("#,##0");
			decimalFormat.setRoundingMode(RoundingMode.DOWN);
			answerString = decimalFormat.format(answer);
			return answerString;
		}
		
		answerString = Double.toString(answer);
		
		int decimalIndex = answerString.indexOf(".");
		
		
		answerString = answerString.substring(0, decimalIndex+decimals+1);
		
		int variance = answerString.length() - (decimalIndex + 1); 
		
		if(variance < decimals) {
			for(int i = 0 ; i < decimals - variance ; i++) {
				answerString.concat("0");
			}
		}
		
		return answerString;
	
	}
	

}
