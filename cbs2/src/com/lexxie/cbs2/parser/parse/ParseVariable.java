package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Variable;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class ParseVariable {

	public static void defineVar(Parser parser){
		
		Variable variable = new Variable();
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);
		
		variable.setName(Utilities.getVariable(parser));
		
		
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);
		Utilities.equal(parser);
		Utilities.consumeSpace(parser);
		
		if(parser.head.token == Token.COMMAND){
			variable.setValue(parser.head.sequence);
		}else if(parser.head.token == Token.TEXT){
			variable.setValue(parser.head.sequence.substring(1, parser.head.sequence.length() - 1));
		}else{
			Utilities.error(parser);
		}
		
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);
		Utilities.epsilon(parser);
		
		Variable.addVar(variable);
		
	}
	
	public static void reassignVariable(Parser parser){
		
		Utilities.consumeSpace(parser);
		String name = Utilities.getVariable(parser);
		
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);
		Utilities.equal(parser);
		Utilities.consumeSpace(parser);
		
		String value = "";
		
		if(parser.head.token == Token.COMMAND){
			value = parser.head.sequence;
		}else if(parser.head.token == Token.TEXT){
			value = parser.head.sequence.substring(1, parser.head.sequence.length() - 1);
		}else{
			Utilities.error(parser);
		}
		
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);
		Utilities.epsilon(parser);
		
		Utilities.reassignVar(parser, name, value);
		
	}
	
}
