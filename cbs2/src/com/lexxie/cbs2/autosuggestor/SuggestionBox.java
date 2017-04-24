package com.lexxie.cbs2.autosuggestor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class SuggestionBox {

	private DefaultListModel sModel = new DefaultListModel();
	private JList sList = new JList(sModel);
	private JScrollPane sPane = new JScrollPane(sList);
	
	public SuggestionBox(){
		sPane.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 3, new Color(42, 40, 32, 150)));
	}

	public void removeAll(){
		sModel.removeAllElements();
	}
	
	public void addItems(String[] items){
		
		for(int i = 0; i < items.length; i++){
			sModel.addElement(items[i]);
		}
		
	}
	
	public void setVisible(boolean visible){
		sPane.setVisible(visible);
	}
	
	public void setSize(){
		
		Dimension size = new Dimension(180, 100);
		
		sPane.setSize(size);
		
	}
	
	public void setLocation(Rectangle location){
		sPane.setLocation(location.x, location.y + 10);
	}
	
	public boolean isVisible(){
		return sPane.isVisible();
	}
	
	public DefaultListModel getModel(){
		return sModel;
	}
	
	public JList getList(){
		return sList;
	}
	
	public JScrollPane getSuggestionBox(){
		return sPane;
	}
	
}
