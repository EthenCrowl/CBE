package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Function;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class ParseFunction {

	public static void parseFunction(Parser parser){
		
		parser.commands.clear();
		
		String name; 
		String value = "";
		Function func = new Function();
		
		Utilities.space(parser);
		name = Utilities.getVariable(parser);
		Utilities.consumeSpace(parser);
		Utilities.openBracket(parser);
		Utilities.consumeSpace(parser);
		
		parseParaOP(func, parser);
		
		Utilities.closeBracket(parser);
		Utilities.consumeSpace(parser);
		Utilities.checkEnd(parser);
		Utilities.consumeSpace(parser);
		Utilities.openCurlyBracket(parser);
		Utilities.consumeSpace(parser);
		Utilities.epsilon(parser);
		
		LineOP.lineOP(parser);
		
		Utilities.closeCurlyBracket(parser);
		Utilities.consumeSpace(parser);
		Utilities.epsilon(parser);
		
		func.setCommands(parser.commands);
		
		Function.addFunction(name, func);
		
	}
	
	public static void parseParaOP(Function function, Parser parser){
		
		if(parser.head.token == Token.VARIABLE){
			//parameter
			function.addParameter(parser.head.sequence);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			
			//has next parameter
			if(parser.head.token == Token.COMMA){
				Utilities.nextToken(parser);
				Utilities.consumeSpace(parser);
				parseParaOP(function, parser);
			}
		}else if(parser.head.token == Token.CLOSE_BRACKETS){
			return;
		}else{
			Utilities.error(parser);
		}

	}
	
}
