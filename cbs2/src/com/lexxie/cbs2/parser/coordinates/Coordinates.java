package com.lexxie.cbs2.parser.coordinates;

public class Coordinates {

	private int x;
	private int y;
	private int z;
	
	/**
	 * 	creates a new Coordinate
	 * 	@param x  - coordinate
	 * 	@param y  - coordinate
	 * 	@param z  - coordinate
	 * */
	public Coordinates(int x, int y, int z){
		
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	@Override
	public String toString(){
		return "~" + this.x + " ~" + this.y + " ~" + this.z;
	}
	
	public String toString(boolean nbt){
		return "~" + this.z + " ~" + this.y + " ~" + this.x;
	}
	
	@Override
	public boolean equals(Object o){
		
		//if no object is found 
		if(o == null) return false;
		//if the object is 'this' object
		if(o == this) return true;
		//if object isn't the same class
		if(getClass() != o.getClass()) return false;
		
		//save to cast, since object is from the same class
		final Coordinates other = (Coordinates) o;
		
		return x == other.x && y == other.y && z == other.z;
		
	}
	
	@Override
	public int hashCode(){
		return 31 * x + y + z; 
	}
	
}
