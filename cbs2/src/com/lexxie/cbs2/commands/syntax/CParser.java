package com.lexxie.cbs2.commands.syntax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.commands.Command;
import com.lexxie.cbs2.commands.Command.CType;
import com.lexxie.cbs2.commands.tokenizer.CTokenizer.Token;
import com.lexxie.cbs2.parser.parse.Parser;

public class CParser {

	List<Command> commands = new LinkedList<Command>();
	
	Command command = new Command();
	
	ArrayList<LinkedList<Token>> tokenStream;
	
	LinkedList<Token> tokens;
	
	Token head;
	
	int list, line;
	
	boolean end, optional;
	
	public void parse(ArrayList<LinkedList<Token>> tokenStream){
		
		list = 0;
		line = 0;
		
		end = false;
		optional = false;
		
		this.tokenStream = tokenStream;
		
		if(tokenStream.isEmpty()){
			error();
		}else if ( list > tokenStream.size()){
			error();
		}else{
			tokens = (LinkedList<Token>) tokenStream.get(list).clone();
			
			head = tokens.getFirst();		
			
			start();
			
		}
	}
	
	private void start(){
		
		consumeSpace();
		
		if(head.token == Token.TEXT){
			
			command.setCommand(head.sequence.substring(1, head.sequence.length() - 1));
			command.setType(CType.COMMAND);
			
			commands.add(command);
			
			checkNext(commands, null);
			
			for(Command c : commands){
				
				Command.syntax.add(c);
			}
			
			commands.clear();
			command = new Command();
			
			
		}else if(head.token == Token.EPSILON){
			nextStream();
			start();
		}else{
			error();
		}
	
		if(!end && head.token != Token.ERROR){
			start();
		}
	}
	
	private void checkNext(List<Command> cmd, List<Command> main){
		
		nextToken();
		consumeSpace();
		
		if(head.token == Token.OPEN_CURLY_BRACKETS){
			
			optional = false;
			
			nextToken();
			consumeSpace();
			
			for(Command c : cmd){
				c.setHasNext(true);	
			}
			
			addSubCommand(cmd);
			consumeSpace();
			closeCurlyBracket();
			
			if(head.token == Token.EPSILON){
				nextStream();
				consumeSpace();
			}
			
			if(head.token == Token.COMMA){
				nextToken();
				consumeSpace();	
				addSubCommand(main);			
			}
			
		}else if(head.token == Token.SEMICOLON){
			
			nextElement(main);
			
		}else if(head.token == Token.SEPERATE){
			
			nextToken();
			consumeSpace();
			
			addSep(cmd);
			checkNext(cmd, main);
			
		}else if(head.token == Token.EPSILON){
			nextStream();
			checkNext(cmd, main);
		}else if(head.token == Token.ERROR){
			error();
		}
		
	}
	
	private void addSubCommand(List<Command> cmd){
		
		consumeSpace();
		
		List<Command> subs = new ArrayList<Command>();

		Command sub = new Command();
		
		if(head.token == Token.TEXT){
			sub.setCommand(head.sequence.substring(1, head.sequence.length() - 1));
			sub.setType(CType.COMMAND);
			
			if(optional)
				sub.setIsOptiona(true);
			
			subs.add(sub);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
			
		}else if(head.token == Token.ALL){
			
			sub.setType(CType.ALL);
			subs.add(sub);
			
			if(optional)
				sub.setIsOptiona(true);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
			
		}else if(head.token == Token.PARAMETER){
			
			sub.setType(CType.PARAMETER);
			subs.add(sub);
			
			if(optional)
				sub.setIsOptiona(true);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
			
		}else if(head.token == Token.ALLTEXT){
		
			sub.setType(CType.TEXT);
			subs.add(sub);
			
			if(optional)
				sub.setIsOptiona(true);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
				
		}else if(head.token == Token.COORDIANTE){
		
			sub.setType(CType.COORDIANTES);
			subs.add(sub);
			
			if(optional)
				sub.setIsOptiona(true);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
				
		}else if(head.token == Token.NUMBER){
		
			sub.setType(CType.NUMBER);
			subs.add(sub);
			
			if(optional)
				sub.setIsOptiona(true);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
				
		}else if(head.token == Token.NBT){
		
			sub.setType(CType.NBT);
			subs.add(sub);
			
			if(optional)
				sub.setIsOptiona(true);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
				
		}else if(head.token == Token.EXECUTE){
		
			sub.setType(CType.EXECUTE);
			subs.add(sub);
			
			if(optional)
				sub.setIsOptiona(true);
			
			checkNext(subs, cmd);
			addToList(cmd, subs);
		}else if(head.token == Token.OPTIONAL){
		
			optional = true;
		
			nextToken();
			addSubCommand(cmd);
			
		}else if(head.token == Token.EPSILON){
			nextStream();
			addSubCommand(cmd);
		}
		
	}
	
	private void addToList(List<Command> main, List<Command> sub){
		
		for(Command c : main){
			for(Command subC : sub){
				c.addNext(subC);
			}
		}
		
	}
	
	private void nextElement(List<Command> cmd){
		
		nextToken();
		consumeSpace();
		
		 if(head.token == Token.TEXT || head.token == Token.ALL){
			 addSubCommand(cmd);
		 }else if(head.token == Token.EPSILON){
			 nextStream();
			 if(head.token != Token.CLOSE_CURLY_BRACKETS){
				 nextElement(cmd);
			 }
		 }else{
			 return;
		 }
		
	}
	
	private void addSep(List<Command> cmd){
		
		Command sub = new Command();
		
		if(head.token == Token.TEXT){
			
			sub.setCommand(head.sequence.substring(1,  head.sequence.length() - 1));
			sub.setType(CType.COMMAND);
			cmd.add(sub);
			
		}else if(head.token == Token.EPSILON){
			nextStream();
			consumeSpace();
			addSep(cmd);
		}
		
	}
	
	private void consumeSpace(){
		if(head.token == Token.SPACE){
			nextToken();
		}
	}
	
	private void closeCurlyBracket(){
		
		if(head.token == Token.CLOSE_CURLY_BRACKETS){
			nextToken();
		}else{
			error();
		}
	}
	
	public void nextStream(){
		
		list++;
		line++;
		
		if(!(tokenStream.size() <= list)){
			tokens.clear();
			tokens = (LinkedList<Token>) tokenStream.get(list).clone();
			
			if(tokens.isEmpty()){
				head = new Token(Token.EPSILON, "");
			}else{
				head =tokens.getFirst();
			}
			
		}else{
			end = true;
		}		
	}	
	
	private void nextToken(){

		try{
			tokens.pop();
				
				if(tokens.isEmpty()){
					head = new Token(Token.EPSILON, "");
				}else{
					head = tokens.getFirst();
				}
			
			}catch(NoSuchElementException ex){		
				error();
			}
	}
	
	
	private void error(){
		if(head.token != Token.ERROR){
			String token = head.sequence;
			head = new Token(Token.ERROR, "");
			JOptionPane.showMessageDialog(new JDialog(), "Couldn't parse syntax \n Unexpected Token:" + token, "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
	
}
