package com.lexxie.cbs2.debug.gui;

import com.lexxie.cbs2.parser.data.commands.BlockData;

public class DebugInfo {

	private BlockData data;
	private String command;
	
	public DebugInfo(BlockData data, String command){
		this.data = data;
		this.command = command;
	}
	
	public BlockData getBlockData(){
		return data;
	}
	
	public String getCommand(){
		return command;
	}
	
}
