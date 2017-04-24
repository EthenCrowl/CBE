package com.lexxie.cbs2.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.lexxie.cbs2.gui.main.MainWindow;
import com.lexxie.cbs2.utilities.scoreboard.ScoreHandler;
import com.lexxie.cbs2.utilities.variables.VariableHandler;

/**
 * 	handles all Projects and Project files
 * */
public class ProjectManager {

	public static List<ProjectManager> projects = new ArrayList<ProjectManager>();
	
	public static ProjectManager activeProject;
	
	public static void addProject(ProjectManager project){
		projects.add(project);
	}
	
	/**
	 * 	Saves all Projects
	 * */
	public static void saveProjects(){
		
	} 
	
	public static boolean isProject(String name){
		for(ProjectManager p : projects){
			if(p.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public static ProjectManager getProjectByName(String name){
		for(ProjectManager p : projects){
			if(p.getName().equals(name)){
				return p;
			}
		}
		return null;
	}
	
	/**
	 * Opens a Project
	 * */
	public static boolean setActiveProject(String projectName){
		for(ProjectManager p : projects){
			if(p.getName().equals(projectName)){
				activeProject = p;
				JOptionPane.showMessageDialog(new JDialog(), "Active Project: " + projectName);
				MainWindow.getApplication().updateTitle();
				return true;
			}
		}
		return false;
	}
	
	public static void setActiveProjectNull(){
		activeProject = null;
	}
	
	//Class
	
	private String projectName;
	private ScoreHandler scoreHandler;
	private VariableHandler variableHandler;
	
	private List<ExecuteData> executeList = new ArrayList<ExecuteData>();
	
	public ProjectManager(String projectName){
		
		this.projectName = projectName;
		scoreHandler = new ScoreHandler();
		variableHandler = new VariableHandler();
	
	}
	
	/**
	 * 	Add a new subfile to the Project
	 * */
	public void addExecute(String name){
		executeList.add(new ExecuteData(name));
	}
	
	public String getName(){
		return projectName;
	}
	
	public ScoreHandler getScoreHandler(){
		return scoreHandler;
	}
	
	public VariableHandler getVariableHandler(){
		return variableHandler;
	}
	
	public List<ExecuteData> getSubFiles(){
		return executeList;
	}
	
	/**
	 * represents a file form the project
	 * */
	public class ExecuteData{
		
		private String name;
		
		public ExecuteData(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
		
	}
	
}
