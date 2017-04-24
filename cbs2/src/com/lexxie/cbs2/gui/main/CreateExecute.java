package com.lexxie.cbs2.gui.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import com.lexxie.cbs2.project.ProjectFileHandler;
import com.lexxie.cbs2.project.ProjectManager;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateExecute extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField executeCondition;
	private JTextField executeName;

	/**
	 * Create the dialog.
	 */
	public CreateExecute() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(CreateExecute.class.getResource("/icon/Execute.png")));
		setTitle("Create Execute");
		setResizable(false);
		setBounds(100, 100, 304, 113);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblExecutename = new JLabel("ExecuteName:");
		lblExecutename.setBounds(10, 11, 70, 14);
		contentPanel.add(lblExecutename);
		
		JLabel lblExecuteCondition = new JLabel("Execute condition:");
		lblExecuteCondition.setBounds(10, 36, 97, 14);
		contentPanel.add(lblExecuteCondition);
		
		executeCondition = new JTextField();
		executeCondition.setBounds(104, 33, 188, 20);
		contentPanel.add(executeCondition);
		executeCondition.setColumns(10);
		
		executeName = new JTextField();
		executeName.setBounds(104, 8, 188, 20);
		contentPanel.add(executeName);
		executeName.setColumns(10);
		
		final JCheckBox chckbxCreateInitField = new JCheckBox("create init field");
		chckbxCreateInitField.setBounds(6, 57, 97, 23);
		contentPanel.add(chckbxCreateInitField);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(executeName != null && !executeName.getText().isEmpty() && ProjectManager.activeProject != null){
					ProjectManager.activeProject.addExecute(executeName.getText());
					ProjectFileHandler.createFile(executeName.getText());
					
					MainWindow.getApplication().createNewTab(ProjectManager.activeProject.getName(), executeName.getText(), executeCondition.getText(), chckbxCreateInitField.isSelected());
					dispose();
				}else{
					JOptionPane.showMessageDialog(new JDialog(), "Please create a Project or enter a name");
				}
			}
		});
		btnCreate.setBounds(104, 57, 89, 23);
		contentPanel.add(btnCreate);
		
		JButton btnCancle = new JButton("Cancel");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancle.setBounds(203, 57, 89, 23);
		contentPanel.add(btnCancle);
	}
}
