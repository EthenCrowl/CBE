package com.lexxie.cbs2.utilities.variables;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.lexxie.cbs2.parser.data.commands.Variable;
import com.lexxie.cbs2.project.ProjectManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class VariablesGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Create the dialog.
	 */
	public VariablesGUI() {
		setResizable(false);
		setTitle("Variables");
		setIconImage(Toolkit.getDefaultToolkit().getImage(VariablesGUI.class.getResource("/icon/Planet.png")));
		setBounds(100, 100, 201, 324);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblVariables = new JLabel("Variables:");
		lblVariables.setBounds(10, 11, 63, 14);
		contentPanel.add(lblVariables);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 34, 174, 233);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name:", "Value:"
			}
		){
			
		@Override
		public boolean isCellEditable(int row, int colum){
			return false;
		}
			
		});
		scrollPane.setViewportView(table);
		
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				VariableEditGUI editGUI = new VariableEditGUI(VariablesGUI.this, 
																table.getModel().getValueAt(table.getSelectedRow(), 0).toString(), 
																table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
				editGUI.setModal(true);
				editGUI.setVisible(true);
			}
		});
		popupMenu.add(edit);
		
		table.setComponentPopupMenu(popupMenu);
		
		JButton btnAddVar = new JButton("Add var");
		btnAddVar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddVariablesGUI add = new AddVariablesGUI(VariablesGUI.this);
				add.setModal(true);
				add.setVisible(true);
			}
		});
		btnAddVar.setBounds(10, 270, 81, 23);
		contentPanel.add(btnAddVar);
		
		JButton btnDeleteVar = new JButton("Delete var");
		btnDeleteVar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteVariable();
			}
		});
		btnDeleteVar.setBounds(101, 270, 83, 23);
		contentPanel.add(btnDeleteVar);
		
		update();
	}
	
	public void deleteVariable(){
		
		try{
			int row = table.getSelectedRow();
			
			int oContinue = JOptionPane.showConfirmDialog(new JDialog(), "Delete variable: " + table.getModel().getValueAt(row, 0).toString(), "Delete", JOptionPane.YES_NO_OPTION);
			
			if(oContinue == JOptionPane.YES_OPTION){
				String variableName = table.getModel().getValueAt(row, 0).toString();
				ProjectManager.activeProject.getVariableHandler().deleteVariable(variableName);
			
				Variable.deleteVat(variableName);
				
				((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
			}
		}catch(ArrayIndexOutOfBoundsException ex){
			
		}
		
	}
	
	public void update(){
		
		VariableHandler handler = ProjectManager.activeProject.getVariableHandler();
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		for(int i = model.getRowCount() - 1; i >= 0; i--){
			model.removeRow(i);
		}
		
		List<String> variableNames = new ArrayList<String>();
		List<String> value = new ArrayList<String>();
		
		for(Entry<String, String> entry : handler.getVariables().entrySet()){
			variableNames.add(entry.getKey());
			value.add(entry.getValue());
		}
		
		Collections.sort(variableNames);
		Collections.sort(value);
		
		for(int i = 0; i < variableNames.size(); i++){
			((DefaultTableModel) table.getModel()).addRow(new Object[]{variableNames.get(i), value.get(i)});
		}
		
	}
	
}
