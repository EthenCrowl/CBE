package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class ParseInit {

	public static void parseInit(Parser parser){
		
		parser.commands.clear();
		Utilities.consumeSpace(parser);
		Utilities.checkEnd(parser);
		Utilities.openCurlyBracket(parser);
		Utilities.consumeSpace(parser);
		Utilities.epsilon(parser);
		LineOP.lineOP(parser);
		Utilities.consumeSpace(parser);
		Utilities.closeCurlyBracket(parser);
	
		Init.addCommands(parser.commands);
		
	}
	
}
