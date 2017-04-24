package com.lexxie.cbs2.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.BlockData.Type;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Function;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.parser.data.commands.Variable;

public class PlaceFunctions {

	public static boolean failedToParse = false;
	
	private static ListIterator<BlockData> executeIterator;
	private static ListIterator<BlockData> initIterator;
	
	public static void placeFunction(){
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		
		while(it.hasNext()){
			
			Entry pair = (Entry) it.next();
		
			for(executeIterator = ((Execute) pair.getValue()).getCommands().listIterator(); executeIterator.hasNext(); ){
				
				if(!failedToParse)
					parseFunctionCall(executeIterator);
						
			}	
		}
		
		if(!testFunction())
			placeFunction();
		
		for(initIterator = Init.getCommands().listIterator(); initIterator.hasNext(); ){
			
			if(!failedToParse)
				parseFunctionCall(initIterator);
			
		}
		
	}
	
	/**
	 * 	Parses function calls and splits its parameters
	 * */
	private static void parseFunctionCall(ListIterator<BlockData> dataIterator){
		
		BlockData blockData = dataIterator.next();
		
		String command = blockData.getCommand();
		
		if(blockData.getType() != Type.REASSIGN && command.charAt(0) != '#'){
			
			if(command.charAt(0) == '%' && command.charAt(command.length() - 1) == '%'){
				command = Variable.getVariables().get(command.substring(1, command.length() - 1)).toString();
			} 
			
			String info = command.replace(")", "|");
			info = info.replace("(", "|");
			info = info.replaceAll(",", "\\|");
			
			String[] info_f = info.split("\\|");
				
			if(info_f.length > 0)
				parseParameters(dataIterator, info_f, command);
			
		}
	}
	
	/**
	 * Parses function parameters and assign its values
	 * */
	private static void parseParameters(ListIterator<BlockData> dataIterator, String[] info_f, String command){
		
		String name = info_f[0];
		
		if(Function.getFunctions().containsKey(name)){
			
			Function function = (Function) Function.getFunctions().get(name);
			
			int parameterCount = function.getParameter().size();
			int count = 0;
			
			List<String> paraName = new ArrayList<String>();
			List<String> paraValue = new ArrayList<String>();
			
			try{
				System.out.println("---next--");
				while(parameterCount != 0){
					
					paraName.add(function.getParameter().get(count).toString());
					paraValue.add(info_f[count + 1]);

					System.out.println(info_f[count + 1]);
					
					parameterCount--;
					count++;
					
				}
			}catch(IndexOutOfBoundsException ex){
				failedToParse = true;
				JOptionPane.showMessageDialog(new JDialog(), "Invalid arguments for function \"" + command + "\"\n" + "expected: " + function.getParameter().size(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
			insertFunction(dataIterator, paraName, paraValue, function);
					
		}	
	}
	
	/**
	 * insert function into execution list
	 * */
	private static void insertFunction(ListIterator<BlockData> dataIterator, List<String> paraName, List<String> paraValue, Function function){
		
		List<BlockData> fCommands = function.getCommands();
		
		dataIterator.remove();
		
		for(BlockData data : fCommands){
			
			String nCommand = data.getCommand();
			String tmp = data.getCommand();
			
			for(int i = 0; i < paraName.size(); i++){
				
				nCommand = tmp;
				nCommand = nCommand.replaceAll("%" + paraName.get(i) + "%", paraValue.get(i));
				tmp = nCommand;
				
			}
			
			BlockData nData = new BlockData(data);
			nData.setCommand(nCommand);
			dataIterator.add(nData);
			
		}
	}
	
	/**
	 * test if all functions have been added, if not, repeat;
	 * */
	private static boolean testFunction(){
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		while(it.hasNext()){
			
			Entry pair = (Entry)it.next();		
			
			for(ListIterator<BlockData> dataIterator = ((Execute) pair.getValue()).getCommands().listIterator(); dataIterator.hasNext(); ){
				
				BlockData command = dataIterator.next();
				
				String nCommand =  command.getCommand();
				
				
				if(command.getType() != Type.REASSIGN && nCommand.charAt(0) != '#'){
					
					if(nCommand.charAt(0) == '%' && nCommand.charAt(nCommand.length() - 1) == '%'){
						nCommand = Variable.getVariables().get(nCommand.substring(1, nCommand.length() - 1)).toString();
					}else{
						return false;
					} 	
				}
			}
		}	
			
	return true;
		
	}
	
}
