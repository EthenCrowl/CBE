package com.lexxie.cbs2.utilities.variables;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.lexxie.cbs2.project.ProjectManager;
import com.lexxie.cbs2.utilities.refactor.Rename;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VariableEditGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tFName;
	private JTextField tFValue;

	/**
	 * Create the dialog.
	 */
	public VariableEditGUI(final VariablesGUI gui,final String varName, String value) {
		setTitle("Edit " + varName);
		setResizable(false);
		setBounds(100, 100, 278, 111);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblName = new JLabel("name:");
		lblName.setBounds(10, 11, 46, 14);
		contentPanel.add(lblName);
		
		tFName = new JTextField();
		tFName.setBounds(66, 8, 198, 20);
		contentPanel.add(tFName);
		tFName.setColumns(10);
		tFName.setText(varName);
		
		JLabel lblValue = new JLabel("value:");
		lblValue.setBounds(10, 36, 46, 14);
		contentPanel.add(lblValue);
		
		tFValue = new JTextField();
		tFValue.setBounds(66, 33, 198, 20);
		contentPanel.add(tFValue);
		tFValue.setColumns(10);
		tFValue.setText(value);
		
		JButton btnApply = new JButton("apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tFName.getText().isEmpty() && !tFValue.getText().isEmpty() && ProjectManager.activeProject != null){
					Rename.renameForProject("%" + varName + "%", "%" + tFName.getText() + "%");
					
					ProjectManager.activeProject.getVariableHandler().deleteVariable(varName);
					ProjectManager.activeProject.getVariableHandler().addVariables(tFName.getText(), tFValue.getText());
					
					gui.update();
					dispose();
				}
			}
		});
		btnApply.setBounds(66, 57, 89, 23);
		contentPanel.add(btnApply);
		
		JButton btnCancle = new JButton("cancle");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancle.setBounds(175, 57, 89, 23);
		contentPanel.add(btnCancle);
	}
}
