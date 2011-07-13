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

import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupported;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.ModelType;
import com.leclercb.taskunifier.api.settings.ModelIdSettingsCoder;
import com.leclercb.taskunifier.gui.actions.ActionConfiguration;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcherType;
import com.leclercb.taskunifier.gui.api.searchers.filters.FilterLink;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.configuration.ConfigurationDialog.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.models.ModelConfigurationDialog;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.notesearchertree.nodes.FolderItem;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class NoteSearcherPanel extends JPanel implements SavePropertiesListener, NoteSearcherView, PropertyChangeSupported, NoteSearcherSelectionListener {
	
	public static final String PROP_TITLE_FILTER = "titleFilter";
	
	private NoteSearcherSelectionChangeSupport noteSearcherSelectionChangeSupport;
	
	private NoteSearcherTree searcherView;
	
	private String titleFilter;
	
	public NoteSearcherPanel() {
		this.noteSearcherSelectionChangeSupport = new NoteSearcherSelectionChangeSupport(
				this);
		
		Main.SETTINGS.addSavePropertiesListener(this);
		
		this.initialize();
	}
	
	@Override
	public void setTitleFilter(String titleFilter) {
		if (EqualsUtils.equals(this.titleFilter, titleFilter))
			return;
		
		String oldTitleFilter = this.titleFilter;
		this.titleFilter = titleFilter;
		
		this.firePropertyChange(PROP_TITLE_FILTER, oldTitleFilter, titleFilter);
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
		
		if (this.titleFilter != null && this.titleFilter.length() != 0) {
			NoteFilter originalFilter = searcher.getFilter();
			
			NoteFilter newFilter = new NoteFilter();
			newFilter.setLink(FilterLink.AND);
			
			NoteFilter searchFilter = new NoteFilter();
			searchFilter.setLink(FilterLink.OR);
			searchFilter.addElement(new NoteFilterElement(
					NoteColumn.TITLE,
					StringCondition.CONTAINS,
					this.titleFilter));
			searchFilter.addElement(new NoteFilterElement(
					NoteColumn.NOTE,
					StringCondition.CONTAINS,
					this.titleFilter));
			
			newFilter.addFilter(searchFilter);
			newFilter.addFilter(originalFilter);
			
			searcher.setFilter(newFilter);
		}
		
		return searcher;
	}
	
	@Override
	public void refreshNoteSearcher() {
		this.noteSearcherSelectionChangeSupport.fireNoteSearcherSelectionChange(this.getSelectedNoteSearcher());
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.searcherView = new NoteSearcherTree();
		
		this.add(
				ComponentFactory.createJScrollPane(this.searcherView, false),
				BorderLayout.CENTER);
		
		this.searcherView.addNoteSearcherSelectionChangeListener(this);
		
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
		
		this.initializeSelectedSearcher();
	}
	
	private void openManageFolders(FolderItem item) {
		if (item.getFolder() == null) {
			ActionConfiguration.configuration(ConfigurationPanel.SEARCHER);
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
	
	@Override
	public void noteSearcherSelectionChange(
			NoteSearcherSelectionChangeEvent event) {
		NoteSearcher searcher = event.getSelectedNoteSearcher();
		
		if (searcher == null)
			return;
		
		this.setTitleFilter(null);
		
		this.noteSearcherSelectionChangeSupport.fireNoteSearcherSelectionChange(this.getSelectedNoteSearcher());
	}
	
	private void initializeSelectedSearcher() {
		try {
			String value = Main.SETTINGS.getStringProperty("searcher.note.selected.value");
			NoteSearcherType type = Main.SETTINGS.getEnumProperty(
					"searcher.note.selected.type",
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
			t.printStackTrace();
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
					"searcher.note.selected.type",
					NoteSearcherType.class,
					searcher.getType());
			
			if (searcher.getType() == NoteSearcherType.FOLDER) {
				if (this.searcherView.getSelectedFolder() != null) {
					ModelId id = this.searcherView.getSelectedFolder().getModelId();
					Main.SETTINGS.setStringProperty(
							"searcher.note.selected.value",
							new ModelIdSettingsCoder().encode(id));
				}
				
				return;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		Main.SETTINGS.setStringProperty("searcher.note.selected.value", null);
	}
	
}
