package com.lexxie.cbs2.gui.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import com.lexxie.cbs2.project.ProjectFileHandler;
import com.lexxie.cbs2.project.ProjectManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateProject extends JDialog {
	private JTextField projectName;

	/**
	 * Create the dialog.
	 */
	public CreateProject() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(CreateProject.class.getResource("/icon/Project.png")));
		setTitle("New Project");
		setBounds(100, 100, 198, 119);
		getContentPane().setLayout(null);
		
		projectName = new JTextField();
		projectName.setBounds(10, 31, 177, 20);
		getContentPane().add(projectName);
		projectName.setColumns(10);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(projectName != null && !projectName.getText().isEmpty()){
					createNewProject(projectName.getText());
					dispose();
				}else{
					JOptionPane.showMessageDialog(new JDialog(), "Project needs a name");
				}
			}
		});
		btnCreate.setBounds(10, 57, 89, 23);
		getContentPane().add(btnCreate);
		
		JButton btnCancle = new JButton("Cancel");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancle.setBounds(109, 57, 78, 23);
		getContentPane().add(btnCancle);
		
		JLabel lblName = new JLabel("Project Name:");
		lblName.setBounds(10, 11, 89, 20);
		getContentPane().add(lblName);
	}
	
	private void createNewProject(String name){
		
		ProjectFileHandler.createFolder(name);
		
		ProjectManager.addProject(new ProjectManager(name));
		ProjectManager.setActiveProject(name);
		
	}
}
