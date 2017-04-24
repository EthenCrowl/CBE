package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.BlockData.Type;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;

public class Utilities {

	public static void error(Parser parser){
		
		if(parser.head.token != Token.ERROR){
			JOptionPane.showMessageDialog(new JDialog(), "Couldn't parse \"" + parser.head.sequence + "\"", "Syntax error", JOptionPane.ERROR_MESSAGE);
			parser.error = " " + parser.head.sequence;
			parser.head = new Token(Token.ERROR, "");
		}
		
	}
	
	public static void nextStream(Parser parser){
		
		parser.list++;
		parser.line++;
		
		if(!(parser.tokenStream.size() <= parser.list)){
			parser.tokens.clear();
			parser.tokens = (LinkedList<Token>) parser.tokenStream.get(parser.list).clone();
			
			if(parser.tokens.isEmpty()){
				parser.head = new Token(Token.EPSILON, "");
			}else{
				parser.head = parser.tokens.getFirst();
			}
			
		}else{
			parser.end = true;
		}		
	}	
	
	public static void consumeSpace(Parser parser){
		if(parser.head.token == Token.SPACE){
			nextToken(parser);
		}
	}
	
	public static void nextToken(Parser parser){
		try{
			parser.tokens.pop();
			
			if(parser.tokens.isEmpty()){
				parser.head = new Token(Token.EPSILON, "");
			}else{
				parser.head = parser.tokens.getFirst();
			}
			
		}catch(NoSuchElementException ex){
			parser.error = "Syntax error: ";
			error(parser);
		}
		
		
	}
	
	public static void space(Parser parser){
		
		if(parser.head.token == Token.SPACE){
			nextToken(parser);
		}else{
			error(parser);
		}
		
	}
	
	public static String getVariable(Parser parser){
		if(parser.head.token == Token.VARIABLE){
			String var = parser.head.sequence;
			nextToken(parser);
			return var;
		}else{
			error(parser);
			return "";
		}
	}

	public static void openCurlyBracket(Parser parser){
		
		if(parser.head.token == Token.OPEN_CURLY_BRACKETS){
			nextToken(parser);
		}else{
			error(parser);
		}	
	}
	
	public static void closeCurlyBracket(Parser parser){
		
		if(parser.head.token == Token.CLOSE_CURLY_BRACKETS){
			nextToken(parser);
		}else{
			error(parser);
		}
	}
	
	public static void openBracket(Parser parser){
		
		if(parser.head.token == Token.OPEN_BRACKETS){
			nextToken(parser);
		}else{
			error(parser);
		}
	}
	
	public static void closeBracket(Parser parser){
		
		if(parser.head.token == Token.CLOSE_BRACKETS){
			nextToken(parser);
		}else{
			error(parser);
		}	
	}
	
	public static int getDigit(Parser parser){
		
		if(parser.head.token == Token.DIGIT){
			int count = Integer.valueOf(parser.head.sequence);
			nextToken(parser);
			return count;
		}else{
			error(parser);
			return -1;
		}
		
	}
	
	public static void checkEnd(Parser parser){
		if(parser.head.token == Token.EPSILON){
			nextStream(parser);
		}
	}
	
	public static void epsilon(Parser parser){
		if(parser.head.token == Token.EPSILON){
			nextStream(parser);
		}else{
			error(parser);
		}
	}
	
	public static void equal(Parser parser){
			if(parser.head.token == Token.EQUALS){
				nextToken(parser);
			}else{
				error(parser);
			}
	}
	
	public static void reassignVar(Parser parser, String name, String value){
		parser.commands.add(new BlockData());
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).assignName(name);
			parser.commands.get(parser.commands.size() - 1).assignValue(value);
			parser.commands.get(parser.commands.size() - 1).setBlockType(Type.REASSIGN);
		}
	}
	
	public static void command(Parser parser, String command){
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setCommand(command);
		}
	}
	
	public static void addCommand(Parser parser, String command){
		parser.commands.add(new BlockData());
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setCommand(command);
		}
	}
	
	public static void addCommand(Parser parser, String command, Type type){
		parser.commands.add(new BlockData());
		if(parser.commands.get(parser.commands.size() - 1) != null){
			parser.commands.get(parser.commands.size() - 1).setCommand(command);
			parser.commands.get(parser.commands.size() - 1).setBlockType(type);
		}
	}
	
}
