package edu.upenn.cit594.datamanagement;

import java.util.List;
import java.util.Map;

import edu.upenn.cit594.data.PropertyValues;

/**
 * The purpose of this interface is to provide flexibility in the implementation
 * of the reader classes and the processor classes.
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public interface PropertyValuesReader {
	
	/**
	 * This returns a map of Property Values
	 * @return
	 */
	public Map<String, List<PropertyValues>> getPropertyValues();

}
