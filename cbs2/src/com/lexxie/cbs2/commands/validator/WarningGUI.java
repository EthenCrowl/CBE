package com.lexxie.cbs2.commands.validator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.SpringLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.lexxie.cbs2.commands.validator.Validator.WarningInfo;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class WarningGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	
	List<WarningInfo> warnings;

	/**
	 * Create the dialog.
	 */
	public WarningGUI(List<WarningInfo> warnings) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(WarningGUI.class.getResource("/icon/X.png")));
		
		this.warnings = warnings;
		setTitle("Warnings");
		setBounds(100, 100, 441, 292);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{425, 0};
		gbl_contentPanel.rowHeights = new int[]{254, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPanel.add(scrollPane, gbc_scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Warning:", "Occurrence:", "Description"
				}){				
			});
		scrollPane.setViewportView(table);
		
		loadData();
	}
	
	private void loadData(){
		
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		
		for(WarningInfo info : warnings){
			model.addRow(new Object[]{info.getWarning(), info.getLine(), info.getDescription()});
		}
	}
	
}
