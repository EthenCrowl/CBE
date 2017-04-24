package com.lexxie.cbs2.parser.data.commands;

import java.util.HashMap;

public class Variable {

	/*
	 * 	Static methods
	 * */
		
	private static HashMap variables = new HashMap<String, String>();
	
	/**
	 *	Adds a variable
	 *	@param ParseVariable 	
	 * */
	public static void addVar(Variable var){
		variables.put(var.getName(), var.getValue());
	}
	
	public static void deleteVat(String name){
		
		if(variables.containsKey(name)){
			variables.remove(name);
		}
		
	}
	
	public static void reassignVar(String name, String value){
	
		if(variables.containsKey(name)){
			variables.put(name, value);
		}
		
	}
	
	public static HashMap getVariables(){
		return variables;
	}
	
	/*
	 * 	Non static methods
	 * */
	
	private String value;
	private String name;
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getName(){
		return name;
	}
	
	public String getValue(){
		return value;
	}
	
}
