package com.lexxie.cbs2.parser;

import java.util.Iterator;
import java.util.Map.Entry;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.parser.data.commands.Variable;
import com.lexxie.cbs2.parser.data.commands.BlockData.Type;

public class PlaceVariables {

	public static void placeVariables(){
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		while(it.hasNext()){
			
			Entry pair = (Entry)it.next();

			Execute exe = (Execute) pair.getValue();

			Iterator listIterator = exe.getCommands().listIterator();
			
			while(listIterator.hasNext()){
				
				BlockData command = (BlockData) listIterator.next();
				
				if(command.getType() != Type.REASSIGN){
					String nCommand = command.getCommand();
				
					Iterator iz = Variable.getVariables().entrySet().iterator();
					while(iz.hasNext()){
					
						Entry v_pair = (Entry)iz.next();
					
						String re = v_pair.getValue().toString();
					
						if(re.charAt(0) == '#'){
							re = re.substring(1, re.length() - 1);
						}
					
						nCommand = nCommand.replaceAll("%" + v_pair.getKey() + "%", re);
					
					}	
					command.setCommand(nCommand);
				}else{
					Variable.reassignVar(command.getAssignedName(), command.getAssignedValue());
					listIterator.remove();
				}
			}
			
		}
		
		for(BlockData command : Init.getCommands()){
			
			String nCommand = command.getCommand();
			
			Iterator iz = Variable.getVariables().entrySet().iterator();
			while(iz.hasNext()){
				
				Entry v_pair = (Entry)iz.next();
				
				String re = v_pair.getValue().toString();
				
				if(re.charAt(0) == '#'){
					re = re.substring(1, re.length() -1);
				}
				
				nCommand = nCommand.replaceAll("%" + v_pair.getKey() + "%", re);
				
			}	
			command.setCommand(nCommand);
		}
		
	}
	
}
