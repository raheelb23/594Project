package edu.upenn.cit594.processor;

import java.util.List;

import edu.upenn.cit594.data.MarketValueLivableArea;
import edu.upenn.cit594.data.PropertyValues;

/**
 * This class is a specialized processor intended to be used 
 * to calculate market value and livable area for residences in 
 * a given ZIP. By running this processor, a single process can calculate
 * both aspects. Additionally, fields that house sum of market values, 
 * livable areas, as well as the respective number of properties can be
 * used for other calculations. 
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class MarketValueLivableAreaProcessor {
	
	protected List<PropertyValues> propertiesList;
	protected int ZIP;
	
	/**
	 * Constructor
	 * @param ZIP
	 * @param propertiesList
	 */
	public MarketValueLivableAreaProcessor(int ZIP, List<PropertyValues> propertiesList) {
		this.ZIP = ZIP;
		this.propertiesList = propertiesList;
	}
	
	/**
	 * This method calculates the marketSum, livableSum, totalResidencesMV, totalResidencesLA
	 * for a given ZIP Code. The processor doesn't perform the full calculation for
	 * user entries. Rather, it strategizes to provide all fields necessary in those calculations
	 * by returns an object of MarketValueLivableArea data structure. 
	 * @return
	 */
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
					totalResidencesMV++; //only add to total residences if market value is valid
					}
				catch (Exception e) {
					try { 
						//if market value does not exist then an exception is thrown
						//this makes sure livableSum and totalResidencesLA are calculated
						livableSum += Double.parseDouble(property.getLivableArea()); 
						totalResidencesLA++; //only add to total residences if livable area is valid
						}
					catch (Exception f) { 
						continue; } //if both market value and livable area fail
					continue;
				}
				
				//comes to this if market value is valid entry
				try { 
					livableSum += Double.parseDouble(property.getLivableArea());
					totalResidencesLA++; //only add to total residences if livable area is valid
					}
				catch (Exception e) { 
					continue; } //for invalid entry
				
				
			}
		}
		
		return new MarketValueLivableArea(marketSum, livableSum, totalResidencesMV, totalResidencesLA);
	}
}
