package com.lexxie.cbs2.parser.data.commands;

import java.util.LinkedList;
import java.util.List;

public class Init {

	private static List<BlockData> commands = new LinkedList<BlockData>();
	
	public static void clearList(){
		commands.clear();
	}
	
	public static void addCommands(List<BlockData> commandData){
		for(BlockData command : commandData){
			commands.add(command);
		}
	}
	
	public static List<BlockData> getCommands(){
		return commands;
	}
	
}
