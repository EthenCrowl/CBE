package com.lexxie.cbs2.autosuggestor;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;

import com.lexxie.cbs2.commands.Command;
import com.lexxie.cbs2.commands.Command.CType;

public class Autosuggestor {

	public static void suggest(String line, int caretline, int caret, SuggestionBox box, JEditorPane codePane){
		
		box.removeAll();
		
		String[] suggestion = createSuggestion(line, caretline);
		
		Rectangle location = null;
		try {
			 location = codePane.modelToView(caret);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		if(suggestion != null){
			box.addItems(suggestion);
		
			box.setLocation(location);
			box.setSize();
		
		
			box.setVisible(true);
		}else{
			box.setVisible(false);
		}
	}
	
	private static String[] createSuggestion(String line, int caretLine){
		
		
		String[] parts = line.split("\\s");
	
		if(doSuggestion(line, getWordOnCaret(line, caretLine))){
			//System.out.println("suggest!");
			try{
				if(line.charAt(caretLine - 1) == ' '){
					//System.out.println("suggestall");
				
					parts[0] = parts[0].substring(1, parts[0].length());
				
					String[] suggest = suggestAll(parts, line, caretLine);
				
					if(!(suggest == null))
						return suggest;
				
				}else if(parts.length == 1 && !(line.charAt(caretLine - 1) == ' ')){
					//remove '#'
					parts[0] = parts[0].substring(1, parts[0].length());
			
					//System.out.println("suggest in between:" + parts[0]);
				
					List<String> commands = getCommands(parts[0]);
				
					String[] suggestion = new String[commands.size()];
					suggestion = commands.toArray(suggestion);
			
					if(!(suggestion.length == 0))
						return suggestion;
			
				}else{
				//System.out.println("suggest another");
				
					parts[0] = parts[0].substring(1, parts[0].length());
				
					String[] suggestions = suggestCommandOnCaret(parts, line, caretLine);
			
					if(suggestions != null){
						return suggestions;
					}
				}
			}catch(StringIndexOutOfBoundsException ex){
				System.out.println("null");
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * 	takes the current line and the position of the caret in the line
	 * 	return the word, the caret is actual in between
	 * 	@param the current line
	 * 	@param caret position relative to line
	 * */
	private static String getWordOnCaret(String line, int caret){	
		
		for(int i = caret - 1; i >= 0; i--){
			try{
				if(line.charAt(i) == ' '){
					String word = line.substring(i);
				
					String[] words = word.split("\\s+");
					try{		
						return words[1];
					}catch(ArrayIndexOutOfBoundsException ex){
						return null;
					}
				}else if(line.charAt(i) == '#'){		
					String word = line.substring(i);
				
					String[] words = word.split("\\s+");	
								
					return words[0].substring(1, words[0].length());			
				}
			}catch(StringIndexOutOfBoundsException ex){
				return null;
			}
		}
	return null;	
}
	
	/**
	 * 	return if there should be a suggestion
	 * 	@param current line
	 * 	@param word, the caret is currently in between
	 * */
	private static boolean doSuggestion(String line, String word){
		
		String[] parts = line.split("\\s+");

		parts[0] = parts[0].substring(1, parts[0].length());
		
		Command root;
		
		for(Command command : Command.syntax){
			if(command.getCommand().equals(parts[0])){
				root = command;
					
				if(parts[0].equals(word)){
					return false;
				}else{
					for(int i = 1; i < parts.length; i++){
						
						root = getSubCommand(root, parts[i]);
						
					}
					
					if(root != null && word != null){
						if(root.getType() == CType.COMMAND && root.getCommand().equals(word)){
							return false;
						}else if(root.getType() == CType.ALL){
							return false;
						}else if(root.getType() == CType.COORDIANTES){
							return false;
						}else if(root.getType() == CType.NUMBER){
							return false;
						}else if(root.getType() == CType.NBT){
							return false;
						}
					}
					
				}
			}
			
		}
		
		return true;
		
	}
	
	private static String[] suggestAll(String[] parts, String line, int caretLine){
		
		Command root;
	
		for(Command cmd : Command.syntax){
			
			if(cmd.getCommand().equals(parts[0])){
				
				root = cmd;
				
				for(int i = 1; i < parts.length; i++){
					
					root = getSubCommand(root, parts[i]);
					
					if(root.getType() == CType.EXECUTE){
						
						String newLine = "#" + parts[i] + " ";
						for(int j = i+1; j < parts.length; j++){
								newLine += parts[j] + " ";
						}
						
						return createSuggestion(newLine, newLine.length());
					}
						
				}
				
				List<Command> commands = new ArrayList<Command>();
				
				try{
					commands = root.getAllNextCommands();
				}catch(NullPointerException ex){
					return null;
				}
				
				if(commands.size() == 1){
					if(commands.get(0).getType() == CType.ALL){
						return null;
					}else if(commands.get(0).getType() == CType.PARAMETER){
						return null;
					}else if(commands.get(0).getType() == CType.TEXT){
						return null;
					}else if(commands.get(0).getType() == CType.COORDIANTES){
						return null;
					}else if(commands.get(0).getType() == CType.NUMBER){
						return null;
					}else if(commands.get(0).getType() == CType.NBT){
						return null;
					}else if(commands.get(0).getType() == CType.EXECUTE){
						System.out.println("test");
						return createSuggestion("#", 1);
					}
				}else if(commands.size() == 0){
					return null;
				}
				
				List<Command> copy = new ArrayList<Command>();
				
				for(Command c : commands){
					if(c.getType() == CType.COMMAND){
						copy.add(c);
					}else if(c.getType() == CType.EXECUTE){
						for(Command syntax : Command.syntax){
							copy.add(syntax);
						}
					}
				}
				
				commands.clear();
				
				commands = copy;
				
				String[] suggestions = new String[commands.size()];
				
				for(int i = 0; i < suggestions.length; i++){
					suggestions[i] = commands.get(i).getCommand();
				}
				
				return suggestions;
				
			}
			
		}
		
		return null;
	}
	
	/**
	 * 	returns a list out of commands to suggest
	 * 
	 * */
	private static String[] suggestCommandOnCaret(String[] parts, String line, int caretLine){
		
		Command root;
		
		for(Command command : Command.syntax){
			
			if(command.getCommand().equals(parts[0])){
				
					root = command;
					int i = 1;
						do{
							if(i < parts.length - 1){
						
								root = getSubCommand(root, parts[i]);
								
								if(root != null && root.getCommandsMap().size() >= 1){
									if(root.containsExecute() && parts.length > i+1 && parts[i+1].charAt(0) != 'd'){
				
										String newLine = "#";
										for(int j = i+1; j < parts.length; j++){
											if(j < parts.length - 1){
												newLine += parts[j] + " ";
											}else{
												newLine += parts[j];
											}
										}
										
										return createSuggestion(newLine, newLine.length());
									}
								}
								
							}else if(i == parts.length){
								//no string - show all possibility's
								System.out.println("parameter!");
								List<Command> commands = root.getAllNextCommands();
							
								String[] suggestions = new String[commands.size()];
								
								for(int j = 0; j < suggestions.length; j++){
									suggestions[j] = commands.get(j).getCommand();
								}
								
								return suggestions;
								
							}else{
								//return command
								if(root != null && root.getCommandsMap().size() > 1){
									List<Command> commands = new ArrayList<Command>();
								
									try{
										commands = root.getNextCommands(parts[i]);
									}catch(NullPointerException ex){
										return null;
									}
									
									String[] suggestions = new String[commands.size()];
						
									for(int j = 0; j < suggestions.length; j++){
										suggestions[j] = commands.get(j).getCommand();
									}
								
									return suggestions;
								}else{
									
									if(root != null && root.getCommandsMap().size() == 1 && root.getAllNextCommands().get(0).getType() != CType.PARAMETER
											&& root.getAllNextCommands().get(0).getType() != CType.TEXT){					
											
											List<Command> commands = new ArrayList<Command>();
											
											try{
												commands = root.getNextCommands(parts[i]);
											}catch(NullPointerException ex){
												return null;
											}
											
											String[] suggestions = new String[commands.size()];
								
											for(int j = 0; j < suggestions.length; j++){
												suggestions[j] = commands.get(j).getCommand();
											}
										
											return suggestions;
											
										}else{
											
											return null;
											
										}							
									}
								}
								
							i++;
						}while(i < parts.length);
					
					}
			}		
		return null;
	}
	
	
	private static Command getSubCommand(Command cmd, String key){
		
		if(cmd != null && cmd.getCommandsMap().size() > 1){
			if(!cmd.getNextCommands(key).isEmpty()){
				return cmd.getNextCommand(key);
			}else if(cmd != null && cmd.getNextAllCommand() != null){
				return cmd.getNextAllCommand();
			}else if(cmd != null && cmd.containsExecute())
				return cmd.getNextExecuteCommand();
		}else if(cmd != null && cmd.getCommandsMap().size() == 1){
			
			List<Command> commands = cmd.getAllNextCommands();
			
			if(!commands.isEmpty()){
				Command c = commands.get(0);
				
				if(c.getType() == CType.ALL){
					return c;
				}else if(c.getType() == CType.PARAMETER){
					return c;
				}else{
					return c;
				}
			}
			
		}
		
		return null;
	}
	
	private static List<String> getCommands(String search){
	
		List<String> suggestions = new ArrayList<String>();
		
		for(Command command : Command.syntax){
			
			if(command.getCommand().startsWith(search)){
				suggestions.add(command.getCommand());
			}
			
		}
			
		return suggestions;
	}
	
	public static void complete(JEditorPane codePane, SuggestionBox box, int caretLine, String word){
		
		String selection = box.getList().getSelectedValue().toString();
		
		String complet = selection.substring(word.length() - 1);
		
		try {
			codePane.getDocument().insertString(codePane.getCaretPosition(), complet, null);
		} catch (BadLocationException e) {
			
		}
		
		
		
	}
	
	
	
}
