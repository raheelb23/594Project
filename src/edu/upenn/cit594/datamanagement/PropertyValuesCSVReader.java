package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.upenn.cit594.data.PropertyValues;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.ui.ErrorCheckerPrinter;

/**
 * This class reads the properties data from the specified file
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class PropertyValuesCSVReader implements PropertyValuesReader {

	public static String fileName;
	private HashMap<String, Integer> rowLabelMap = new HashMap<>();
	private String ZIPCode;


	@Override
	/**
	 * This method returns a list containing property value objects
	 */
	public List<PropertyValues> getPropertyValues(Logger logging) {

		List<PropertyValues> propertyValues = new ArrayList<PropertyValues>();
		BufferedReader readCSVInputFile = null;

		try {
			logging.logString(fileName);
			File file = new File(fileName);
			if (file.canRead()) {
				readCSVInputFile = new BufferedReader(new FileReader(fileName));
			} else {
				ErrorCheckerPrinter.printFileReadError(); //prints the error
				//returns empty list to trigger program exit
				return new ArrayList<PropertyValues>();
			}
		} catch (FileNotFoundException e) {
			ErrorCheckerPrinter.printFileDoesNotExistError(); //prints the error
			//returns empty list to trigger program exit
			return new ArrayList<PropertyValues>();
		}

		try {
			String property;

			if ((property = readCSVInputFile.readLine()) != null) {
				identifyRowLabels(property);
			}

			while ((property = readCSVInputFile.readLine()) != null) {
				PropertyValues pv = identifyPropertyInfo(property);
				propertyValues.add(pv);
			}

		} catch (Exception e) {

			ErrorCheckerPrinter.printFileReadError(); //prints the error
			//returns empty list to trigger program exit
			return new ArrayList<PropertyValues>();
		}

		return propertyValues;
	}

	/**
	 * Helper method for comma recognitions
	 * @param label
	 */
	private void identifyRowLabels(String label) {
		String[] rowComponents = label.split(",");

		for (int i = 0; i < rowComponents.length; i++) {
			String row = rowComponents[i];
			rowLabelMap.put(row, i);
		}
	}

	/**
	 * Helper method to replace commas found within quotes
	 * @param info
	 * @return
	 */
	private PropertyValues identifyPropertyInfo(String info) {

		StringBuilder sb = new StringBuilder(info);
		boolean found = false;

		for (int i = 0; i < sb.length(); i++) {
			char currentCharacter = sb.charAt(i);

			if (currentCharacter == '\"') {
				found = !found;
			}

			if (currentCharacter == ',' && found) {
				sb.setCharAt(i, ' ');
			}
		}

		String[] propertyInfoComponents = sb.toString().split(",");

		String marketValue = propertyInfoComponents[rowLabelMap.get("market_value")];
		String livableArea = propertyInfoComponents[rowLabelMap.get("total_livable_area")];
		String zipCodeAll = propertyInfoComponents[rowLabelMap.get("zip_code")];

		if (zipCodeAll.length() >= 5) {
			ZIPCode = zipCodeAll.substring(0, 5);
		}

		else {
			ZIPCode = "";
		}

		PropertyValues propertyValueObject = new PropertyValues(marketValue, livableArea, ZIPCode);

		return propertyValueObject;
	}

}
