package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.Array;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class ParseArray {

	public static void parseArray(Parser parser){
		
		Array array = new Array();
		
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);
		
		array.setName(Utilities.getVariable(parser));
		
		Utilities.consumeSpace(parser);
		Utilities.equal(parser);
		Utilities.consumeSpace(parser);
		
		Utilities.openCurlyBracket(parser);
		Utilities.consumeSpace(parser);
		
		addValue(parser, array);
		
		Utilities.closeCurlyBracket(parser);
		
		Array.addArray(array.getName(), array);
		
	}
	
	public static void addValue(Parser parser, Array array){
		
		if(parser.head.token == Token.TEXT){
			array.addValue(parser.head.sequence.substring(1,  parser.head.sequence.length() - 1));
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			
			if(parser.head.token == Token.COMMA){
				Utilities.nextToken(parser);
				Utilities.consumeSpace(parser);
				addValue(parser, array);
			}else if(parser.head.token == Token.EPSILON){
				Utilities.nextStream(parser);
				Utilities.consumeSpace(parser);
				addValue(parser, array);
			}
		}else if(parser.head.token == Token.CLOSE_CURLY_BRACKETS){
			return;
		}else if(parser.head.token == Token.EPSILON){
			Utilities.nextStream(parser);
			Utilities.consumeSpace(parser);
			addValue(parser, array);
		}
		else{
			Utilities.error(parser);
		}
	}
	
}
