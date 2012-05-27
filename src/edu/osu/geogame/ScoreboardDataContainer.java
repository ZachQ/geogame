package edu.osu.geogame;

public class ScoreboardDataContainer {
	
	private int savingsFromPreviousYear;
	private int turnNumber;
	private String fateCard;
	private int money;
	private int adults;
	private int children;
	private int totalConsumption;
	private int totalLand;
	private int seededLand;
	private int weather;
	private int wheatPrice;
	private int yieldLR;
	private int yieldHYC;
	private int assetSellIncome;
	private int laborWagesIncome;
	private int wheatSellIncome;
	private int landBuyCost;
	private int seedBuyCost;
	private int fertilizerBuyCost;
	private int waterBuyCost;
	private int laborBuyCost;
	private int oxenBuyCost;
	
	public ScoreboardDataContainer() {	
	}
	
	public int savingsFromPreviousYear() {
		return savingsFromPreviousYear;
	}
	
	public int turnNumber() {
		return turnNumber;
	}
	
	public String fateCard() {
		return fateCard;
	}
	
	public int money() {
		return money;
	}
	
	public int adults() {
		return adults;
	}
	
	public int children() {
		return children;
	}
	
	public int totalConsumption() {
		return totalConsumption;
	}
	
	public int totalLand() {
		return totalLand;
	}
	
	public int seededLand() {
		return seededLand;
	}
	
	public int weather() {
		return weather;
	}
	
	public int wheatPrice() {
		return wheatPrice;
	}
	
	public int yieldLR() {
		return yieldLR;
	}
	
	public int yieldHYC() {
		return yieldHYC;
	}
	
	public int totalYield() {
		return yieldHYC + yieldLR;
	}
	
	public int totalYieldMinusConsumption() {
		return yieldHYC + yieldLR - totalConsumption;
	}
	
	public int assetSellIncome() {
		return assetSellIncome;
	}
	
	public int laborWagesIncome() {
		return laborWagesIncome;
	}
	
	public int wheatSellIncome() {
		return wheatSellIncome;
	}
	
	public int totalIncome() {
		return assetSellIncome + laborWagesIncome + wheatSellIncome;
	}
	
	public int landBuyCost() {
		return landBuyCost;
	}
	
	public int seedBuyCost() {
		return seedBuyCost;
	}
	
	public int fertilizerBuyCost() {
		return fertilizerBuyCost;
	}
	
	public int waterBuyCost() {
		return waterBuyCost;
	}
	
	public int laborBuyCost() {
		return laborBuyCost;
	}
	
	public int oxenBuyCost() {
		return oxenBuyCost;
	}
	
	public int totalCosts() {
		return landBuyCost + seedBuyCost + fertilizerBuyCost + waterBuyCost + laborBuyCost
				+ oxenBuyCost;
	}
	
	public int balance() {
		return totalIncome() - totalCosts();
	}
	
	
	
	public void setSavingsFromPreviousYear( int value ) {
		savingsFromPreviousYear = value;
	}
	
	public void setTurnNumber( int value ) {
		 turnNumber = value;
	}
	
	public void setFateCard( String value ) {
		 fateCard = value;
	}
	
	public void setMoney( int value ) {
		 money = value;
	}
	
	public void setAdults( int value ) {
		 adults = value;
	}
	
	public void setChildren( int value ) {
		 children = value;
	}
	
	public void setTotalConsumption( int value ) {
		 totalConsumption = value;
	}
	
	public void setTotalLand( int value ) {
		 totalLand = value;
	}
	
	public void setSeededLand( int value ) {
		 seededLand = value;
	}
	
	public void setWeather( int value ) {
		 weather = value;
	}
	
	public void setWheatPrice( int value ) {
		 wheatPrice = value;
	}
	
	public void setYieldLR( int value ) {
		 yieldLR = value;
	}
	
	public void setYieldHYC( int value ) {
		 yieldHYC = value;
	}
	
	public void setAssetSellIncome( int value ) {
		 assetSellIncome = value;
	}
	
	public void setLaborWagesIncome( int value ) {
		 laborWagesIncome = value;
	}
	
	public void setWheatSellIncome( int value ) {
		 wheatSellIncome = value;
	}
	
	public void setLandBuyCost( int value ) {
		 landBuyCost = value;
	}
	
	public void setSeedBuyCost( int value ) {
		 seedBuyCost = value;
	}
	
	public void setFertilizerBuyCost( int value ) {
		 fertilizerBuyCost = value;
	}
	
	public void setWaterBuyCost( int value ) {
		 waterBuyCost = value;
	}
	
	public void setLaborBuyCost( int value ) {
		 laborBuyCost = value;
	}
	
	public void setOxenBuyCost( int value ) {
		 oxenBuyCost = value;
	}
	
	

}
