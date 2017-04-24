package com.lexxie.cbs2.parser.data.commands;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Execute {

	/* 
	 * 	Static methods
	 * */
	
	private static HashMap execute = new HashMap<Integer, Execute>();
	
	private static int loop = 0;
	
	public static void clearList(){
		execute.clear();
	}
	
	
	/**
	 * 	adds ExecuteCommand to execution list
	 * 	@param ExecuteCommand
	 * */
	public static void addExecutionList(Execute ex){
		execute.put(loop, ex);
		loop++;
	}
	
	/**
	 * 	return the commandRows to execute
	 * */
	public static HashMap getMainLoops(){
		return execute;
	}
	
	/*
	 * 	Non static methods
	 * */
	
	private List<BlockData> commands = new LinkedList<BlockData>();
	
	public void setCommands(List<BlockData> commands){
		this.commands.addAll(commands);
	}
	
	public List<BlockData> getCommands(){
		return this.commands;
	}
	
}
