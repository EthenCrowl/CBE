package com.lexxie.cbs2.utilities.scoreboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.lexxie.cbs2.project.ProjectManager;

public class ScoreFileHandler {

	
	/**
	 * 	load scores into ScoreHandler for every project
	 * */
	public static void loadScores(){
		
		File scores = new File("Scores");
		
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
							
								String[] parts = line.split(":");
								
								String scoreName = parts[0];
								String scoreType = parts[1];
								
								project.getScoreHandler().addScore(scoreName, scoreType);
								
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
	public static void saveScores(){
		
		File scoreFolder = new File("Scores");
		
		if(!scoreFolder.exists()){
			scoreFolder.mkdir();
		}
		
		for(ProjectManager project : ProjectManager.projects){
			
			String pFolder = project.getName();
			File pScoreFile = new File("Scores\\" + pFolder);
			System.out.println("folder:" + pFolder);
			PrintWriter writer = null;
			
			try{
				 writer = new PrintWriter(pScoreFile);
				
				 for(Entry<String, String> entry : project.getScoreHandler().getScores().entrySet()){
					 
					 String scoreName = entry.getKey();
					 String scoreType = entry.getValue();
					 
					 writer.write(scoreName + ":" + scoreType + "\n"); 
				 }
				
			}catch(IOException ex){
				JOptionPane.showMessageDialog(new JDialog(), "Couldn't save Scores for Project \"" + project.getName() + "\"");
			}finally{
				writer.flush();
				writer.close();
			}	
		}
	}
	
	public static void deleteFile(String name){
		
		File file = new File("Scores\\" + name);
		file.delete();
		
	}
	
}
