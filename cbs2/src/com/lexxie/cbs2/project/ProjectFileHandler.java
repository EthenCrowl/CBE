package com.lexxie.cbs2.project;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.lexxie.cbs2.gui.main.MainWindow;
import com.lexxie.cbs2.project.ProjectManager.ExecuteData;
import com.lexxie.cbs2.tabs.ProjectTab;
import com.lexxie.cbs2.utilities.scoreboard.ScoreFileHandler;

public class ProjectFileHandler {

	/**
	 * 	Loads Projects
	 * */
	public static void loadProjects(String path, DefaultMutableTreeNode parent){
		
		File file = new File(path);
		File[] subFiles = file.listFiles();
		if(subFiles != null)
			for(File f : subFiles){
				if(f.isDirectory()){
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(f.getName());
					parent.add(node);
					ProjectManager.projects.add(new ProjectManager(f.getName()));
					loadProjects(f.getAbsolutePath(), node);
				}else{
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(f.getName());
					parent.add(node);
			}
		}
		
	}
	
	/**
	 * 	clears project list
	 * */
	public static void clearProjectList(){
		ProjectManager.projects.clear();
	}
	
	public static void saveAll(){
		
		JTabbedPane pane = MainWindow.getApplication().getTabbedCodePane();
		
		for(int i = 0; i < pane.getTabCount(); i++){
			Component comp = pane.getComponentAt(i);
			
			if(comp instanceof JScrollPane){
				
				comp = ((JScrollPane) comp).getViewport().getView();
				
				saveFile(ProjectTab.getTab(i), pane.getTitleAt(i), ((JEditorPane) comp).getText());
				
			}else{
				JOptionPane.showMessageDialog(new JDialog(), "Couldn't save " + pane.getTitleAt(i));
			}
			
		}
		
	}
	
	public static void saveFile(String project, String fileName, String text){
		
		File file = new File("Projects\\" + project + "\\" + fileName);
		
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(file);
			
			writer.write(text);
			
		}catch(IOException ex){
			
		}finally{
			writer.flush();
			writer.close();
		}
		
	}
	
	public static void openFile(JTree projectTree){
		
		String path = "";
		
		if(projectTree.getSelectionPath().toString() != null){
			path = projectTree.getSelectionPath().toString().replaceAll("[\\[\\]]", "").replace(", ",  "\\");
		}
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			File file = new File(path);
			
			int dOption = JOptionPane.showConfirmDialog(null, "Open File?", "Info", JOptionPane.YES_NO_OPTION);
			if(dOption == JOptionPane.YES_OPTION){
				
				String line;
				String code = "";
				
				while((line = br.readLine()) != null){
					code += line + "\n";
				}
				
				MainWindow.getApplication().openTab(file.getParentFile().getName(), file.getName(), code);
				
			}
			
			br.close();
			
		}catch(IOException ex){
			 Object object = projectTree.getLastSelectedPathComponent();
             if (object instanceof DefaultMutableTreeNode) {
                 Object userObject = ((DefaultMutableTreeNode) object).getUserObject();
                 //If instanceof of File
                 if (userObject instanceof File) {
                 	JOptionPane.showConfirmDialog(new JDialog(), "Couldn't load File:" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                 }
                 //else ignore
             }
		}
		
	}

	public static void createFolder(String folderName){
		
		File pFile = new File("Projects");
		
		if(!pFile.exists())
			pFile.mkdir();
		
		File folder = new File("Projects\\" + folderName);
		
		if(!folder.exists())
			try{
				folder.mkdir();
				
				DefaultTreeModel model = (DefaultTreeModel) MainWindow.getApplication().getProjectTree().getModel();
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
				node.add(new DefaultMutableTreeNode(folderName));
				model.reload();
			}catch(SecurityException ex){
				
			}
		
	}
	
	public static void createFile(String fileName){
	
		String project = ProjectManager.activeProject.getName();
		File folder = new File("Projects\\" + project);
		File file = new File("Projects\\" + project + "\\" + fileName);
	
		file.getParentFile();
		try{
			file.createNewFile();
			DefaultTreeModel model = (DefaultTreeModel) MainWindow.getApplication().getProjectTree().getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		
			Enumeration children = root.children();
			while(children.hasMoreElements()){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
				if(node.toString().equals(project)){
					DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(fileName);
					node.add(newFile);
					model.nodesWereInserted(node, new int[] {node.getChildCount() - 1} );
					break;
				}
			}	
		}catch(IOException ex){
		
		}
	}
	
	public static boolean delete(String path, String name){
		
		path = path.replaceAll("[\\[\\]]", "").replace(", ",  "\\");
		
		File file = new File(path);
		
		if(file.isDirectory()){
			
			int continueOption = JOptionPane.showConfirmDialog(new JDialog(), "Are you sure you want to delete this Project : " + name + "?", "Delete", JOptionPane.YES_NO_OPTION);
			if(continueOption == JOptionPane.NO_OPTION)
				return false;
		}else{
			int continueOption = JOptionPane.showConfirmDialog(new JDialog(), "Are you sure you want to delete this File : " + name + "?", "Delete", JOptionPane.YES_NO_OPTION);
			if(continueOption == JOptionPane.NO_OPTION)
				return false;
		}
		
		if(file.isDirectory()){
			ScoreFileHandler.deleteFile(file.getName());
			
			String[] entities = file.list();
			for(String s : entities){
				File currentFile = new File(file.getPath(), s);
				currentFile.delete();		
			}
			
			ProjectManager.setActiveProjectNull();
		}
		
		file.delete();
	
		DefaultTreeModel model = (DefaultTreeModel) MainWindow.getApplication().getProjectTree().getModel();
		
		TreePath[] paths = MainWindow.getApplication().getProjectTree().getSelectionPaths();
		for(TreePath treePath :paths){
		
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
			if(node.getParent() != null){
				model.removeNodeFromParent(node);
			}
			
		}
		
		return true;
		
	}
	
}
