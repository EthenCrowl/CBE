package com.lexxie.cbs2.edit;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class AddUndoRedo {

	public AddUndoRedo(JEditorPane pane){
		
		final UndoManager undo = new UndoManager();
		Document doc = pane.getDocument();
		
		doc.addUndoableEditListener(new UndoableEditListener(){

			public void undoableEditHappened(UndoableEditEvent arg0) {
				undo.addEdit(arg0.getEdit());
			}
		});
		
		pane.getActionMap().put("Undo", new AbstractAction("Undo"){
			public void actionPerformed(ActionEvent arg0) {
				try{
					if(undo.canUndo()){
						undo.undo();
					}
				}catch(CannotUndoException ex){}
				
			}
			
		});
		
		pane.getActionMap().put("Redo", new AbstractAction("Redo"){

			public void actionPerformed(ActionEvent arg0) {
				try{
					if(undo.canRedo()){
						undo.redo();
					}
				}catch(CannotRedoException ex){
						
				}
			}
		});
		
		pane.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
		pane.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
	}
	
}
