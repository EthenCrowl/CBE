package com.lexxie.cbs2.gui.help;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import java.awt.Toolkit;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JEditorPane;

public class AboutGUI extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutGUI() {
		setTitle("AboutCBE");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AboutGUI.class.getResource("/icon/cbs.png")));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(0, 0, 434, 262);
			contentPanel.add(scrollPane);
			
			JEditorPane tAAbout = new JEditorPane();
			
			tAAbout.addHyperlinkListener(new HyperlinkListener(){

				public void hyperlinkUpdate(HyperlinkEvent hle) {
					 if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                         System.out.println(hle.getURL());
                         Desktop desktop = Desktop.getDesktop();
                         try {
                             desktop.browse(hle.getURL().toURI());
                         } catch (Exception ex) {
                             ex.printStackTrace();
                         }
					 }		
				}							
			});
			
			tAAbout.setEditable(false);
			tAAbout.setContentType("text/html");
			tAAbout.setText("CommandBlockEditor is development tool,\r\naimed at map makers to create a smooth \r\nand easy workflow with commandBlocks.\r\n<br><br>\r\nCBE uses the newest version of CBS \r\n(commandBlockScript) to enable \r\na easy and understandable syntax.\r\n<br><br>\r\nBoth CBE and CBS are created by\r\nLexxer Dennington.<br>\r\n<a href=\"https://twitter.com/lexxerden\">Twitter</a><br>\r\n<a href=\"http://www.planetminecraft.com/member/lexxer/\">Planetminecraft</a> \r\n<br><br>\r\nGraphics by Emex:<br>\r\n<a href= \"www.youtube.com/user/EmeroX498\">Emex</a>");
			scrollPane.setViewportView(tAAbout);
			
			
			
		}
	}

}
