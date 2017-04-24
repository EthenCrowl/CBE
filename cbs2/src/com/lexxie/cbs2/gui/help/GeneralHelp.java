package com.lexxie.cbs2.gui.help;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GeneralHelp extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public GeneralHelp() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(GeneralHelp.class.getResource("/icon/cbs.png")));
		setTitle("GeneralHelp");
		setBounds(100, 100, 450, 516);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 444, 488);
		contentPanel.add(scrollPane);
		
		JTextArea tAHelp = new JTextArea();
		tAHelp.setEditable(false);
		tAHelp.setText("'//' comment: will be ignored by the parser\r\n\r\n'execute:#\r\n{\r\n<code>\r\n}' execute: a row of commandBlocks, every \r\ncommand wil be executed after another, multiple \r\n'execute' lines will be execute synchronously.\r\nThe '#' at the end of 'execute' represents the very \r\nfirst command of the row.\r\n\r\n'init:{\r\n<code>\r\n}' init: similar to 'exect' but will only be \r\nexecuted one and before all\r\nother commands\r\n\r\n'func var(para1, para2){\r\n<code>\r\n}' func: represents a function, functions can have \r\nzero or more paramaters sepataed by commas.\r\nTo call a function just wirte the name of the \r\nfunction and associated parameters:\r\n\"var(myParameter, myOtherParameter)\"\r\n\r\nVariables:\r\n\r\n'var name = \"insert your text here\"'\r\n'var command = #inser your command here'\r\nvar: the word 'var' followed by a name and \r\nan equla sign an either text in parentheses\r\nor a '#' will assign either text or a command\r\nto the variable.\r\nIn order to access the variable, just call it in \r\nthe code:\r\n\"#say %name%\" <- var contains text\r\n\"%command%\" <- var is a command\r\n\r\n'var[] array = {\"text1\", \"text2\"}'\r\nWrite 'var[]' to creat a array, thne initialize\r\njust like above. \r\nA array can only contain text,\r\nto access it, write the name of it, followed \r\nby the index of the value you want:\r\n\"%array[0]%\" <- would be the first entry 'text1'\r\n\"%array[1]%\" <- would be the second entry 'text2'\r\n \r\n\r\n<Code>:\r\n'#': A hashtag represents a command, everything \r\nafter a hashtag in the same line, will be \r\nparsed as a command:\r\n\"#time set day\"\r\n\r\n'repeat(2x){\r\n<code>\r\n}' repeat: will repeat the code n times.\r\nn is the number you parse, followed by \r\na variable to acces to loop count.\r\nThe above code would be repeated 2 times.\r\n\"repeat(2x){\r\n\t#say repeated %x% times\r\n}\" <- would create 2commands\r\nwith : \r\n\"/say repeated 0 times\"\r\n\"/say repeated 1 times\"\r\nYou can also use the access variable,\r\nto access array:\r\n\"repeat(2x){\r\n\t#say %array[%x%]%\r\n}\" <- would create two commands:\r\n\"/say test1\"\r\n\"/say test2\"\r\n\r\nParameters:\r\n\r\nParameters can be used to change the type \r\nof a commandBlock.\r\nParameters need to be right before \r\nthe command\r\n\" -r -b #<command>\"\r\n\r\n-R: Changes the type of the command Block to \r\n\"repeatingCommandBlock\"\r\n-C: Changes the type of the command Block to \r\n\"chainCommandBlock\"\r\n-N: Cahnges the type of the commandBlock to \r\n\"commandBlock\"\r\n-r: Set the command block to \"need redstone\"\r\n-c: makes the commandBlock conditional\r\n-b:defines the default behaviour of the generation\r\n-n: reset the standart behaviour \r\n-tag name: tag saves the coordiantes of the block \r\nin a variable, wich can be accessed via the 'name'\r\n\r\nExamples:\r\n\"execute:#\r\n{\r\n\t-r  #say I like apples \r\n}\" <- will create a ChainCommandBlock (default) \r\nwhich is set \"need redstone\"\r\n\r\n\"execute:#\r\n{\r\n\t-r -c #say i only like fruits\r\n}\" <- will create a ChainCommandBlock (default)\r\nwhich is conditional and need redstone\r\n\r\n\"execute:#\r\n{\r\n\t-N -r #say i like chocolate\r\n}\" <- will create a (normal)commandBlock\r\nset to \"ned redstone\"\r\n\r\n\"execute:#\r\n{\r\n\t-c -b #say i change you all\r\n\t#say i am also conditional\r\n}\" <- will create a comditional\r\nchainCommandBlock, and changes\r\nthe default generation behavior.\r\n(All other commandBlock after this \r\nwill also be conditional)\r\n\r\n\"execute:#\r\n{\r\n\t-c -b #say i change you all!\r\n\t#say i am conditional\r\n\t-n #say i am not affected\r\n\t#say i am conditional\r\n}\" <- will create a conditional \r\nchainCommandBlock and change the \r\ndefault generation behaviour, every \r\ncommand after will be conditional.\r\n-n restores the default behaviour only\r\nfor the affected command.\r\n\r\n\r\n\"execute:#\r\n{\r\n\t-c -b #say i change you all!\r\n\t#say i am conditional\r\n\t-n -b #say i am not affected\r\n\t#say i am not affected\r\n}\" <- will create a conditional\r\nchainCommandBlock, every \r\ncommand after this, will be conditional.\r\n-n -b resets the dafult behavior agian, for \r\nall following commands.\r\n\r\n\"execute:#\r\n{\r\n\t-tag stone #\r\n\t-r #scoreboard players set @a activate 1\r\n\t#setblock %stone% minectaft:stone\r\n}\" <- will save the coordiantes for\r\nthe command '#' in a variable called \"stone\"\r\nThe third command, will then use the \r\ncoordiantes, replace the commandBlock with \r\na redstone block.\r\n");
		scrollPane.setViewportView(tAHelp);
	}
}
