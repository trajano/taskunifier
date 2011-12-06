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
package com.leclercb.taskunifier.gui.components.taskcontacts.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.table.TableColumnExt;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.components.taskcontacts.TaskContactsColumn;
import com.leclercb.taskunifier.gui.components.taskcontacts.table.editors.ContactEditor;
import com.leclercb.taskunifier.gui.components.taskcontacts.table.editors.LinkEditor;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;

public class TaskContactsTableColumn extends TableColumnExt {
	
	private static final TableCellRenderer LINK_RENDERER;
	private static final TableCellRenderer CONTACT_RENDERER;
	
	private static final TableCellEditor LINK_EDITOR;
	private static final TableCellEditor CONTACT_EDITOR;
	
	static {
		LINK_RENDERER = new DefaultTableRenderer();
		CONTACT_RENDERER = new DefaultTableRenderer(new MappedValue(
				StringValueModel.INSTANCE,
				IconValueModel.INSTANCE));
		
		LINK_EDITOR = new LinkEditor();
		CONTACT_EDITOR = new ContactEditor();
	}
	
	private TaskContactsColumn column;
	
	public TaskContactsTableColumn(TaskContactsColumn column) {
		super(column.ordinal());
		
		CheckUtils.isNotNull(column, "Task contacts column cannot be null");
		
		this.column = column;
		
		this.setIdentifier(column);
		this.setHeaderValue(column.getLabel());
		this.setPreferredWidth(column.getWidth());
		
		this.column.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(TaskColumn.PROP_VISIBLE)) {
					TaskContactsTableColumn.this.setVisible((Boolean) evt.getNewValue());
				}
				
				if (evt.getPropertyName().equals(TaskColumn.PROP_WIDTH)) {
					TaskContactsTableColumn.this.setPreferredWidth((Integer) evt.getNewValue());
				}
			}
			
		});
	}
	
	@Override
	public Comparator<?> getComparator() {
		switch (this.column) {
			case LINK:
				return super.getComparator();
			case CONTACT:
				return ModelComparator.INSTANCE;
			default:
				return super.getComparator();
		}
	}
	
	@Override
	public boolean isSortable() {
		return true;
	}
	
	@Override
	public void setPreferredWidth(int preferredWidth) {
		this.column.setWidth(preferredWidth);
		super.setPreferredWidth(preferredWidth);
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.column.setVisible(visible);
		super.setVisible(visible);
	}
	
	@Override
	public TableCellRenderer getCellRenderer() {
		switch (this.column) {
			case LINK:
				return LINK_RENDERER;
			case CONTACT:
				return CONTACT_RENDERER;
			default:
				return super.getCellRenderer();
		}
	}
	
	@Override
	public TableCellEditor getCellEditor() {
		switch (this.column) {
			case LINK:
				return LINK_EDITOR;
			case CONTACT:
				return CONTACT_EDITOR;
			default:
				return super.getCellEditor();
		}
	}
	
}
