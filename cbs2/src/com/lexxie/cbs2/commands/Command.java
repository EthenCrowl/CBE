package com.lexxie.cbs2.commands;

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Command {
	
	public static enum CType{COMMAND, ALL,	PARAMETER, TEXT, COORDIANTES, NUMBER, NBT, EXECUTE};
	
	public static List<Command> syntax = new LinkedList<Command>();
	
	private HashMap<String, Command> next = new HashMap<String, Command>();
	
	private CType type;
	
	private String command;
	private boolean hasNext, isOptional;
	
	public Command(String command, boolean hasNext, CType type){
		this.command = command;
		this.hasNext = hasNext;
		this.type = type;
	}
	
	public Command(){
		isOptional = false;
	}
	
	public void setType(CType type){
	this.type = type;
	}
	
	public void setHasNext(boolean hasNext){
		this.hasNext = hasNext;
	}
	
	public void setIsOptiona(boolean isOptiona){
		this.isOptional = isOptiona;
	}
	
	public void setCommand(String command){
		this.command = command;
	}
	
	public void addNext(Command next){
		this.next.put(next.getCommand(), next);
	}
	
	/**
	 * 	return the type of this command
	 * */
	public CType getType(){
		return type;
	}
	
	/**
	 * 	true if commands follow this command
	 * */
	public boolean hasNext(){
		return hasNext;
	}

	/**
	 * 	returns of this command is optional
	 * */
	public boolean isOptional(){
		return isOptional;
	}
	
	/**
	 * 	return this command
	 * */
	public String getCommand(){
		return command;
	}
	
	/*
	 * 	return hashMap of all Commands, folowwing this command
	 * */
	public HashMap<String, Command> getCommandsMap(){
		return next;
	}
	
	/**
	 * 	returns command, following this command, that contains the 'key'
	 * */
	public Command getNextCommand(String key){
		
		if(next.containsKey(key)){
			return next.get(key);
		}
		
		return null;
		
	}
	
	public boolean getNextAllOptional(){
		
		for(Entry pair : next.entrySet()){
			if(!((Command) pair.getValue()).isOptional())
				return false;
		}
			
		return true;
	}
	
	/**
	 * 	returns if the nextMap contains a execute command
	 * */
	public boolean containsExecute(){
		
		for(Entry<String, Command> entry : next.entrySet()){
			if(entry.getValue().getType() == CType.EXECUTE)
				return true;
		}
		
		return false;
	}
	
	/**
	 * 	returns all commands, following this command, who start with 's'
	 * */
	public List<Command> getNextCommands(String s){
		
		List<Command> commands = new LinkedList<Command>();
		
		for(Entry<String, Command> c : next.entrySet()){
			
			if(c.getValue().getType() == CType.COMMAND && c.getValue().command.startsWith(s)){
				commands.add(c.getValue());
			}
		}
		
		return commands;
		
	}
	
	public Command getNextExecuteCommand(){
		
		for(Entry<String, Command> entry : next.entrySet()){
			if(entry.getValue().getType() == CType.EXECUTE)
				return entry.getValue();
		}
		
		return null;
		
	}
	
	public Command getNextAllCommand(){
		
		List<Command> commands = new LinkedList<Command>();
		
		for(Entry<String, Command> c : next.entrySet()){
			
			if(c.getValue().getType() == CType.ALL){
				return c.getValue();
			}
		}
		
		return null;
	}
	
	/**
	 * 	returns all commands, following this command
	 * */
	public List<Command> getAllNextCommands(){
		
		List<Command> commands = new LinkedList<Command>();
		
		for(Entry<String, Command> c : next.entrySet()){	
				commands.add(c.getValue());		
		}
		
		return commands;	
		
	}
	
	public String getExpectations(){
		
		String expected = "Expected:";
		
		List<CType> in = new ArrayList<CType>();
		
		for(Entry entry : next.entrySet()){
			
			if(in.isEmpty()){
				in.add(((Command) entry.getValue()).getType());
				expected += ((Command) entry.getValue()).getType();
			}else{
				
				if(!in.contains(((Command) entry.getValue()).getType())){
				
					in.add(((Command) entry.getValue()).getType());
					expected += "or " + ((Command) entry.getValue()).getType();
					
				}
			}
			
		}
		
		return expected;
		
	}
	
}
