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

import com.lexxie.cbs2.project.ProjectManager;
import com.lexxie.cbs2.utilities.refactor.Rename;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScoreEditGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField tFScoreName;
	private JTextField tFType;

	/**
	 * Create the dialog.
	 */
	public ScoreEditGUI(final ScoreGUI gui, final String scoreName, String type) {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ScoreEditGUI.class.getResource("/icon/Scoreboard.png")));
		setTitle("Edit " + scoreName);
		setBounds(100, 100, 248, 123);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblScoreName = new JLabel("name:");
			lblScoreName.setBounds(10, 11, 40, 14);
			contentPanel.add(lblScoreName);
		}
		{
			JLabel lblType = new JLabel("Type:");
			lblType.setBounds(10, 39, 46, 14);
			contentPanel.add(lblType);
		}
		
		tFScoreName = new JTextField(scoreName);
		tFScoreName.setBounds(49, 8, 186, 20);
		contentPanel.add(tFScoreName);
		tFScoreName.setColumns(10);
		
		tFType = new JTextField(type);
		tFType.setBounds(49, 36, 186, 20);
		contentPanel.add(tFType);
		tFType.setColumns(10);
		
		JButton btnApply = new JButton("apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!tFScoreName.getText().isEmpty() && !tFType.getText().isEmpty() && ProjectManager.activeProject != null){
					Rename.renameForProject(scoreName, tFScoreName.getText());
					
					ProjectManager.activeProject.getScoreHandler().deleteScore(scoreName);
					ProjectManager.activeProject.getScoreHandler().addScore(tFScoreName.getText(), tFType.getText());
				
					gui.update();
					
					dispose();		
				}
			}
		});
		btnApply.setBounds(47, 64, 89, 23);
		contentPanel.add(btnApply);
		
		JButton btnCancle = new JButton("cancle");
		btnCancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancle.setBounds(146, 64, 89, 23);
		contentPanel.add(btnCancle);
	}
}
