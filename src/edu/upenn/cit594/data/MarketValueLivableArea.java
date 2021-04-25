package edu.upenn.cit594.data;

public class MarketValueLivableArea {

	protected double marketValue;
	protected double livableArea;
	protected int totalHomes;
	
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
	
	
}
