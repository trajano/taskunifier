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
package com.leclercb.taskunifier.gui.components.notes.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;

import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.table.TableColumnExt;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueNote;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModelId;
import com.leclercb.taskunifier.gui.commons.values.StringValueNoteTitle;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.notes.table.editors.FolderEditor;
import com.leclercb.taskunifier.gui.components.notes.table.sorter.NoteRowComparator;

public class NoteTableColumn extends TableColumnExt {
	
	private static final TableCellRenderer MODEL_ID_RENDERER;
	private static final TableCellRenderer MODEL_RENDERER;
	private static final TableCellRenderer MODEL_CREATION_DATE_RENDERER;
	private static final TableCellRenderer MODEL_UPDATE_DATE_RENDERER;
	private static final TableCellRenderer NOTE_RENDERER;
	private static final TableCellRenderer TITLE_RENDERER;
	
	private static final TableCellEditor FOLDER_EDITOR;
	private static final TableCellEditor GENERIC_EDITOR;
	
	static {
		MODEL_ID_RENDERER = new DefaultTableRenderer(
				StringValueModelId.INSTANCE);
		
		MODEL_RENDERER = new DefaultTableRenderer(new MappedValue(
				StringValueModel.INSTANCE,
				IconValueModel.INSTANCE));
		
		MODEL_CREATION_DATE_RENDERER = new DefaultTableRenderer(
				StringValueCalendar.INSTANCE_DATE_TIME);
		
		MODEL_UPDATE_DATE_RENDERER = new DefaultTableRenderer(
				StringValueCalendar.INSTANCE_DATE_TIME);
		
		NOTE_RENDERER = new DefaultTableRenderer(new MappedValue(
				StringValues.EMPTY,
				IconValueNote.INSTANCE));
		
		((DefaultTableRenderer) NOTE_RENDERER).getComponentProvider().setHorizontalAlignment(
				SwingConstants.CENTER);
		
		TITLE_RENDERER = new DefaultTableRenderer(StringValueNoteTitle.INSTANCE);
		FOLDER_EDITOR = new FolderEditor();
		GENERIC_EDITOR = new JXTable.GenericEditor();
	}
	
	private NoteColumn noteColumn;
	
	public NoteTableColumn(NoteColumn noteColumn) {
		super(noteColumn.ordinal());
		
		CheckUtils.isNotNull(noteColumn);
		
		this.noteColumn = noteColumn;
		
		this.setIdentifier(noteColumn);
		this.setHeaderValue(noteColumn.getLabel());
		this.setPreferredWidth(noteColumn.getWidth());
		this.setVisible(noteColumn.isVisible());
		
		this.noteColumn.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(NoteColumn.PROP_VISIBLE)) {
					NoteTableColumn.this.setVisible((Boolean) evt.getNewValue());
				}
				
				if (evt.getPropertyName().equals(NoteColumn.PROP_WIDTH)) {
					NoteTableColumn.this.setPreferredWidth((Integer) evt.getNewValue());
				}
			}
			
		});
	}
	
	@Override
	public Comparator<?> getComparator() {
		if (this.noteColumn == NoteColumn.MODEL)
			return NoteRowComparator.getInstance();
		
		return super.getComparator();
	}
	
	@Override
	public boolean isSortable() {
		if (this.noteColumn == NoteColumn.MODEL)
			return true;
		
		return false;
	}
	
	@Override
	public void setPreferredWidth(int preferredWidth) {
		this.noteColumn.setWidth(preferredWidth);
		super.setPreferredWidth(preferredWidth);
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.noteColumn.setVisible(visible);
		super.setVisible(visible);
	}
	
	@Override
	public TableCellRenderer getCellRenderer() {
		switch (this.noteColumn) {
			case MODEL:
				return MODEL_ID_RENDERER;
			case MODEL_CREATION_DATE:
				return MODEL_CREATION_DATE_RENDERER;
			case MODEL_UPDATE_DATE:
				return MODEL_UPDATE_DATE_RENDERER;
			case TITLE:
				return TITLE_RENDERER;
			case FOLDER:
				return MODEL_RENDERER;
			case NOTE:
				return NOTE_RENDERER;
			default:
				return super.getCellRenderer();
		}
	}
	
	@Override
	public TableCellEditor getCellEditor() {
		switch (this.noteColumn) {
			case TITLE:
				return GENERIC_EDITOR;
			case FOLDER:
				return FOLDER_EDITOR;
			default:
				return super.getCellEditor();
		}
	}
	
}
