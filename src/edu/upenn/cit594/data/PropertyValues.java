package edu.upenn.cit594.data;

/**
 * This class provides a data structure to
 * store property values data
 * @author Muizz Mullani and Raheel Bhimani
 *
 */
public class PropertyValues {

	private String ZIPCode;
	private String livableArea;
	private String marketValue;
	
	public PropertyValues(String marketValue, String livableArea, String ZIPCode) {
		this.marketValue = marketValue;
		this.livableArea = livableArea;
		this.ZIPCode = ZIPCode;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return ZIPCode;
	}
	/**
	 * @return the liveableArea
	 */
	public String getLivableArea() {
		return livableArea;
	}
	/**
	 * @return the marketValue
	 */
	public String getMarketValue() {
		return marketValue;
	}
	
	
}
