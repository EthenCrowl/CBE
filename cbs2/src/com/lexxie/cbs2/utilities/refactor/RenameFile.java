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
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.lexxie.cbs2.gui.main.MainWindow;
import com.lexxie.cbs2.project.ProjectFileHandler;
import com.lexxie.cbs2.project.ProjectManager;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

public class RenameFile extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tFRename;

	/**
	 * Create the dialog.
	 */
	public RenameFile(final String name, final String project, final JTree tree) {
		setTitle("Rename File");
		setIconImage(Toolkit.getDefaultToolkit().getImage(RenameFile.class.getResource("/icon/Execute.png")));
		setResizable(false);
		setBounds(100, 100, 222, 110);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblRename = new JLabel("New name:");
		lblRename.setBounds(10, 11, 60, 14);
		contentPanel.add(lblRename);
		
		tFRename = new JTextField();
		tFRename.setBounds(10, 25, 194, 20);
		contentPanel.add(tFRename);
		tFRename.setColumns(10);
		
		tFRename.setText(name);
		
		JButton btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rename(name, project, tree);
			}
		});
		btnRename.setBounds(10, 50, 89, 23);
		contentPanel.add(btnRename);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(115, 50, 89, 23);
		contentPanel.add(btnCancel);
	}
	
	private void rename(String name, String project, JTree tree){
		
		File orig = new File(MainWindow.getApplication().getPath() + project + "\\" + name);
		File newF = new File(MainWindow.getApplication().getPath() + project + "\\" +  tFRename.getText());
		
		if(newF.exists()){
			JOptionPane.showMessageDialog(new JDialog(), "File already exist", "Couldn't rename", JOptionPane.ERROR_MESSAGE);
		}else{
			
			boolean success = orig.renameTo(newF);
			
			if(!success){
				JOptionPane.showMessageDialog(new JDialog(), "Coudn't rename File", "Error", JOptionPane.ERROR_MESSAGE);
			}else{
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
