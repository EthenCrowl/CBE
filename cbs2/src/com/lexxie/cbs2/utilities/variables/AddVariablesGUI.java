package com.lexxie.cbs2.utilities.variables;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.lexxie.cbs2.parser.data.commands.Variable;
import com.lexxie.cbs2.project.ProjectManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddVariablesGUI extends JDialog {
	private JTextField tFVariableName;
	private JTextField tFValue;

	VariablesGUI gui;
	
	/**
	 * Create the dialog.
	 */
	public AddVariablesGUI(VariablesGUI gui) {
		this.gui = gui;
		
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddVariablesGUI.class.getResource("/icon/Planet.png")));
		setTitle("Add variable");
		setBounds(100, 100, 245, 115);
		getContentPane().setLayout(null);
		
		JLabel lblVariableName = new JLabel("Variable:");
		lblVariableName.setBounds(10, 11, 46, 14);
		getContentPane().add(lblVariableName);
		
		tFVariableName = new JTextField();
		tFVariableName.setBounds(66, 8, 169, 20);
		getContentPane().add(tFVariableName);
		tFVariableName.setColumns(10);
		
		JLabel lblValue = new JLabel("Value:");
		lblValue.setBounds(10, 36, 46, 14);
		getContentPane().add(lblValue);
		
		tFValue = new JTextField();
		tFValue.setBounds(66, 33, 169, 20);
		getContentPane().add(tFValue);
		tFValue.setColumns(10);
		
		JButton btnAdd = new JButton("add Var");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addVariable();
			}
		});
		btnAdd.setBounds(66, 61, 80, 23);
		getContentPane().add(btnAdd);
		
		JButton btnCancle = new JButton("cancle");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancle.setBounds(156, 61, 79, 23);
		getContentPane().add(btnCancle);
	}
	
	private void addVariable(){
		
		if(!tFVariableName.getText().isEmpty() && !tFValue.getText().isEmpty()){
				ProjectManager.activeProject.getVariableHandler().addVariables(tFVariableName.getText(), tFValue.getText());
				Variable var = new Variable();
				var.setName(tFVariableName.getText());
				var.setValue(tFValue.getText());
				Variable.addVar(var);
		}
		dispose();
		gui.update();
	}
}
