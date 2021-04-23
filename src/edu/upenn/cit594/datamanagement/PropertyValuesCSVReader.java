package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.cit594.data.PropertyValues;

public class PropertyValuesCSVReader implements PropertyValuesReader{

	protected String fileName;
	private HashMap<String, Integer> rowLabelMap = new HashMap<>();

	public PropertyValuesCSVReader(String fileName) {
		this.fileName = fileName;
	}

	
	@Override
	public List<PropertyValues> getPropertyValues() {
		
		List<PropertyValues> propertyValues = new ArrayList<PropertyValues>();
		BufferedReader readCSVInputFile = null;
		
		try {
			File file = new File(fileName);
			if (file.canRead()) {
				readCSVInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				System.out.println("Please re-check your csv input file.");
				//ErrorHandling.fileNotFound();
				//add later
			}
		} catch (FileNotFoundException e) {
			//ErrorHandling.fileNotFound();
			//add later
		}
		
		String property; 
		
		try {
			if((property = readCSVInputFile.readLine()) != null) {
				String rowLabel = readCSVInputFile.readLine();
				identifyRowLabels(rowLabel);
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		try {
			while((property = readCSVInputFile.readLine()) != null) {
				String propertyInfo = readCSVInputFile.readLine();
				PropertyValues pv = identifyPropertyInfo(propertyInfo);
				propertyValues.add(pv);
			}

			
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		return propertyValues;
	}
	
	private void identifyRowLabels(String label) {
		String[] rowComponents = label.split(",");
		
		for(int i = 0; i<rowComponents.length; i++) {
			String row = rowComponents[i];
			rowLabelMap.put(row, i);
		}
	}
	
	private PropertyValues identifyPropertyInfo(String info) {
		
		//to replace commas found within quotes
		StringBuilder sb = new StringBuilder(info);
		boolean found = false;
		
		for(int i = 0; i<sb.length(); i++) {
			char currentCharacter = sb.charAt(i);
			
			if(currentCharacter == '\"') {
				found = !found;
			}
			
			if(currentCharacter == ',' && found) {
				sb.setCharAt(i, ' ');
			}
		}
		
		String[] propertyInfoComponents = sb.toString().split(",");
		
		String marketValue = propertyInfoComponents[rowLabelMap.get("market_value")];
		String livableArea = propertyInfoComponents[rowLabelMap.get("total_livable_area")];
		String zipCodeAll = propertyInfoComponents[rowLabelMap.get("zip_code")];
		
		String ZIPCode; 
		
		if(zipCodeAll.length() >= 5) {
			ZIPCode = zipCodeAll.substring(0, 5);
		}
		
		else {
			ZIPCode = "";
		}
		
		PropertyValues propertyValueObject = new PropertyValues(marketValue, livableArea, ZIPCode);
		
		return propertyValueObject;
	}

}
