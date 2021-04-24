package edu.upenn.cit594.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	protected Map<String, List<ParkingViolations>> violationsMap;
	protected Map<String, List<PropertyValues>> propertiesMap;
	protected Map<String, String> populationMap;
	
	//for memoization - if a calculation is called, results are stored,
	//so it can be used for subsequent calls
	protected int totalPopulation = -1;
	protected Map<String, Double> totalFinesPerCapita = new HashMap<String, Double>();
	
	public MainProcessor(ParkingViolationsReader violations) {
		this.violations = violations;
		population = new PopulationReader();
		properties = new PropertyValuesCSVReader();
		
		violationsMap = violations.getParkingViolations();
		propertiesMap = properties.getPropertyValues();
		populationMap = population.getPopulationData();
	}
	
	public int getTotalPopulation() {
		if (totalPopulation > -1) return totalPopulation; //check if call made prev.
		
		totalPopulation = 0; //first time calculation so set population to zero
		
		for(Map.Entry<String, String> entry : populationMap.entrySet()) {
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
		if(!totalFinesPerCapita.isEmpty()) return totalFinesPerCapita; //check if call made prev.
		
		double sum = 0;
		
		for(Map.Entry<String, List<ParkingViolations>> entry : violationsMap.entrySet()) {
			try {
				if(populationMap.containsKey(entry.getKey()) == false) continue;
				for(ParkingViolations single : entry.getValue()) {
					if(single.getZIPCode() == null) continue;
					if(single.getZIPCode().isEmpty()) continue;
					if(single.getState().equalsIgnoreCase("PA") == false) continue;
					sum += Double.parseDouble(single.getFine());
				}
				if(sum == 0) continue;
				
				int ZIPpopulation = Integer.parseInt(populationMap.get(entry.getKey()));
				double answer = sum / ZIPpopulation;
				
				totalFinesPerCapita.put(entry.getKey(), Double.parseDouble(truncate(answer, 5)));
			}
			catch (Exception e) {
				continue;
			}
		}
		
		return totalFinesPerCapita;
	}
	
	private String truncate(double answer, int decimals) {
		String answerString = Double.toString(answer);
		
		int decimalIndex = answerString.indexOf(".");
		
		if (decimals == 0) return answerString.substring(0, decimalIndex);
		
		answerString = answerString.concat(answerString.substring(0, decimalIndex)+
				answerString.substring(decimalIndex,decimalIndex+decimals+1));
		
		return answerString;
		
	}
	
	
	

}
