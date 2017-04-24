package com.lexxie.cbs2.project;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.lexxie.cbs2.gui.main.MainWindow;

public class ProjectMouseListener extends MouseAdapter{

	JTree tree;
	
	public ProjectMouseListener(JTree tree){
		this.tree = tree;
	}
	
		public void mousePressed(MouseEvent e){
			//Select item by right click
			int row = MainWindow.getApplication().getProjectTree().getRowForLocation(e.getX(), e.getY());
			TreePath path = MainWindow.getApplication().getProjectTree().getPathForLocation(e.getX(), e.getY());
			
			if(row > -1)
				MainWindow.getApplication().getProjectTree().setSelectionRow(row);
			
			if(e.isPopupTrigger())
				doPop(e);
		}
	
		public void mouseReleased(MouseEvent e){
			if(e.isPopupTrigger())
				doPop(e);
		}
		
		public void doPop(MouseEvent e){
			ProjectPopup pP = new ProjectPopup(tree);
			pP.show(e.getComponent(), e.getX(), e.getY());
		}
		
}
