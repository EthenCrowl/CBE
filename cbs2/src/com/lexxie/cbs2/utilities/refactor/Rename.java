package com.lexxie.cbs2.utilities.refactor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.lexxie.cbs2.gui.main.MainWindow;
import com.lexxie.cbs2.project.ProjectFileHandler;
import com.lexxie.cbs2.project.ProjectManager;
import com.lexxie.cbs2.project.ProjectManager.ExecuteData;

public class Rename {

	
	/**
	 * 	Takes two Strings and renames all occures of 'old' in all projects files to 'new'
	 * 	@param String - old name
	 * 	@param String - rename with
	 * 
	 * */
	public static void renameForProject(String old, String newS){
		
		File projectFile = new File("Projects\\" + ProjectManager.activeProject.getName());
		
		File[] subFiles = projectFile.listFiles();
		
		for(File file : subFiles){			
			try{
				BufferedReader br = new BufferedReader(new FileReader(file));
				
				String line = "";
				String content = "";
				
				while((line = br.readLine()) != null){
					content += line + "\n";
				}
				
				br.close();
				
				if(!content.isEmpty()){
					PrintWriter writer = new PrintWriter(file);
					
					content = content.replaceAll(old, newS);
					
					writer.write(content);
					
					writer.flush();
					writer.close();
				}
				
				//rename tabs
				
				int tabs = MainWindow.getApplication().getTabbedCodePane().getTabCount();
				
				for(int i = 0; i < tabs; i++){
					
					if(MainWindow.getApplication().getTabbedCodePane().getTitleAt(i).equals(file.getName())){
						
						JViewport viewport = ((JScrollPane) MainWindow.getApplication().getTabbedCodePane().getComponentAt(i)).getViewport();
						JEditorPane editor = (JEditorPane) viewport.getView();
						
						editor.setText(editor.getText().replaceAll(old, newS));
						
					}
				}
				
			}catch(IOException ex){
				
			}	
		}	
	}
	
	public static void renameFile(JTree tree){
		
		String rename = tree.getSelectionModel().getSelectionPath().getLastPathComponent().toString();
		
		if(!ProjectManager.isProject(rename)){
			RenameFile rf = new RenameFile(rename, 
					tree.getSelectionModel().getSelectionPath().getPathComponent(tree.getSelectionModel().getSelectionCount()).toString(), tree);
			rf.setModal(true);
			rf.setVisible(true);
		}else{
			RenameProject rP = new RenameProject(rename, tree);
			rP.setModal(true);
			rP.setVisible(true);
		}
		
	}
	
}
