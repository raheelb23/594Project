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
		int totalResidences = 0;
		
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
				totalResidences++;
				
				double market = 0, livable = 0;
				
				try {  market = Double.parseDouble(property.getMarketValue()); }
				catch (Exception e) {
					try { livable = Double.parseDouble(property.getLivableArea()); }
					catch (Exception f) { continue; }
					continue;
				}
				
				try { livable = Double.parseDouble(property.getLivableArea());}
				catch (Exception e) { continue; }
				
				marketSum += market;
				livableSum += livable;
				
			}
		}
		
		return new MarketValueLivableArea(marketSum, livableSum, totalResidences);
	}
}
