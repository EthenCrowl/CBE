package com.lexxie.cbs2.parser;

import java.util.Iterator;
import java.util.Map.Entry;

import com.lexxie.cbs2.parser.data.commands.Array;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;

public class PlaceArrays {

	public static void placeArrays(){
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		while(it.hasNext()){
			
			Entry pair = (Entry)it.next();

			Execute exe = (Execute) pair.getValue();

			for(BlockData command : exe.getCommands()){
				
				String nCommand = command.getCommand();
				
				Iterator iz = Array.getArrays().entrySet().iterator();
				while(iz.hasNext()){
					
					Entry pair_a =  (Entry) iz.next();
					Array array = (Array) pair_a.getValue();
					
					int arrayCount = 0;
						
					for(String val : array.getValues()){
								
						nCommand = nCommand.replaceAll("%" + array.getName() + "\\[" + arrayCount + "\\]%", val);
				
						arrayCount++;
					}
					
				}	
				command.setCommand(nCommand);
			}	
		}
		
		for(BlockData command : Init.getCommands()){
			
			String nCommand = command.getCommand();
			
			Iterator iz = Array.getArrays().entrySet().iterator();
			while(iz.hasNext()){
				
				Entry pair_a =  (Entry) iz.next();
				Array array = (Array) pair_a.getValue();
				
				int arrayCount = 0;
					
				for(String val : array.getValues()){
							
					nCommand = nCommand.replaceAll("%" + array.getName() + "\\[" + arrayCount + "\\]%", val);
			
					arrayCount++;
				}
				
			}	
			command.setCommand(nCommand);
		}	
		
	}
	
}
