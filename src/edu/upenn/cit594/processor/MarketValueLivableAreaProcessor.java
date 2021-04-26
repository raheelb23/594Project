package edu.upenn.cit594.processor;

import java.util.List;

import edu.upenn.cit594.data.MarketValueLivableArea;
import edu.upenn.cit594.data.PropertyValues;

public class MarketValueLivableAreaProcessor {
	
	protected List<PropertyValues> propertiesList;
	protected int ZIP;
	
	public MarketValueLivableAreaProcessor(int ZIP, List<PropertyValues> propertiesList) {
		this.ZIP = ZIP;
		this.propertiesList = propertiesList;
	}
	
	public MarketValueLivableArea calculate() {
		
		double marketSum = 0;
		double livableSum = 0;
		int totalResidencesMV = 0;
		int totalResidencesLA = 0;
		
		for(PropertyValues property : propertiesList) {
			
			String ZIPcode = property.getZipCode();

			if(ZIPcode == null) continue;
			if(ZIPcode.isEmpty()) continue;
			
			try { Integer.parseInt(ZIPcode);
			}
			catch (Exception e) {
				continue;
			}
			
			if(Integer.parseInt(ZIPcode) == ZIP) {
			
				
				try {  
					marketSum += Double.parseDouble(property.getMarketValue()); 
					totalResidencesMV++;
					}
				catch (Exception e) {
					try { 
						livableSum += Double.parseDouble(property.getLivableArea()); 
						totalResidencesLA++;
						}
					catch (Exception f) { 
						continue; }
					continue;
				}
				
				try { 
					livableSum += Double.parseDouble(property.getLivableArea());
					totalResidencesLA++;
					}
				catch (Exception e) { 
					continue; }
				
				
			}
		}
		
		return new MarketValueLivableArea(marketSum, livableSum, totalResidencesMV, totalResidencesLA);
	}
}
