package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.BlockData.Type;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class ParseExecute {

	public static void parseExecute(Parser parser){
		
		if(parser.head.token == Token.COMMAND){
			
			parser.commands.clear();
			
			Utilities.addCommand(parser, parser.head.sequence, Type.REPEAT);
			
			Utilities.nextToken(parser);
			Utilities.epsilon(parser);
			
			Utilities.consumeSpace(parser);
			Utilities.openCurlyBracket(parser);
			Utilities.consumeSpace(parser);
			Utilities.epsilon(parser);
			
			LineOP.lineOP(parser);
			
			Utilities.consumeSpace(parser);
			Utilities.closeCurlyBracket(parser);
			
			Execute exe = new  Execute();
			exe.setCommands(parser.commands);
			Execute.addExecutionList(exe);
		}
		
	}
	
}
