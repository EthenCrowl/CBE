package com.lexxie.cbs2.tabs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import com.lexxie.cbs2.project.ProjectFileHandler;

public class CloseTabComponent extends JPanel{

	private final JTabbedPane pane;
	private String tabName;
	private JEditorPane codeEditor;
	
	public CloseTabComponent(final JTabbedPane pane, String tabName, JEditorPane codeEditor){
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		this.tabName = tabName;
		this.codeEditor = codeEditor;
		
		if(pane == null)
			throw new NullPointerException("TabbedPane is null");
		
		this.pane = pane;
		
		setOpaque(false);
		
		JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(CloseTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        	
        add(label);
        label.setBorder(BorderFactory.createEmptyBorder(0,0,0,5));
        
        JButton button = new TabButton();
        add(button);
        
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
	}
	
	private class TabButton extends JButton implements ActionListener{

		public TabButton(){
			int size = 17;
			setPreferredSize(new Dimension(size, size));
			setToolTipText("close this tab");
			
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			int i = pane.indexOfTabComponent(CloseTabComponent.this);
			if(i != -1){
				int sOption = JOptionPane.showConfirmDialog(null, "Save changes?", "Save", JOptionPane.YES_NO_OPTION);
				if(sOption == JOptionPane.YES_OPTION){
					ProjectFileHandler.saveFile(ProjectTab.getTab(i), tabName, codeEditor.getText());
				}
				pane.remove(i);
				ProjectTab.getTabs().remove(i);
			}
		}
		
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			
			if(getModel().isPressed())
				g2.translate(1, 1);
			
			g2.setStroke(new BasicStroke());
			g2.setColor(Color.BLACK);
			
			if(getModel().isRollover())
				g2.setColor(Color.BLUE);
			
			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2.dispose();
		}
	}
	
	private final static MouseListener buttonMouseListener = new MouseAdapter(){
		public void mouseEntered(MouseEvent e){
			Component component = e.getComponent();
			if(component instanceof AbstractButton){
				AbstractButton  button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}
		
		public void mouseExited(MouseEvent e){
			Component component = e.getComponent();
			if(component instanceof AbstractButton){
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
		
	};

}
