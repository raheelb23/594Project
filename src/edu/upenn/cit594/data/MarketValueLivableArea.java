package edu.upenn.cit594.data;

public class MarketValueLivableArea {

	protected double marketValue;
	protected double livableArea;
	protected int totalHomes;
	protected String avgMarketValue;
	protected String avgLivableArea;
	protected String marketValuePerCapita;
	
	public MarketValueLivableArea(double marketValue, double livableArea, int totalHomes) {
		this.marketValue = marketValue;
		this.livableArea = livableArea;
		this.totalHomes = totalHomes;
	}

	/**
	 * @return the marketValue
	 */
	public double getMarketValue() {
		return marketValue;
	}

	/**
	 * @return the livableArea
	 */
	public double getLivableArea() {
		return livableArea;
	}

	/**
	 * @return the totalHomes
	 */
	public int getTotalHomes() {
		return totalHomes;
	}

	/**
	 * @return the avgMarketValue
	 */
	public String getAvgMarketValue() {
		return avgMarketValue;
	}

	/**
	 * @param avgMarketValue the avgMarketValue to set
	 */
	public void setAvgMarketValue(String avgMarketValue) {
		this.avgMarketValue = avgMarketValue;
	}

	/**
	 * @return the avgLivableArea
	 */
	public String getAvgLivableArea() {
		return avgLivableArea;
	}

	/**
	 * @param avgLivableArea the avgLivableArea to set
	 */
	public void setAvgLivableArea(String avgLivableArea) {
		this.avgLivableArea = avgLivableArea;
	}

	/**
	 * @return the marketValuePerCapita
	 */
	public String getMarketValuePerCapita() {
		return marketValuePerCapita;
	}

	/**
	 * @param marketValuePerCapita the marketValuePerCapita to set
	 */
	public void setMarketValuePerCapita(String marketValuePerCapita) {
		this.marketValuePerCapita = marketValuePerCapita;
	}
	
	
	
}
