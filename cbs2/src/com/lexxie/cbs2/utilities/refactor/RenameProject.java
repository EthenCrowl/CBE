package com.lexxie.cbs2.utilities.refactor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.lexxie.cbs2.gui.main.MainWindow;
import com.lexxie.cbs2.project.ProjectFileHandler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class RenameProject extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tFName;

	/**
	 * Create the dialog.
	 */
	public RenameProject(final String name, final JTree tree) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RenameProject.class.getResource("/icon/Project.png")));
		setTitle("Rename Project");
		setBounds(100, 100, 222, 116);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewName = new JLabel("New name:");
		lblNewName.setBounds(10, 11, 64, 14);
		contentPanel.add(lblNewName);
		
		
		tFName = new JTextField();
		tFName.setBounds(10, 28, 188, 20);
		contentPanel.add(tFName);
		tFName.setColumns(10);
		tFName.setText(name);
		
		
		JButton btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rename(name, tree);
			}
		});
		btnRename.setBounds(10, 52, 89, 23);
		contentPanel.add(btnRename);
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(109, 52, 89, 23);
		contentPanel.add(btnCancel);
		
	}

	private void rename(String name, JTree tree){
		
		File orig = new File(MainWindow.getApplication().getPath() + "\\" + name);
		File newP = new File(MainWindow.getApplication().getPath() + "\\" + tFName.getText());
		
		if(newP.exists()){
			JOptionPane.showMessageDialog(new JDialog(), "File already exist", "Couldn't rename", JOptionPane.ERROR_MESSAGE);
		}else{
			
			boolean success = orig.renameTo(newP);
			
			if(!success){
				JOptionPane.showMessageDialog(new JDialog(), "Coudn't rename File", "Error", JOptionPane.ERROR_MESSAGE);
			}else{
				
				File origScore = new File("Scores\\" + name);
				File newScore = new File("Scores\\" + tFName.getText());
				
				File origVar = new File("Variables\\" + name);
				File newVar = new File("Variables\\" + tFName.getText());
				
				boolean sScore = origScore.renameTo(newScore);
				boolean sVar = origVar.renameTo(newVar);
				
				if(!sScore)
					JOptionPane.showMessageDialog(new JDialog(), "Coudn't rename Score File", "Error", JOptionPane.ERROR_MESSAGE);
				
				if(!sVar)
					JOptionPane.showMessageDialog(new JDialog(), "Coudn't rename Variable File", "Error", JOptionPane.ERROR_MESSAGE);
				
				((DefaultMutableTreeNode) tree.getModel().getRoot()).removeAllChildren();
				((DefaultTreeModel) tree.getModel()).reload();
				ProjectFileHandler.clearProjectList();
				ProjectFileHandler.loadProjects(MainWindow.getApplication().getPath(), (DefaultMutableTreeNode) tree.getModel().getRoot());
				((DefaultTreeModel) tree.getModel()).reload();		
				
			}
			
		}
		dispose();
	}
	
}
