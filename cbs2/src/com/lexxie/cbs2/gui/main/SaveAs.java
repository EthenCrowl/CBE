package com.lexxie.cbs2.gui.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JList;

import com.lexxie.cbs2.project.ProjectFileHandler;
import com.lexxie.cbs2.project.ProjectManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

public class SaveAs extends JDialog {
	
	private JTextField tFFileName;
	private DefaultListModel lProjectsModel = new DefaultListModel();
	
	
	/**
	 * Create the dialog.
	 */
	public SaveAs() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(SaveAs.class.getResource("/icon/Save.png")));
		setTitle("Save As");
		setBounds(100, 100, 212, 317);
		getContentPane().setLayout(null);
		
		JLabel lblSelectProject = new JLabel("Select Project:");
		lblSelectProject.setBounds(10, 11, 70, 14);
		getContentPane().add(lblSelectProject);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 188, 187);
		getContentPane().add(scrollPane);
		
		final JList lProjects = new JList(lProjectsModel);
		scrollPane.setViewportView(lProjects);
		
		JLabel lblSaveAs = new JLabel("File name:");
		lblSaveAs.setBounds(10, 233, 54, 14);
		getContentPane().add(lblSaveAs);
		
		tFFileName = new JTextField();
		tFFileName.setBounds(62, 230, 136, 20);
		getContentPane().add(tFFileName);
		tFFileName.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tFFileName.getText().isEmpty() && lProjects.getSelectedValue() != null && MainWindow.getApplication().getTabbedCodePane().getSelectedComponent() != null){
					ProjectFileHandler.saveFile(lProjects.getSelectedValue().toString(), tFFileName.getText(), 
							((JEditorPane) ((JScrollPane) MainWindow.getApplication().getTabbedCodePane().getComponentAt(
										MainWindow.getApplication().getTabbedCodePane().getSelectedIndex())).getViewport().getView()).getText());
				
					DefaultTreeModel model = (DefaultTreeModel) MainWindow.getApplication().getProjectTree().getModel();
					DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
					
					Enumeration children = root.children();
					while(children.hasMoreElements()){
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) children.nextElement();
						
						if(node.toString().equals(lProjects.getSelectedValue().toString())){
							DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(tFFileName.getText());
							node.add(newFile);
							model.nodesWereInserted(node, new int[] {node.getChildCount() - 1});
							break;
						}
						
					}
					dispose();
				}
			}
		});
		btnSave.setBounds(10, 258, 89, 23);
		getContentPane().add(btnSave);
		
		JButton btnCancle = new JButton("Cancle");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancle.setBounds(109, 258, 89, 23);
		getContentPane().add(btnCancle);
		
		loadProjects();
	}
	
	private void loadProjects(){
		for(ProjectManager project : ProjectManager.projects)
			lProjectsModel.addElement(project.getName());
	}
	
}
