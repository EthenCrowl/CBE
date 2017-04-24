package com.lexxie.cbs2.utilities.scoreboard;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class ScoreTableModel extends AbstractTableModel{

	public ScoreTableModel(){
		
		new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Score:", "Type:"
				}
			);
		
	}
	
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
