package com.lexxie.cbs2.utilities.scoreboard;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;

import com.lexxie.cbs2.project.ProjectManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class AddScoreGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tFScoreName;
	private JTextField tFCustomType;
	private JCheckBox chckbxCustom;
	private JComboBox comboBox;

	private ScoreGUI gui;
	
	/**
	 * Create the dialog.
	 */
	public AddScoreGUI(ScoreGUI gui) {
		this.gui = gui;
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddScoreGUI.class.getResource("/icon/Scoreboard.png")));
		setTitle("Add Score");
		setBounds(100, 100, 259, 153);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblScoreName = new JLabel("Score name:");
			lblScoreName.setBounds(10, 11, 60, 14);
			contentPanel.add(lblScoreName);
		}
		
		tFScoreName = new JTextField();
		tFScoreName.setBounds(73, 8, 160, 20);
		contentPanel.add(tFScoreName);
		tFScoreName.setColumns(10);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(10, 36, 46, 14);
		contentPanel.add(lblType);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"dummy", "trigger", "deathCount", "playerKillCount", "totalKillCount", "health", "xp", "level", "food", "air", "armor"}));
		comboBox.setBounds(73, 33, 160, 20);
		contentPanel.add(comboBox);
		
		tFCustomType = new JTextField();
		tFCustomType.setEditable(false);
		tFCustomType.setBounds(73, 58, 160, 20);
		contentPanel.add(tFCustomType);
		tFCustomType.setColumns(10);
		
		chckbxCustom = new JCheckBox("Custom");
		chckbxCustom.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if(chckbxCustom.isSelected()){
					tFCustomType.setEditable(true);
					comboBox.setEnabled(false);
				}else{
					tFCustomType.setEditable(false);
					comboBox.setEnabled(true);
				}
			}
		});
		chckbxCustom.setBounds(6, 57, 64, 23);
		contentPanel.add(chckbxCustom);
		
		JButton btnAddScore = new JButton("Add Score");
		btnAddScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addScore();
			}
		});
		btnAddScore.setBounds(73, 87, 81, 23);
		contentPanel.add(btnAddScore);
		
		JButton btnCancle = new JButton("Cancle");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancle.setBounds(158, 87, 75, 23);
		contentPanel.add(btnCancle);
	}
	
	private void addScore(){
		
		if(!tFScoreName.getText().isEmpty()){
			if(!chckbxCustom.isSelected()){
				ProjectManager.activeProject.getScoreHandler().addScore(tFScoreName.getText(), comboBox.getSelectedItem().toString());
			}else if(!tFCustomType.getText().isEmpty()){
				ProjectManager.activeProject.getScoreHandler().addScore(tFScoreName.getText(), tFCustomType.getText());
			}
		}
		
		gui.update();
		dispose();
	}
	
}
