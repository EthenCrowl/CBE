package com.lexxie.cbs2.parser.data.commands;

import java.util.ArrayList;

public class BlockData {

	public static enum Type {NORMAL, CHAIN, REPEAT, REASSIGN};
	
	public static enum Stat {
		AE, AI, AB, SC, QR;
		
		String selector, object;
		
		public void set(String selector, String object){
			this.selector = selector;
			this.object = object;
		}
		
		public String getObject(){
			return object;
		}
		
		public String getSelector(){
			return selector;
		}
		
	}
	
	//default behavior
	private static boolean dDebug = false, dTest = false, dConditional = false, dRedstone = false;
	
	
	/**
	 * 	restore default setting 
	 * */
	public static void restoreDefaultBehaviour(){
		
		dDebug = false;
		dConditional = false;
		dRedstone = false;
	}

	private String command, blockName;
	private boolean tag, debug, conditional, redstone, behaviour;
	private Type type;
	private ArrayList<Stat> stats = new ArrayList<Stat>();
	
	String name, value;
	
	private String tagName;
	
	/**
	 * 	creates a new BlockData and define the custom behaviour
	 * */
	public BlockData(){
		debug = dDebug;
		conditional = dConditional;
		redstone = dRedstone;
		behaviour = false;
		type = Type.CHAIN;
		blockName = "@";
	}
	
	/**
	 * 	Creates a copy of BlockData
	 * */
	public BlockData(BlockData data){
		this.tag = data.tag;
		this.debug = data.debug;
		this.conditional = data.conditional;
		this.redstone = data.redstone;
		this.behaviour = data.behaviour;
		this.command = data.command;
		this.type = data.type;
		this.blockName = data.blockName;
	}
	
	public void assgingStat(Stat stat){
		stats.add(stat);
	}
	
	/**
	 * 	adds the command for this block
	 * */
	public void setCommand(String command){
		this.command = command;
	}
	
	/**
	 * 	sets parameter 'delete' for this block
	 * */
	public void setTag(boolean tag, String tagName){
		this.tag = tag;
		this.tagName = tagName;
	}
	
	/**
	 * 	adds name for the block
	 * */
	public void setBlockName(String blockName){
		this.blockName = blockName;
	}
	
	/**
	 * 	adds name to reassign variable later
	 * */
	public void assignName(String name){
		this.name = name;
	}
	
	public void assignValue(String value){
		this.value = value;
	}
	
	/**
	 * 	sets parameter 'initialize' for this block
	 * */
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	/**
	 * 	sets parameter 'conditional' for this block
	 * */
	public void setConditional(boolean conditional){
		this.conditional = conditional;
	}
	
	/**
	 * 	sets parameter 'need redstone' for this block
	 * */
	public void setRedstone(boolean redstone){
		this.redstone = redstone;
	}
	
	/**
	 * 	sets parameter 'behaviour' for this block
	 * */
	public void setBehaviour(boolean behaviour){
		this.behaviour = behaviour;
	}
	
	/**
	 * 	negates the custom behaviour for this block
	 * */
	public void negation(){
			tag = false;
			debug = false;
			conditional = false;
			redstone = false;
	}
	
	/**
	 * 	sets parameter 'delete' for this block
	 * */
	public void setBlockType(Type type){
		this.type = type;
	}
	
	/**
	 * 	defines custom behaviour for the next blocks
	 * */
	public void setDefaultBehaviour(){
		
		if(behaviour){
			dDebug = debug;
			dConditional = conditional;
			dRedstone = redstone;
		}
	}
	
	public String getAssignedName(){
		return name;
	}
	
	public String getAssignedValue(){
		return value;
	}
	public String getBlockName(){
		return blockName;
	}
	
	/**
	 * 	sets Type 'type' for this block
	 * */
	public void setType(Type type){
		this.type = type;
	}
	
	public String getCommand(){
		return this.command;
	}
	
	public String getTag(){
		return tagName;
	}
	
	public boolean hasTag() {
		return tag;
	}

	public boolean isDebug() {
		return debug;
	}

	public boolean isConditional() {
		return conditional;
	}

	public boolean isRedstone() {
		return redstone;
	}

	public boolean isBehaviour() {
		return behaviour;
	}

	public Type getType() {
		return type;
	}
	
	public ArrayList<Stat> getStats(){
		return stats;
	}
	
}
