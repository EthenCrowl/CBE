package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class LineOP {

	public static void lineOP(Parser parser){
		
		if(parser.head.token == Token.REPEAT){
			Utilities.nextToken(parser);
			ParseRepeat.parseRepeat(parser);
			lineOP(parser);
			
		}else if(parser.head.token == Token.COMMENT){
			Utilities.nextStream(parser);
			lineOP(parser);
			
		}else if(parser.head.token == Token.P_BEHAVIOUR || parser.head.token == Token.P_CHAINCOMMAND || parser.head.token == Token.P_CONDITIONAL ||
					parser.head.token == Token.P_DEBUG || parser.head.token == Token.P_NEEDREDSTONE || parser.head.token == Token.P_NEGATE ||
					parser.head.token == Token.P_NORMALCOMMAND || parser.head.token == Token.P_REPEATINGCOMMAND || parser.head.token == Token.P_TAG
					|| parser.head.token == Token.P_TEST || parser.head.token == Token.P_NAME || parser.head.token == Token.P_STAT){
			ParseParameterOP.parseParameterOP(parser);
			Parameter.setDefaultBehaviour(parser);
			lineOP(parser);	
		}else if(parser.head.token == Token.COMMAND){
			if(parser.added){
				Utilities.command(parser, parser.head.sequence);
			}else{
				Utilities.addCommand(parser, parser.head.sequence);
			}
			parser.added = false;
			Utilities.nextToken(parser);
			Utilities.epsilon(parser);
			lineOP(parser);
			
		}else if(parser.head.token == Token.DEFINE_VARIABLE){
			Utilities.nextToken(parser);
			ParseVariable.defineVar(parser);
			lineOP(parser);
			
		}else if(parser.head.token == Token.ARRAY){
			ParseArray.parseArray(parser);
			lineOP(parser);

		}else if(parser.head.token == Token.VARIABLE){
			String call = "";
			while(parser.head.token != Token.EPSILON){
				call += parser.head.sequence;
				Utilities.nextToken(parser);
			}
			Utilities.addCommand(parser, call);
			lineOP(parser);
			
		}else if(parser.head.token == Token.ASSIGN){
			
			Utilities.nextToken(parser);
			ParseVariable.reassignVariable(parser);
			lineOP(parser);
			
		}else if(parser.head.token == Token.PLACEHOLDR){
			Utilities.addCommand(parser, parser.head.sequence);
			Utilities.nextToken(parser);
			lineOP(parser);

		}else if(parser.head.token == Token.EPSILON){
			Utilities.nextStream(parser);
			lineOP(parser);

		}else if(parser.head.token == Token.SPACE){
			Utilities.space(parser);
			lineOP(parser);

		}else if(parser.head.token == Token.CLOSE_CURLY_BRACKETS){
			return;
		}else{
			Utilities.error(parser);
		}
		
	}
	
}
