package com.lexxie.cbs2.gui.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JEditorPane;

import com.lexxie.cbs2.autosuggestor.SuggestionBox;
import com.lexxie.cbs2.autosuggestor.SuggestionListener;
import com.lexxie.cbs2.commands.ReadCommands;
import com.lexxie.cbs2.commands.validator.Validator;
import com.lexxie.cbs2.commands.validator.WarningGUI;
import com.lexxie.cbs2.debug.gui.DebugGUI;
import com.lexxie.cbs2.edit.AddUndoRedo;
import com.lexxie.cbs2.gui.help.AboutGUI;
import com.lexxie.cbs2.gui.help.GeneralHelp;
import com.lexxie.cbs2.parser.ReadFiles;
import com.lexxie.cbs2.parser.data.nbt.Builder;
import com.lexxie.cbs2.parser.data.nbt.Builder.algorithmus;
import com.lexxie.cbs2.project.ProjectFileHandler;
import com.lexxie.cbs2.project.ProjectManager;
import com.lexxie.cbs2.project.ProjectManager.ExecuteData;
import com.lexxie.cbs2.project.ProjectMouseListener;
import com.lexxie.cbs2.project.ProjectTreeRenderer;
import com.lexxie.cbs2.tabs.CloseTabComponent;
import com.lexxie.cbs2.tabs.ProjectTab;
import com.lexxie.cbs2.utilities.scoreboard.ScoreFileHandler;
import com.lexxie.cbs2.utilities.scoreboard.ScoreGUI;
import com.lexxie.cbs2.utilities.variables.VariableFileHandler;
import com.lexxie.cbs2.utilities.variables.VariablesGUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.BevelBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.undo.UndoManager;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.SpringLayout;

public class MainWindow extends JFrame {

	private static MainWindow app;
	
	JTree projectTree;
	DefaultMutableTreeNode root;
	
	DefaultListModel localModel = new DefaultListModel();
	DefaultListModel globalModel = new DefaultListModel();
	DefaultListModel functionModel = new DefaultListModel();
	
	private boolean autoComplete = true;
	private boolean validate = true;
	private boolean doCreate = true;
	
	private String path = "Projects";
	
	public static MainWindow getApplication(){
		if(app == null){
			app = new MainWindow();
		}
		return app;
	}
	
	private JPanel contentPane;
	private final JTabbedPane tabbedCodeField = new JTabbedPane();

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/icon/cbs.png")));
		setTitle("CommandBlockEditor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 972, 505);
		
		this.addWindowListener(new java.awt.event.WindowAdapter(){
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent){
				
				ScoreFileHandler.saveScores();
				VariableFileHandler.saveVariables();
				
				int oContinue = JOptionPane.showConfirmDialog(new JDialog(), "Save files?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);			
				
				if(oContinue == JOptionPane.YES_OPTION){
					ProjectFileHandler.saveAll();
					System.exit(0);
				}else if(oContinue == JOptionPane.NO_OPTION){
					System.exit(0);
				}else if(oContinue == JOptionPane.CANCEL_OPTION){
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
				
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenu mnNew = new JMenu("New");
		mnNew.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/File.png")));
		mnFile.add(mnNew);
		
		JMenuItem mntmProject = new JMenuItem("Project");
		mntmProject.setAccelerator(KeyStroke.getKeyStroke('B', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreateProject cP = new CreateProject();
				cP.setVisible(true);
			}
		});
		mntmProject.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Project.png")));
		mnNew.add(mntmProject);
		
		JMenuItem mntmExecute = new JMenuItem("Execute");
		mntmExecute.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmExecute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CreateExecute cE = new CreateExecute();
				cE.setVisible(true);
			}
		});
		mntmExecute.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Execute.png")));
		mnNew.add(mntmExecute);
		
		JMenuItem mntmOpenFile = new JMenuItem("Open Project");
		mntmOpenFile.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Open.png")));
		mnFile.add(mntmOpenFile);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{			
					ProjectFileHandler.saveFile(ProjectTab.getTab(getTabbedCodePane().getSelectedIndex()), 
											getTabbedCodePane().getTitleAt(getTabbedCodePane().getSelectedIndex()), 
											((JEditorPane) ((JScrollPane) getTabbedCodePane().getComponentAt(getTabbedCodePane().getSelectedIndex())).getViewport().getView()).getText());
					
				}catch(ArrayIndexOutOfBoundsException ex){
					
				}
			}
		});
		mntmSave.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Save.png")));
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SaveAs saveAs = new SaveAs();
				saveAs.setVisible(true);
			}
		});
		mntmSaveAs.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Save.png")));
		mnFile.add(mntmSaveAs);
		
		JMenuItem mntmSaveAll = new JMenuItem("Save All");
		mntmSaveAll.setAccelerator(KeyStroke.getKeyStroke('S', ActionEvent.CTRL_MASK+ActionEvent.ALT_MASK));
		mntmSaveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProjectFileHandler.saveAll();
			}
		});
		mntmSaveAll.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Save.png")));
		mnFile.add(mntmSaveAll);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		final JMenuItem mntmDisableAutocomplete = new JMenuItem("Autocomplete");
		mntmDisableAutocomplete.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mntmDisableAutocomplete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(autoComplete){
					autoComplete = false;
					mntmDisableAutocomplete.setIcon(null);
				}else{
					autoComplete = true;
					mntmDisableAutocomplete.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
				}
			}
		});
		
		mnOptions.add(mntmDisableAutocomplete);
		mntmDisableAutocomplete.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
		
		final JMenuItem mntmValidate = new JMenuItem("Validate");
		mntmValidate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(validate){
					validate = false;
					mntmValidate.setIcon(null);
				}else{
					validate = true;
					mntmValidate.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
				}
			}
		});
		mntmValidate.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
		mntmValidate.setAccelerator(KeyStroke.getKeyStroke('D', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		mnOptions.add(mntmValidate);
		
		JSeparator separator_1 = new JSeparator();
		mnOptions.add(separator_1);
		
		JMenuItem mntmNbtpath = new JMenuItem("NBTPath");
		mntmNbtpath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Structures save");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				
				chooser.showSaveDialog(null);
				
				ProjectManager.activeProject.getVariableHandler().addVariables("CBE$NBTPATH", chooser.getSelectedFile().getPath());
				
			}
		});
		mnOptions.add(mntmNbtpath);
		
		JMenu mnGeneration = new JMenu("Generation");
		menuBar.add(mnGeneration);
		
		final JMenuItem mntmSideways = new JMenuItem("SideWays");
		mntmSideways.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		final JMenuItem mntmUp = new JMenuItem("Up");
		mntmUp.setAccelerator(KeyStroke.getKeyStroke('W', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		final JMenuItem mntmNbt = new JMenuItem("NBT");
		mntmNbt.setAccelerator(KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
		mntmSideways.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Builder.setAlgo(algorithmus.sideways);
				mntmSideways.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
				mntmUp.setIcon(null);
				mntmNbt.setIcon(null);
			}
		});
		
		mntmNbt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Builder.setAlgo(algorithmus.nbt);
				mntmNbt.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
				mntmSideways.setIcon(null);
				mntmUp.setIcon(null);
			}
		});
		mnGeneration.add(mntmNbt);
		mntmNbt.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
		mnGeneration.add(mntmSideways);
		
		mntmUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Builder.setAlgo(algorithmus.up);
				mntmUp.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Check.png")));
				mntmSideways.setIcon(null);
				mntmNbt.setIcon(null);
				
			}
		});
		mnGeneration.add(mntmUp);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmSyntax = new JMenuItem("General Help");
		mntmSyntax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GeneralHelp help = new GeneralHelp();
				help.setVisible(true);
			}
		});
		mnHelp.add(mntmSyntax);
		
		JMenuItem mntmAboutCbe = new JMenuItem("About CBE");
		mntmAboutCbe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutGUI gui = new AboutGUI();
				gui.setVisible(true);
			}
		});
		mnHelp.add(mntmAboutCbe);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		root = new DefaultMutableTreeNode(path);
		
		ProjectFileHandler.loadProjects(path, root);
		
		if(!ProjectManager.projects.isEmpty()){
			ProjectManager.activeProject = ProjectManager.projects.get(0);	
			ScoreFileHandler.loadScores();
			VariableFileHandler.loadVariables();
			updateTitle();
		}
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{141, 790, 0};
		gbl_contentPane.rowHeights = new int[]{260, 39, 39, 39, 39, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JButton btnVariable = new JButton("Variables        ");
		btnVariable.setOpaque(false);
		btnVariable.setContentAreaFilled(false);
		btnVariable.setBorderPainted(false);
		btnVariable.setHorizontalAlignment(SwingConstants.LEFT);
		btnVariable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				VariablesGUI gui = new VariablesGUI();
				gui.setVisible(true);
			}
		});
		
		JTabbedPane tabbedNavigationBar = new JTabbedPane(JTabbedPane.TOP);
		
		projectTree = new JTree(root);
		projectTree.setRootVisible(false);
		projectTree.setCellRenderer(new ProjectTreeRenderer());
		projectTree.addMouseListener(new ProjectMouseListener(projectTree));
		projectTree.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2){
					ProjectFileHandler.openFile(projectTree);
		}}});
		((DefaultTreeModel) projectTree.getModel()).reload();
		JScrollPane tab1 = new JScrollPane(projectTree);
		tabbedNavigationBar.addTab("Project", null, tab1, "nothing");
		GridBagConstraints gbc_tabbedNavigationBar = new GridBagConstraints();
		gbc_tabbedNavigationBar.fill = GridBagConstraints.BOTH;
		gbc_tabbedNavigationBar.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedNavigationBar.gridx = 0;
		gbc_tabbedNavigationBar.gridy = 0;
		contentPane.add(tabbedNavigationBar, gbc_tabbedNavigationBar);
		tabbedCodeField.setEnabled(true);
		GridBagConstraints gbc_tabbedCodeField = new GridBagConstraints();
		gbc_tabbedCodeField.fill = GridBagConstraints.BOTH;
		gbc_tabbedCodeField.gridheight = 5;
		gbc_tabbedCodeField.gridx = 1;
		gbc_tabbedCodeField.gridy = 0;
		contentPane.add(tabbedCodeField, gbc_tabbedCodeField);
		btnVariable.setSelectedIcon(null);
		btnVariable.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Planet.png")));
		GridBagConstraints gbc_btnVariable = new GridBagConstraints();
		gbc_btnVariable.anchor = GridBagConstraints.NORTH;
		gbc_btnVariable.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnVariable.insets = new Insets(0, 0, 5, 5);
		gbc_btnVariable.gridx = 0;
		gbc_btnVariable.gridy = 1;
		contentPane.add(btnVariable, gbc_btnVariable);
		
		JButton btnScore = new JButton("Scoreboard    ");
		btnScore.setOpaque(false);
		btnScore.setContentAreaFilled(false);
		btnScore.setBorderPainted(false);
		btnScore.setHorizontalAlignment(SwingConstants.LEFT);
		btnScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ScoreGUI scoreHandler = new ScoreGUI();
				scoreHandler.setVisible(true);
			}
		});
		btnScore.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Scoreboard.png")));
		GridBagConstraints gbc_btnScore = new GridBagConstraints();
		gbc_btnScore.anchor = GridBagConstraints.NORTH;
		gbc_btnScore.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnScore.insets = new Insets(0, 0, 5, 5);
		gbc_btnScore.gridx = 0;
		gbc_btnScore.gridy = 2;
		contentPane.add(btnScore, gbc_btnScore);
		
		JButton btnDebug = new JButton(" Debug            ");
		btnDebug.setOpaque(false);
		btnDebug.setContentAreaFilled(false);
		btnDebug.setBorderPainted(false);
		btnDebug.setHorizontalAlignment(SwingConstants.LEFT);
		btnDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doCreate = false;
				
				if(ReadFiles.readFile()){
					DebugGUI debug = new DebugGUI();
					debug.setModal(true);
					debug.setVisible(true);
				}
			}
		});
		btnDebug.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Debug32.png")));
		GridBagConstraints gbc_btnDebug = new GridBagConstraints();
		gbc_btnDebug.anchor = GridBagConstraints.NORTH;
		gbc_btnDebug.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDebug.insets = new Insets(0, 0, 5, 5);
		gbc_btnDebug.gridx = 0;
		gbc_btnDebug.gridy = 3;
		contentPane.add(btnDebug, gbc_btnDebug);
		
		JButton btnCreate = new JButton(" Create           ");
		btnCreate.setOpaque(false);
		btnCreate.setContentAreaFilled(false);
		btnCreate.setBorderPainted(false);
		btnCreate.setHorizontalAlignment(SwingConstants.LEFT);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doCreate = true;
				ReadFiles.readFile();
			}
		});
		btnCreate.setIcon(new ImageIcon(MainWindow.class.getResource("/icon/Gear.png")));
		GridBagConstraints gbc_btnCreate = new GridBagConstraints();
		gbc_btnCreate.anchor = GridBagConstraints.NORTH;
		gbc_btnCreate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCreate.insets = new Insets(0, 0, 0, 5);
		gbc_btnCreate.gridx = 0;
		gbc_btnCreate.gridy = 4;
		contentPane.add(btnCreate, gbc_btnCreate);
		
		ReadCommands.read();
	}
	
	/**
	 * 	Create new Empty tab
	 * @param tabName - display name of the tab
	 * @param condition - condition for the block to execute
	 * @param initBlock - if this file contains ainit block
	 * */
	public void createNewTab(String project, String tabName, String condition, boolean initBlock){
		
		JEditorPane codeTab = new JEditorPane();
		AddUndoRedo undo = new AddUndoRedo(codeTab);
		
		if(initBlock){
			codeTab.setText("init:{\n}\nexecute:#" + condition + "\n{\n}");
		}else{
			codeTab.setText("execute:#" + condition + "\n{\n}");
		}
		
		SuggestionBox box = new SuggestionBox();
		
		codeTab.addKeyListener(new SuggestionListener(codeTab, box));
		
		JScrollPane codeScrollTab = new JScrollPane();
		codeScrollTab.setViewportView(codeTab);
		codeTab.add(box.getSuggestionBox());
		
		disableScroll(codeScrollTab);
		
		tabbedCodeField.addTab(tabName, null, codeScrollTab);
		tabbedCodeField.setTabComponentAt(tabbedCodeField.getTabCount() - 1, new CloseTabComponent(tabbedCodeField, tabName, codeTab));
		
		ProjectTab newTab = new ProjectTab(project);
		
	}

	/**
	 * 	Opens a new tab 
	 * 	@param tabName - name of the tab
	 * 	@param text - the text in the editor pane
	 * */
	public void openTab(String project, String tabName, String text){
		JEditorPane codeTab = new JEditorPane();
		AddUndoRedo undo = new AddUndoRedo(codeTab);
		
		SuggestionBox box = new SuggestionBox();
		
		codeTab.setText(text);
		codeTab.addKeyListener(new SuggestionListener(codeTab, box));

		JScrollPane codeScrollTab = new JScrollPane();
		codeScrollTab.setViewportView(codeTab);
		codeTab.add(box.getSuggestionBox());
		
		disableScroll(codeScrollTab);
		
		tabbedCodeField.addTab(tabName, null, codeScrollTab);
		tabbedCodeField.setTabComponentAt(tabbedCodeField.getTabCount() - 1, new CloseTabComponent(tabbedCodeField, tabName, codeTab));
		
		ProjectTab newTab = new ProjectTab(project);
		
	}
	
	private void disableScroll(JScrollPane pane){
		pane.getActionMap().put("unitScrollRight", new AbstractAction(){
		    public void actionPerformed(ActionEvent e) {
		    }});
		pane.getActionMap().put("unitScrollDown", new AbstractAction(){
		    public void actionPerformed(ActionEvent e) {
		    }});
	}
	
	public boolean doAutoComplete(){
		return autoComplete;
	}
	
	public boolean doValidate(){
		return validate;
	}
	
	public boolean doCreate(){
		return doCreate;
	}
	
	public String getPath(){
		return path;
	}
	
	public JTabbedPane getTabbedCodePane(){
		return tabbedCodeField;
	}
	
	public void updateTitle(){
		setTitle("CommandBlockEditor - Active Project: " + ProjectManager.activeProject.getName());
	}
	
	public JTree getProjectTree(){
		return projectTree;
	}
}
