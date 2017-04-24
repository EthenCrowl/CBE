package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.BlockData.Type;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class ParseParameterOP {

	public static void parseParameterOP(Parser parser){
		
		if(!parser.added){
			parser.commands.add(new BlockData());
			parser.added = true;
		}
		
		if(parser.head.token == Token.P_BEHAVIOUR){
			Parameter.setBehaviour(parser, true);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}
		else if(parser.head.token == Token.P_CHAINCOMMAND){
			Parameter.setType(parser, Type.CHAIN);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_CONDITIONAL){
			Parameter.setConditionl(parser, true);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_DEBUG){
			Parameter.setParameterDebug(parser, true);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_NEEDREDSTONE){
			Parameter.setRedstone(parser, true);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_NEGATE){
			Parameter.setNegate(parser);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_NORMALCOMMAND){
			Parameter.setType(parser, Type.NORMAL);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_REPEATINGCOMMAND){
			Parameter.setType(parser, Type.REPEAT);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_TAG){
			Parameter.setParameterTag(parser, true);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.COMMAND){
			return;
		}else if(parser.head.token == Token.P_NAME){
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			String blockName = Utilities.getVariable(parser);
			Parameter.setBlockName(parser, blockName);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else if(parser.head.token == Token.P_STAT){
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			
			Parameter.addStat(parser);
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parseParameterOP(parser);
		}else{
			Utilities.error(parser);
		}
	}
	
}
