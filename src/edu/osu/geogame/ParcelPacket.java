package edu.osu.geogame;

public class ParcelPacket {
	
	private ParcelType type;
	private int id;
	private float area;
	private int price;
	private int seed;
	private int fertilizer;
	private int irrigation;
	private String owner;
	
	public ParcelPacket() {
	}
	
	public void setType( ParcelType parcelType ) {
		this.type = parcelType;
	}
	
	public void setPlotID( int plotID ) {
		this.id = plotID;
	}
	
	public void setArea( float plotArea ) {
		this.area = plotArea;
	}
	
	public void setPrice( int plotPrice ) {
		this.price = plotPrice;
	}
	
	public void setSeed( int seedAmt ) {
		this.seed = seedAmt;
	}
	
	public void setFertilizer( int fertilizerAmt ) {
		this.fertilizer = fertilizerAmt;
	}
	
	public void setIrrigation( int irrigationAmt ) {
		this.irrigation = irrigationAmt;
	}
	
	public void setOwner( String owner ) {
		this.owner = owner;
	}
	
	public ParcelType parecelType() {
		return this.type;
	}
	
	public int plotID() {
		return this.id;
	}
	
	public float area() {
		return this.area;
	}
	
	public int price() {
		return this.price;
	}
	
	public int seedAmt() {
		return this.seed;
	}
	
	public int fertilizerAmt() {
		return this.fertilizer;
	}
	
	public int irrigationAmt() {
		return this.irrigation;
	}
	
	public String owner() {
		return this.owner;
	}
	
	

}
