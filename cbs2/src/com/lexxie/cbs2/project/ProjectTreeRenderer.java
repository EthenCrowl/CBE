package com.lexxie.cbs2.project;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ProjectTreeRenderer extends DefaultTreeCellRenderer{

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		if(value != null && value instanceof DefaultMutableTreeNode){
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			
			if(!node.isRoot()){
				
				if(((DefaultMutableTreeNode) node.getParent()).isRoot() || !node.isLeaf()){	
					if(!expanded){
						setIcon(new ImageIcon(ProjectTreeRenderer.class.getResource("/icon/Project.png")));
					}else{
						setIcon(new ImageIcon(ProjectTreeRenderer.class.getResource("/icon/ProjectOpen.png")));
					}
				}else{
					setIcon(new ImageIcon(ProjectTreeRenderer.class.getResource("/icon/File.png")));
				}
			}
		}
		
		return this;
		
	}
	
}
