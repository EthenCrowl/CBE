package com.lexxie.cbs2.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.commands.syntax.CParser;
import com.lexxie.cbs2.commands.tokenizer.CInitialize;
import com.lexxie.cbs2.commands.tokenizer.CTokenizer;
import com.lexxie.cbs2.commands.tokenizer.CTokenizer.Token;

public class ReadCommands {

	public static void read(){
		
		File cmd = new File("Options\\Syntax");
		
		File[] files = cmd.listFiles();
		
		ArrayList<LinkedList<Token>> syntax = new ArrayList<LinkedList<Token>>();
		
		if(files != null)
		for(File file : files){
			
			try{
				
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line = "";
				
				while((line = br.readLine()) != null){
					
					CTokenizer tokenizer = new CTokenizer();
					CInitialize.addExo(tokenizer);
					tokenizer.tokenize(line);
					syntax.add(tokenizer.getTokens());
					
				}
				
				br.close();
				
			}catch(IOException ex){
				JOptionPane.showMessageDialog(new JDialog(), "Couldn't load commands");
			}
			
		}	
		
		if(syntax.size() > 0){
			CParser crafter = new CParser();
			crafter.parse(syntax);
		}else{
			JOptionPane.showMessageDialog(new JDialog(), "Couldn't find commands, autocomplete desabled");
		}
		
	}
	
}
