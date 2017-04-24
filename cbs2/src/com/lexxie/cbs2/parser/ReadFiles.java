package com.lexxie.cbs2.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.parser.parse.Parser;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer;
import com.lexxie.cbs2.parser.tokenizer.Tokenizer.Token;
import com.lexxie.cbs2.project.ProjectManager;

public class ReadFiles {

	public static boolean readFile(){
		if(ProjectManager.activeProject != null){
			
			ProjectManager project = ProjectManager.activeProject;
			
			File projectFolder = new File("Projects\\" + project.getName());
			
			File[] projectFiles = projectFolder.listFiles();
			
			ArrayList<LinkedList<Token>> code = new ArrayList<LinkedList<Token>>();
			
			for(File file : projectFiles){
				
				try{
					
					BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
					
					String line = "";
					
					while((line = br.readLine()) != null){
						
						Tokenizer tokenizer = new Tokenizer();
						Initialize.addExp(tokenizer);
						tokenizer.tokenize(line);
						code.add(tokenizer.getTokens());
						
					}
					
					br.close();
					
				}catch(IOException ex){
					JOptionPane.showMessageDialog(new JDialog(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
			Parser parser = new Parser();
			parser.parse(code);
			
			if(parser.head.token == Token.ERROR){
				return false;
			}else{
				return true;
			}
				
			
		}else{
			return false;
		}
		
	}
	
}
