/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.notesearchertree;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.ArrayUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.api.settings.ModelIdSettingsCoder;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.configuration.ConfigurationDialog.ConfigurationTab;
import com.leclercb.taskunifier.gui.components.models.ModelConfigurationDialog;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.FolderItem;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class NoteSearcherPanel extends JPanel implements SavePropertiesListener, NoteSearcherView, PropertyChangeSupported {
	
	public static final String PROP_TITLE_FILTER = "titleFilter";
	
	private NoteSearcherSelectionChangeSupport noteSearcherSelectionChangeSupport;
	
	private String settingsPrefix;
	
	private NoteSearcherTree searcherView;
	
	private Note[] notes;
	private String filter;
	
	public NoteSearcherPanel(String settingsPrefix) {
		this.noteSearcherSelectionChangeSupport = new NoteSearcherSelectionChangeSupport(
				this);
		
		this.settingsPrefix = settingsPrefix;
		
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.initialize();
	}
	
	@Override
	public void addExtraNotes(Note[] notes) {
		if (notes == null)
			return;
		
		if (this.notes == null) {
			this.setExtraNotes(notes);
			return;
		}
		
		this.setExtraNotes(ArrayUtils.concat(this.notes, notes));
	}
	
	@Override
	public void setExtraNotes(Note[] notes) {
		this.notes = notes;
		this.refreshNoteSearcher();
	}
	
	@Override
	public void setSearchFilter(String filter) {
		if (EqualsUtils.equals(this.filter, filter))
			return;
		
		String oldTitleFilter = this.filter;
		this.filter = filter;
		
		this.firePropertyChange(PROP_TITLE_FILTER, oldTitleFilter, filter);
	}
	
	@Override
	public void selectDefaultNoteSearcher() {
		this.searcherView.selectDefaultNoteSearcher();
	}
	
	@Override
	public boolean selectNoteSearcher(NoteSearcher searcher) {
		return this.searcherView.selectNoteSearcher(searcher);
	}
	
	@Override
	public boolean selectFolder(Folder foler) {
		return this.searcherView.selectFolder(foler);
	}
	
	@Override
	public NoteSearcher getSelectedNoteSearcher() {
		NoteSearcher searcher = this.searcherView.getSelectedNoteSearcher();
		
		if (searcher == null)
			return null;
		
		searcher = searcher.clone();
		
		NoteFilter originalFilter = searcher.getFilter();
		
		NoteFilter mainFilter = new NoteFilter();
		mainFilter.setLink(FilterLink.OR);
		
		NoteFilter newFilter = new NoteFilter();
		newFilter.setLink(FilterLink.AND);
		
		NoteFilter extraFilter = new NoteFilter();
		extraFilter.setLink(FilterLink.OR);
		
		NoteFilter searchFilter = new NoteFilter();
		searchFilter.setLink(FilterLink.OR);
		
		if (this.filter != null && this.filter.length() != 0) {
			searchFilter.addElement(new NoteFilterElement(
					NoteColumn.TITLE,
					StringCondition.CONTAINS,
					this.filter));
			searchFilter.addElement(new NoteFilterElement(
					NoteColumn.NOTE,
					StringCondition.CONTAINS,
					this.filter));
			searchFilter.addElement(new NoteFilterElement(
					NoteColumn.FOLDER,
					StringCondition.CONTAINS,
					this.filter));
		}
		
		if (this.notes != null) {
			for (Note note : this.notes) {
				extraFilter.addElement(new NoteFilterElement(
						NoteColumn.MODEL,
						ModelCondition.EQUALS,
						note));
			}
			
			mainFilter.addFilter(extraFilter);
		}
		
		mainFilter.addFilter(newFilter);
		
		newFilter.addFilter(searchFilter);
		newFilter.addFilter(originalFilter);
		
		searcher.setFilter(mainFilter);
		
		return searcher;
	}
	
	@Override
	public NoteSearcher getSelectedOriginalNoteSearcher() {
		return this.searcherView.getSelectedOriginalNoteSearcher();
	}
	
	@Override
	public void refreshNoteSearcher() {
		this.searcherView.refreshNoteSearcher();
		this.noteSearcherSelectionChangeSupport.fireNoteSearcherSelectionChange(this.getSelectedNoteSearcher());
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.searcherView = new NoteSearcherTree(this.settingsPrefix);
		
		this.add(
				ComponentFactory.createJScrollPane(this.searcherView, false),
				BorderLayout.CENTER);
		
		this.searcherView.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					TreePath path = NoteSearcherPanel.this.searcherView.getPathForLocation(
							e.getX(),
							e.getY());
					
					Object node = path.getLastPathComponent();
					
					if (node instanceof FolderItem)
						NoteSearcherPanel.this.openManageFolders((FolderItem) node);
				}
			}
			
		});
		
		this.searcherView.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent evt) {
				NoteSearcherPanel.this.notes = null;
				NoteSearcherPanel.this.noteSearcherSelectionChangeSupport.fireNoteSearcherSelectionChange(NoteSearcherPanel.this.getSelectedNoteSearcher());
			}
			
		});
		
		this.initializeSelectedSearcher();
	}
	
	private void openManageFolders(FolderItem item) {
		if (item.getFolder() == null) {
			ActionConfiguration.configuration(ConfigurationTab.SEARCHER);
			return;
		}
		
		ModelConfigurationDialog dialog = ModelConfigurationDialog.getInstance();
		dialog.setSelectedModel(ModelType.FOLDER, item.getFolder());
		dialog.setVisible(true);
	}
	
	@Override
	public void addNoteSearcherSelectionChangeListener(
			NoteSearcherSelectionListener listener) {
		this.noteSearcherSelectionChangeSupport.addNoteSearcherSelectionChangeListener(listener);
	}
	
	@Override
	public void removeNoteSearcherSelectionChangeListener(
			NoteSearcherSelectionListener listener) {
		this.noteSearcherSelectionChangeSupport.removeNoteSearcherSelectionChangeListener(listener);
	}
	
	private void initializeSelectedSearcher() {
		try {
			String value = Main.SETTINGS.getStringProperty(this.settingsPrefix
					+ ".selected.value");
			NoteSearcherType type = Main.SETTINGS.getEnumProperty(
					this.settingsPrefix + ".selected.type",
					NoteSearcherType.class);
			
			if (value != null && type != null) {
				if (type == NoteSearcherType.FOLDER) {
					ModelId id = new ModelIdSettingsCoder().decode(value);
					Folder folder = FolderFactory.getInstance().get(id);
					
					if (folder != null) {
						if (this.searcherView.selectFolder(folder))
							return;
					}
				}
				
				this.selectDefaultNoteSearcher();
				return;
			}
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.WARNING,
					"Error while initializing selected note searcher",
					t);
		}
		
		this.selectDefaultNoteSearcher();
	}
	
	@Override
	public void saveProperties() {
		try {
			NoteSearcher searcher = this.searcherView.getSelectedNoteSearcher();
			
			if (searcher == null)
				return;
			
			Main.SETTINGS.setEnumProperty(
					this.settingsPrefix + ".selected.type",
					NoteSearcherType.class,
					searcher.getType());
			
			if (searcher.getType() == NoteSearcherType.FOLDER) {
				if (this.searcherView.getSelectedFolder() != null) {
					ModelId id = this.searcherView.getSelectedFolder().getModelId();
					Main.SETTINGS.setStringProperty(
							this.settingsPrefix + ".selected.value",
							new ModelIdSettingsCoder().encode(id));
				}
				
				return;
			}
		} catch (Throwable t) {
			GuiLogger.getLogger().log(
					Level.WARNING,
					"Error while saving selected note searcher",
					t);
		}
		
		Main.SETTINGS.setStringProperty(
				this.settingsPrefix + ".selected.value",
				null);
	}
	
}
