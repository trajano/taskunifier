package com.leclercb.taskunifier.gui.components.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXSearchField;

import com.explodingpixels.macwidgets.SourceListStandardColorScheme;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.components.modelnote.ModelNotePanel;
import com.leclercb.taskunifier.gui.components.modelnote.ModelNoteView;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.notes.NoteTableView;
import com.leclercb.taskunifier.gui.components.notes.table.NoteTable;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherPanel;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherView;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frame.FrameView;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class DefaultNoteView extends JPanel implements NoteView, SavePropertiesListener {
	
	private JSplitPane horizontalSplitPane;
	private JSplitPane verticalSplitPane;
	
	private NoteSearcherPanel noteSearcherPanel;
	private NoteTable noteTable;
	private ModelNotePanel noteNote;
	
	private JXSearchField searchField;
	
	public DefaultNoteView(FrameView frameView) {
		this.initialize();
	}
	
	@Override
	public JPanel getViewContent() {
		return this;
	}
	
	@Override
	public NoteSearcherView getNoteSearcherView() {
		return this.noteSearcherPanel;
	}
	
	@Override
	public NoteTableView getNoteTableView() {
		return this.noteTable;
	}
	
	@Override
	public ModelNoteView getModelNoteView() {
		return this.noteNote;
	}
	
	private void initialize() {
		Main.getSettings().addSavePropertiesListener(this);
		
		this.setLayout(new BorderLayout());
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			this.horizontalSplitPane = ComponentFactory.createThinJSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		} else {
			this.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
			
			this.horizontalSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT);
		}
		
		this.horizontalSplitPane.setOneTouchExpandable(true);
		
		this.verticalSplitPane = new JSplitPane(
				Main.getSettings().getIntegerProperty("view.notes.window.split"));
		this.verticalSplitPane.setOneTouchExpandable(true);
		this.verticalSplitPane.setBorder(BorderFactory.createEmptyBorder());
		
		JPanel searcherPane = new JPanel();
		searcherPane.setLayout(new BorderLayout());
		
		JPanel middlePane = new JPanel();
		middlePane.setLayout(new BorderLayout());
		
		JPanel notePane = new JPanel();
		notePane.setLayout(new BorderLayout());
		
		this.horizontalSplitPane.setLeftComponent(searcherPane);
		this.horizontalSplitPane.setRightComponent(this.verticalSplitPane);
		
		this.verticalSplitPane.setTopComponent(middlePane);
		this.verticalSplitPane.setBottomComponent(notePane);
		
		this.add(this.horizontalSplitPane, BorderLayout.CENTER);
		
		this.loadSplitPaneSettings();
		
		this.initializeSearchField();
		this.initializeSearcherList(searcherPane);
		this.initializeNoteTable(middlePane);
		this.initializeModelNote(notePane);
		
		this.noteSearcherPanel.refreshNoteSearcher();
	}
	
	private void loadSplitPaneSettings() {
		int hSplit = Main.getSettings().getIntegerProperty(
				"view.notes.window.horizontal_split");
		int vSplit = Main.getSettings().getIntegerProperty(
				"view.notes.window.vertical_split");
		
		this.horizontalSplitPane.setDividerLocation(hSplit);
		this.verticalSplitPane.setDividerLocation(vSplit);
	}
	
	@Override
	public void saveProperties() {
		Main.getSettings().setIntegerProperty(
				"view.notes.window.horizontal_split",
				this.horizontalSplitPane.getDividerLocation());
		Main.getSettings().setIntegerProperty(
				"view.notes.window.vertical_split",
				this.verticalSplitPane.getDividerLocation());
	}
	
	private void initializeSearchField() {
		this.searchField = new JXSearchField(
				Translations.getString("general.search"));
		this.searchField.setColumns(15);
		
		this.searchField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultNoteView.this.noteSearcherPanel.setSearchFilter(e.getActionCommand());
			}
			
		});
	}
	
	private void initializeSearcherList(JPanel searcherPane) {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(new SourceListStandardColorScheme().getActiveBackgroundColor());
		
		panel.add(northPanel, BorderLayout.NORTH);
		northPanel.add(this.searchField, BorderLayout.NORTH);
		
		searcherPane.add(panel, BorderLayout.CENTER);
		
		this.initializeNoteSearcherList(panel);
	}
	
	private void initializeNoteSearcherList(JPanel searcherPane) {
		this.noteSearcherPanel = new NoteSearcherPanel("notesearcher.notes");
		
		this.noteSearcherPanel.addPropertyChangeListener(
				NoteSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						String filter = (String) evt.getNewValue();
						if (!DefaultNoteView.this.searchField.getText().equals(
								filter))
							DefaultNoteView.this.searchField.setText(filter);
					}
					
				});
		
		searcherPane.add(this.noteSearcherPanel);
	}
	
	private void initializeNoteTable(JPanel middlePane) {
		this.noteTable = new NoteTable(new TUTableProperties<NoteColumn>(
				NoteColumn.class,
				"notecolumn",
				false));
		
		JPanel notePanel = new JPanel(new BorderLayout());
		notePanel.add(
				ComponentFactory.createJScrollPane(this.noteTable, false),
				BorderLayout.CENTER);
		
		this.noteSearcherPanel.addNoteSearcherSelectionChangeListener(this.noteTable);
		this.noteSearcherPanel.addPropertyChangeListener(
				NoteSearcherPanel.PROP_TITLE_FILTER,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						DefaultNoteView.this.noteTable.noteSearcherSelectionChange(new NoteSearcherSelectionChangeEvent(
								evt.getSource(),
								DefaultNoteView.this.noteSearcherPanel.getSelectedNoteSearcher()));
					}
					
				});
		
		middlePane.add(notePanel);
	}
	
	private void initializeModelNote(JPanel notePane) {
		this.noteNote = new ModelNotePanel("view.notes.modelnote");
		this.noteTable.addModelSelectionChangeListener(this.noteNote);
		notePane.add(this.noteNote);
	}
	
}
