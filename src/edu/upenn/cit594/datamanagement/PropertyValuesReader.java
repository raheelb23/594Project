package edu.upenn.cit594.datamanagement;

import java.util.List;

import edu.upenn.cit594.data.PropertyValues;

/**
 * The purpose of this interface is to provide flexibility in the implementation
 * of the reader classes and the processor classes.
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public interface PropertyValuesReader {
	
	/**
	 * This returns a list of Property Values
	 * @return
	 */
	public List<PropertyValues> getPropertyValues();

}
