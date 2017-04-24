package com.lexxie.cbs2.parser.coordinates;

import java.util.LinkedList;
import java.util.List;

public class Tags {

private static List<Tags> tagList = new LinkedList<Tags>();
	
	public static List<Tags> getTags(){
		return tagList;
	}
	
	/**
	 * 	subtracts tow vectors an return the new vector
	 * 	@param The start coordinate a
	 * 	@param the end coordinate b
	 * 	@return new vector a-b
	 * */
	public static Coordinates subtract(Coordinates v, Coordinates u){
		
		return new Coordinates(v.getX() - u.getX(),
							  v.getY() - u.getY(),
							  v.getZ() - u.getZ());
	}
	
	//Non static class members
	
	private String name;
	private Coordinates vector;
	
	public Tags(String name, Coordinates vector){
		this.name = name;
		this.vector = vector;
	}
	
	public String getName(){
		return name;
	}
	
	public Coordinates getCoordinates(){
		return vector;
	}
	
}
