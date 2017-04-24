package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class ParseRepeat {

	public static void parseRepeat(Parser parser){
		
		Utilities.consumeSpace(parser);
		Utilities.openBracket(parser);
		int count = Utilities.getDigit(parser);
		Utilities.consumeSpace(parser);
		String var = Utilities.getVariable(parser);
		Utilities.closeBracket(parser);
		Utilities.consumeSpace(parser);
		Utilities.checkEnd(parser);
		Utilities.consumeSpace(parser);
		Utilities.openCurlyBracket(parser);
		
		List<BlockData> commandsCopy = new LinkedList<BlockData>();
		commandsCopy.addAll(parser.commands);
		parser.commands.clear();
		
		LineOP.lineOP(parser);
		
		int repeat = 0;
		
		if(count > -1){
			while(count != 0){
				for(BlockData data : parser.commands){
					
					String n_command = data.getCommand();
					
					if(n_command.contains("%" + var + "%")){
						n_command = data.getCommand().replaceAll("%" + var + "%", String.valueOf(repeat));
					}
				
					BlockData n_data = new BlockData(data);
					
					n_data.setCommand(n_command);
					
					commandsCopy.add(n_data);
				}
				repeat++;
				count--;
			}
		}
		
		parser.commands.clear();
		parser.commands = commandsCopy;
		
		Utilities.closeCurlyBracket(parser);
		Utilities.consumeSpace(parser);
		Utilities.epsilon(parser);
	}
	
}
