package com.lexxie.cbs2.parser.data.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Function {

	/*
	 * Static methods
	 */
	
	private static HashMap functions = new HashMap<String, Function>();
	
	/**
	 * 	Adds function to list
	 * 	@param function name
	 * 	@param function commands
	 * */
	public static void addFunction(String name, Function function){
		functions.put(name, function);
	}
	
	public static HashMap getFunctions(){
		return functions;
	}
	
	/*
	 * Non static methods
	 */
	
	private List<BlockData> commands = new LinkedList<BlockData>();
	private List<String> parameter = new ArrayList<String>();
	
	public void setCommands(List<BlockData> commands){
		this.commands.addAll(commands);
	}
	
	/*
	 * 	adds parameter to function
	 * */
	public void addParameter(String para){
		parameter.add(para);
	}
	
	public List<String> getParameter(){
		return parameter;
	}
	
	public List<BlockData> getCommands(){
		return commands;
	}
	
}
