package com.lexxie.cbs2.utilities.variables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.project.ProjectManager;

public class VariableFileHandler {

	/**
	 * 	load scores into ScoreHandler for every project
	 * */
	public static void loadVariables(){
		
		File scores = new File("Variables");
		
		if(scores.exists()){
			
			File[] subFiles = scores.listFiles();
			
			if(subFiles != null){
				for(File file : subFiles){
					
					ProjectManager project = ProjectManager.getProjectByName(file.getName());
					
					BufferedReader reader = null;
					
					try{
							reader = new BufferedReader(new FileReader(file));
							
							String line = "";
						
							while((line = reader.readLine()) != null){
							
								String[] parts = line.split(";");
								
								String varName = parts[0];
								String value = parts[1];
								
								project.getVariableHandler().addVariables(varName, value);
								
							}
							
							reader.close();
						
					}catch(IOException ex){
						
					}
					
				}
			}
			
		}
	}
	
	
	/**
	 * 	Save all scores
	 * */
	public static void saveVariables(){
		
		File scoreFolder = new File("Variables");
		
		if(!scoreFolder.exists()){
			scoreFolder.mkdir();
		}
		
		for(ProjectManager project : ProjectManager.projects){
			
			String pFolder = project.getName();
			File pScoreFile = new File("Variables\\" + pFolder);
			
			PrintWriter writer = null;
			
			try{
				 writer = new PrintWriter(pScoreFile);
				
				 for(Entry<String, String> entry : project.getVariableHandler().getVariables().entrySet()){
					 
					 String varName = entry.getKey();
					 String value = entry.getValue();
					 
					 writer.write(varName + ";" + value + "\n"); 
				 }
				 
			}catch(IOException ex){
				JOptionPane.showMessageDialog(new JDialog(), "Couldn't save Variables for Project \"" + project.getName() + "\"");
			}finally{
				writer.flush();
				writer.close();
			}	
		}
	}
	
	public static void deleteFile(String name){
		
		File file = new File("Variables\\" + name);
		file.delete();
		
	}
	
}
