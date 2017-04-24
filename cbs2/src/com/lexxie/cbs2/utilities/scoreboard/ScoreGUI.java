package com.lexxie.cbs2.utilities.scoreboard;

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

import com.lexxie.cbs2.project.ProjectManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

public class ScoreGUI extends JDialog {
	
	private JTable table;
	
	/**
	 * Create the dialog.
	 */
	public ScoreGUI() {
		setResizable(false);
		setTitle("Scoreboard handler");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ScoreGUI.class.getResource("/icon/Scoreboard.png")));
		setBounds(100, 100, 220, 324);
		getContentPane().setLayout(null);
		
		JLabel lblScores = new JLabel("Score(s):");
		lblScores.setBounds(10, 11, 46, 14);
		getContentPane().add(lblScores);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 38, 194, 225);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Score:", "Type:"
			}){
			
			@Override
			public boolean isCellEditable(int row, int colum){
				return false;
			}
			
		});
		
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem edit = new JMenuItem("Edit");
		edit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				ScoreEditGUI edit = new ScoreEditGUI(ScoreGUI.this,
													table.getModel().getValueAt(table.getSelectedRow(), 0).toString(), 
													table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
				edit.setModal(true);
				edit.setVisible(true);
			}
			
		});
		
		popupMenu.add(edit);
		
		table.setComponentPopupMenu(popupMenu);

		scrollPane.setViewportView(table);
		
		JButton btnAddScore = new JButton("Add Score");
		btnAddScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddScoreGUI add = new AddScoreGUI(ScoreGUI.this);
				add.setModal(true);
				add.setVisible(true);
			}
		});
		btnAddScore.setBounds(10, 268, 89, 23);
		getContentPane().add(btnAddScore);
		
		JButton btnDeleteScore = new JButton("Delete Score");
		btnDeleteScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteScore();
			}
		});
		btnDeleteScore.setBounds(111, 268, 93, 23);
		getContentPane().add(btnDeleteScore);
		
		update();	
	}
	
	public void deleteScore(){	
		try{
			int row = table.getSelectedRow();
			
			int oContinue = JOptionPane.showConfirmDialog(new JDialog(), "Delete score: " + table.getModel().getValueAt(row, 0).toString(), "Delete", JOptionPane.YES_NO_OPTION);
			
			if(oContinue == JOptionPane.YES_OPTION){
				String scoreName = table.getModel().getValueAt(row, 0).toString();
				ProjectManager.activeProject.getScoreHandler().deleteScore(scoreName);
			
				((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
			}
		}catch(ArrayIndexOutOfBoundsException ex){
			
		}
		
	}
	
	public void update(){
		
		ScoreHandler handler = ProjectManager.activeProject.getScoreHandler();
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		for(int i = model.getRowCount() - 1; i >= 0; i--){
			model.removeRow(i);
		}
		
		List<String> scoreNames = new ArrayList<String>();
		List<String> scoreTypes = new ArrayList<String>();
		
		for(Entry<String, String> entry : handler.getScores().entrySet()){
			scoreNames.add(entry.getKey());
			scoreTypes.add(entry.getValue());
		}
		
		Collections.sort(scoreNames);
		Collections.sort(scoreTypes);
		
		for(int i = 0; i < scoreNames.size(); i++){
			((DefaultTableModel) table.getModel()).addRow(new Object[]{scoreNames.get(i), scoreTypes.get(i)});
		}
		
	}
	
}
