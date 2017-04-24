package com.lexxie.cbs2.parser.parse;

import java.util.LinkedList;
import java.util.List;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.BlockData.Stat;
import com.lexxie.cbs2.parser.data.commands.BlockData.Type;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class Parameter {

	public static void setDefaultBehaviour(Parser parser){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setDefaultBehaviour();
		}
	}
	
	public static void setBehaviour(Parser parser, boolean behaviour){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setBehaviour(behaviour);
		}
	}
	
	public static void setRedstone(Parser parser, boolean redstone){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setRedstone(redstone);
		}
	}
	
	public static void setConditionl(Parser parser, boolean conditional){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setConditional(conditional);
		}
	}
	
	public static void setNegate(Parser parser){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).negation();
		}
	}
	
	public static void setParameterDebug(Parser parser, boolean debug){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setDebug(debug);
		}
	}
	
	public static void setParameterTag(Parser parser, boolean tag){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			Utilities.nextToken(parser);
			Utilities.consumeSpace(parser);
			parser.commands.get(parser.commands.size() - 1).setTag(tag, Utilities.getVariable(parser));
			
		}
	}
	
	public static void setType(Parser parser, Type type){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setType(type);
		}
	}
	
	public static void setBlockName(Parser parser, String blockName){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setBlockName(blockName);
		}
	}
	
	public static void addStat(Parser parser){
		
		Utilities.consumeSpace(parser);
		
		String type = Utilities.getVariable(parser);
		
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);

		String selector = Utilities.getVariable(parser);
		
		Utilities.nextToken(parser);
		Utilities.consumeSpace(parser);
		
		String object = Utilities.getVariable(parser);
		
		System.out.println("Type:" + type + " selector:" + selector + " object:" + object);
		
		Stat stat = Stat.AE;
		
		if(type.equals("AE") || type.equals("ae")){
			stat = Stat.AE;
		}else if(type.equals("AI") || type.equals("ai")){
			stat = Stat.AI;
		}else if(type.equals("AB") || type.equals("ab")){
			stat = Stat.AB;
		}else if(type.equals("SC") || type.equals("sc")){
			stat = Stat.SC;
		}else if(type.equals("QR") || type.equals("qr")){
			stat = Stat.QR;
		}else{
			Utilities.error(parser);
		}
		
		stat.set(selector, object);
		
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).assgingStat(stat);
		}
	}
	
}
