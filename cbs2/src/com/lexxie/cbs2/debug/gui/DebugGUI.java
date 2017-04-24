package com.lexxie.cbs2.debug.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.lexxie.cbs2.parser.data.commands.BlockData;
import com.lexxie.cbs2.parser.data.commands.Execute;
import com.lexxie.cbs2.parser.data.commands.Init;
import com.lexxie.cbs2.utilities.scoreboard.ScoreGUI;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class DebugGUI extends JDialog {

	Map<String, ArrayList<DebugInfo>> debugCommands = new HashMap<String, ArrayList<DebugInfo>>();
	
	List<DebugInfo> activeDebug = new ArrayList<DebugInfo>();
	
	JTextArea tACommand;
	
	DefaultComboBoxModel cBModel;
	JComboBox cBSelect;
	
	DefaultListModel infoModel;
	JList lInfo;
	
	int count = 0;
	
	/**
	 * Create the dialog.
	 */
	public DebugGUI() {
		setResizable(false);
		setTitle("Debug");
		setIconImage(Toolkit.getDefaultToolkit().getImage(DebugGUI.class.getResource("/icon/Debug32.png")));
		setBounds(100, 100, 542, 322);
		
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(475, 263, 55, 23);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				count++;
				update(cBSelect.getSelectedItem().toString(), count);
			}
		});
		getContentPane().setLayout(null);
		getContentPane().add(btnNext);
		
		JButton btnPervious = new JButton("Pervious");
		btnPervious.setBounds(391, 263, 73, 23);
		btnPervious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				count--;
				update(cBSelect.getSelectedItem().toString(), count);
			}
		});
		getContentPane().add(btnPervious);
		
		JButton btnJump = new JButton("Jump");
		btnJump.setBounds(323, 263, 57, 23);
		btnJump.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jump();
			}
		});
		getContentPane().add(btnJump);
		
		JLabel lblInfo = new JLabel("Info:");
		lblInfo.setBounds(11, 14, 24, 14);
		getContentPane().add(lblInfo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(11, 34, 88, 218);
		getContentPane().add(scrollPane);
		
		infoModel = new DefaultListModel();
		lInfo = new JList(infoModel);
		scrollPane.setViewportView(lInfo);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(110, 34, 420, 218);
		getContentPane().add(scrollPane_1);
		
		tACommand = new JTextArea();
		tACommand.setEditable(false);
		scrollPane_1.setViewportView(tACommand);
		
		JLabel lblCommand = new JLabel("Command:");
		lblCommand.setBounds(110, 14, 79, 14);
		getContentPane().add(lblCommand);
		
		cBModel = new DefaultComboBoxModel();
		cBSelect = new JComboBox(cBModel);
		cBSelect.setBounds(11, 264, 88, 20);
		cBSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				count = 0;
				update(cBSelect.getSelectedItem().toString(), count);
			}
		});
		getContentPane().add(cBSelect);
		
		initDebug();
	}
	
	private void initDebug(){
		
		ArrayList<DebugInfo> list = new ArrayList<DebugInfo>();
		
		for(BlockData data : Init.getCommands()){
			list.add(new DebugInfo(data, data.getCommand()));
		}
		
		debugCommands.put("Init", list);
		
		Iterator it = Execute.getMainLoops().entrySet().iterator();
		int n = 0;
		
		while(it.hasNext()){
			
			n++;
			ArrayList<DebugInfo> nList = new ArrayList<DebugInfo>();
			Entry pair = (Entry) it.next();
			
			for(BlockData data : ((Execute) pair.getValue()).getCommands()){
				nList.add(new DebugInfo(data, data.getCommand()));
			}
			
			debugCommands.put("Execute#" + n, nList);
		}
		
		for(Entry entry : debugCommands.entrySet()){
			cBModel.addElement(entry.getKey().toString());
		}
	
		if(!debugCommands.get("Init").isEmpty()){
			update("Init", 0);
		}
		
	}
	
	private void update(String key, int n){
		System.out.println("n:" + n);
		activeDebug = debugCommands.get(key);
		
		if(activeDebug.size() > n && n >= 0){
			tACommand.setText(activeDebug.get(n).getCommand().replaceAll("\\{", "\n\\{"));
		
			infoModel.clear();
		
			infoModel.addElement("Block:" + activeDebug.get(n).getBlockData().getType());
			infoModel.addElement("Conditional:" + activeDebug.get(n).getBlockData().isConditional());
			infoModel.addElement("Redstone:" + activeDebug.get(n).getBlockData().isRedstone());
			infoModel.addElement("Command:" + count + "/" + (activeDebug.size() - 1));
			System.out.println("Debug:" + activeDebug.get(n).getBlockData().isDebug());
		}
	}
	
	private void jump(){
		
		for(int i = count + 1; i < activeDebug.size(); i++){
			
			if(activeDebug.get(i).getBlockData().isDebug()){
				count = i;
				update(cBSelect.getSelectedItem().toString(), i);
				break;
			}
			
		}
		
	}
	
}
