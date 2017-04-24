package com.lexxie.cbs2.autosuggestor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ActionMap;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

import com.lexxie.cbs2.gui.main.MainWindow;

public class SuggestionListener implements KeyListener{

	JEditorPane codePane;
	SuggestionBox box;
	
	public SuggestionListener(JEditorPane codePane, SuggestionBox box){
		this.codePane = codePane;
		this.box = box;
	}
	
	public void keyPressed(KeyEvent e) {
		
		ActionMap am = codePane.getActionMap();
		
		if(box.isVisible()){	
				
			//disable caret up/down
			am.get("caret-up").setEnabled(false);
			am.get("caret-down").setEnabled(false);
			am.get("insert-break").setEnabled(false);
			
			
			
			int selected = box.getList().getSelectedIndex();
			//if up
			if(e.getKeyCode() == KeyEvent.VK_UP){
				if(selected == -1){
					box.getList().setSelectedIndex(0);
					box.getList().ensureIndexIsVisible(0);
				}
				else{
					box.getList().setSelectedIndex(selected - 1);
					box.getList().ensureIndexIsVisible(selected - 1);
				}
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				if(selected == -1){
					box.getList().setSelectedIndex(0);
					box.getList().ensureIndexIsVisible(0);
				}else{
					box.getList().setSelectedIndex(selected + 1);
					box.getList().ensureIndexIsVisible(selected + 1);
				}
			}else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				if(box.getList().getSelectedIndex() != -1){
					Autosuggestor.complete(codePane, box, getCaretPos(), getWordOnCaret());
				}else{
					am.get("insert-break").setEnabled(true);
				}
			}
		}else{
			//enable caret up/down
			am.get("caret-up").setEnabled(true);
			am.get("caret-down").setEnabled(true);
			am.get("insert-break").setEnabled(true);
		}		
		
	}

	public void keyReleased(KeyEvent e) {
		
		String line = getCurrentLine();
		
		//suggest
		if(e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN && line != null && MainWindow.getApplication().doAutoComplete()){
			
			Autosuggestor.suggest(line, getCaretPos(), codePane.getCaretPosition(), box, codePane);

		}
		
		if(line == null || getCaretPos() == line.length() + 1 || !MainWindow.getApplication().doAutoComplete()){
			box.setVisible(false);
		}
		
	}

	public void keyTyped(KeyEvent arg0) {
		//ignore
	}
		
	
	/**
	 * 	return String of the current line, caret is in
	 * */
	private String getCurrentLine(){
		
		int caretPos = codePane.getCaretPosition();
		
		Element root = codePane.getDocument().getDefaultRootElement(); 
		
		for(int i = 0; i < root.getElementCount(); i++){
		
			int start = root.getElement(i).getStartOffset();
			int end = root.getElement(i).getEndOffset();
			
			if(start <= caretPos && caretPos <= end){
				
				String line = codePane.getText().substring(start, end - 1);
				
				int index = line.indexOf("#");
				
				if(!(index == -1) && (caretPos - (start + index)) > 0)
					return line.substring(index);
			}
			
		}
		
		return null;
	}
	
	private String getWordOnCaret(){
		
		String line = getCurrentLine();
		
		int caretPos = getCaretPos();
		
		for(int i = caretPos - 1; i >= 0; i--){
			
			if(i == 0){
				return line;
			}else if(line.charAt(i) == ' '){
				return line.substring(i, caretPos);
			}
			
		}
		
		return null;
	}
	
	private int getCaretPos(){
		
		int caretPos = codePane.getCaretPosition();
		
		Element root = codePane.getDocument().getDefaultRootElement(); 
		
		for(int i = 0; i < root.getElementCount(); i++){
		
			int start = root.getElement(i).getStartOffset();
			int end = root.getElement(i).getEndOffset();
			
			if(start <= caretPos && caretPos <= end){
				
				String line = codePane.getText().substring(start, end - 1);
				int index = line.indexOf("#");
				
				if(index != -1)				
					return (caretPos - (start + index));
			}
			
		}
		
		return -1;
		
	}
	
}
