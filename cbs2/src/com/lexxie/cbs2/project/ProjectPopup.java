package com.lexxie.cbs2.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;

import com.lexxie.cbs2.gui.main.CreateExecute;
import com.lexxie.cbs2.gui.main.CreateProject;
import com.lexxie.cbs2.gui.main.MainWindow;
import com.lexxie.cbs2.utilities.refactor.Rename;

public class ProjectPopup extends JPopupMenu{

	JMenu mNew;
	JMenuItem project, execute;
	JMenuItem setProject;
	JMenuItem rename;
	JMenuItem delete;
	
	public ProjectPopup(final JTree tree){
		
		mNew = new JMenu("New ");
		
		project = new JMenuItem("Project");
		project.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Project.png")));
		project.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				CreateProject cE = new CreateProject();
				cE.setVisible(true);
			}
			
		});
		
		execute = new JMenuItem("Execute");
		execute.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Execute.png")));
		execute.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				CreateExecute cE = new CreateExecute();
				cE.setVisible(true);
				
			}
			
		});
		
		setProject = new JMenuItem("Set active Project");
		setProject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(!ProjectManager.setActiveProject(tree.getSelectionModel().getSelectionPath().getLastPathComponent().toString())){
					ProjectManager.setActiveProject(tree.getSelectionModel().getLeadSelectionPath().getPathComponent(tree.getSelectionModel().getLeadSelectionPath().getPathCount() - 2).toString());   			
			}
			
			}});
		
		rename = new JMenuItem("Rename");
		rename.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Rename.renameFile(tree);
			}
			
		});
		
		delete = new JMenuItem("Delete");
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ProjectFileHandler.delete(MainWindow.getApplication().getProjectTree().getSelectionPath().toString(), 
										MainWindow.getApplication().getProjectTree().getLastSelectedPathComponent().toString());
			}
			
		});
		
		mNew.add(project);
		mNew.add(execute);
		
		add(mNew);
		add(setProject);
		add(new JSeparator());
		add(rename);
		add(delete);
		
	}
	
}
