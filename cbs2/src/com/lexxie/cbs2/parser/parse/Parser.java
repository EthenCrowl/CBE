package com.lexxie.cbs2.parser.parse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lexxie.cbs2.commands.validator.Validator;
import com.lexxie.cbs2.gui.main.MainWindow;
import com.lexxie.cbs2.parser.PlaceArrays;
import com.lexxie.cbs2.parser.PlaceFunctions;
import com.lexxie.cbs2.parser.PlaceVariables;
import com.lexxie.cbs2.parser.data.commands.Array;
import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.parser.data.nbt.Builder;
import com.lexxie.cbs2.parser.data.nbt.NBTSequencer;
import com.lexxie.cbs2.parser.data.nbt.SchematicSequencer;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;


public class Parser {

	public ArrayList<LinkedList<Token>> tokenStream;
	public LinkedList<Token> tokens;
	public List<BlockData> commands;
	public Token head;
	public int list, line;
	public boolean end, added;
	
	public String error;
	
	public Parser() { 
		commands = new LinkedList();
		
		list = 0;
		line = 1;
		
		end = false;
		added = false;
	}
	
	/**
	 * 	Parse tokenized String
	 * 	@param tokenStream
	 * */
	public void parse(ArrayList<LinkedList<Token>> tokenStream){
		
		Execute.clearList();
		Init.clearList();
		
		this.tokenStream = (ArrayList<LinkedList<Token>>) tokenStream.clone();
		
		if(tokenStream.isEmpty()){
			Utilities.error(this);
		}else if( list >= tokenStream.size()){
			Utilities.error(this);
		}else{
			this.tokens = (LinkedList<Token>) tokenStream.get(list).clone();
			try{
				head = tokens.getFirst();
			}catch(NoSuchElementException ex){
				Utilities.nextStream(this);
			}
			
			start();
		}
		
		PlaceFunctions.failedToParse = false;
		PlaceFunctions.placeFunction();
		
		if(!PlaceFunctions.failedToParse && head.token != Token.ERROR && MainWindow.getApplication().doCreate()){
			PlaceVariables.placeVariables();
			PlaceArrays.placeArrays();
			
			if(MainWindow.getApplication().doValidate()){
				Validator validator = new Validator();
				validator.initPattern();
				validator.validateAll();
			
				if(validator.getWarningCount() > 0){
				
					JPanel panel = new JPanel();

					panel.add(new JLabel("Found " + validator.getWarningCount() + " possible issues"));
					Object[] options = {"Show Detailed Info", "Ignore and proceed"};
				
					int result = JOptionPane.showOptionDialog(null, panel, "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, null);
				
					if(result == JOptionPane.YES_OPTION){
						validator.showWarnings();
					}else{
						if(!(Builder.getAlgo() == Builder.algorithmus.nbt)){
							SchematicSequencer seq = new SchematicSequencer();
							seq.make();
						}else{
							NBTSequencer seq = new NBTSequencer();
							seq.make();
						}
					}
				
				}else{
					if(!(Builder.getAlgo() == Builder.algorithmus.nbt)){
						SchematicSequencer seq = new SchematicSequencer();
						seq.make();
					}else{
						NBTSequencer seq = new NBTSequencer();
						seq.make();
					}
				}
			}else{
				if(!(Builder.getAlgo() == Builder.algorithmus.nbt)){
					SchematicSequencer seq = new SchematicSequencer();
					seq.make();
				}else{
					NBTSequencer seq = new NBTSequencer();
					seq.make();
				}
			}
		}else{
			PlaceVariables.placeVariables();
			PlaceArrays.placeArrays();	
		}
		
	}
	
	public void start(){
		
		if(head.token == Token.COMMENT){
			if(!end){ 
				Utilities.nextStream(this); 
				start(); 
			}else{
				return;
			}
		}else if(head.token == Token.DEFINE_FUNCTION){
			Utilities.nextToken(this);
			ParseFunction.parseFunction(this);
			start();
		}else if(head.token == Token.EXECUTE){
			Utilities.nextToken(this);
			ParseExecute.parseExecute(this);
			start();
		}else if(head.token == Token.INIT){
			Utilities.nextToken(this);
			ParseInit.parseInit(this);
			start();
		}else if(head.token == Token.DEFINE_VARIABLE){
			ParseVariable.defineVar(this);
			start();
		}else if(head.token == Token.ASSIGN){
		
			Utilities.nextToken(this);
			ParseVariable.reassignVariable(this);
			start();
			
		}else if(head.token == Token.SPACE){
			Utilities.nextToken(this);
			start();
		}else if(head.token == Token.EPSILON){
			if(!end){
				Utilities.nextStream(this);
				start();
			}else{
				return;
			}
		}else{
			Utilities.error(this);
		}
		
	}
		
}
	

