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
package com.leclercb.taskunifier.gui.components.taskfiles.table;

import java.util.Comparator;

import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.MappedValue;

import com.leclercb.taskunifier.gui.commons.values.IconValueOpen;
import com.leclercb.taskunifier.gui.commons.values.StringValueTitle;
import com.leclercb.taskunifier.gui.components.taskfiles.TaskFilesColumn;
import com.leclercb.taskunifier.gui.components.taskfiles.table.editors.FileEditor;
import com.leclercb.taskunifier.gui.components.taskfiles.table.editors.LinkEditor;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskFilesTableColumn extends TUTableColumn<TaskFilesColumn> {
	
	private static final TableCellRenderer LINK_RENDERER;
	private static final TableCellRenderer FILE_RENDERER;
	private static final TableCellRenderer OPEN_RENDERER;
	
	private static final TableCellEditor LINK_EDITOR;
	private static final TableCellEditor FILE_EDITOR;
	
	static {
		LINK_RENDERER = new DefaultTableRenderer();
		FILE_RENDERER = new DefaultTableRenderer(new StringValueTitle(
				Translations.getString("general.no_value")));
		OPEN_RENDERER = new DefaultTableRenderer(new MappedValue(
				null,
				IconValueOpen.INSTANCE), SwingConstants.CENTER);
		
		LINK_EDITOR = new LinkEditor();
		FILE_EDITOR = new FileEditor();
	}
	
	public TaskFilesTableColumn(TableColumnProperties<TaskFilesColumn> column) {
		super(column);
	}
	
	@Override
	public Comparator<?> getComparator() {
		switch (this.column.getColumn()) {
			case LINK:
				return super.getComparator();
			case FILE:
				return super.getComparator();
			case OPEN:
				return super.getComparator();
			default:
				return super.getComparator();
		}
	}
	
	@Override
	public boolean isSortable() {
		return true;
	}
	
	@Override
	public TableCellRenderer getCellRenderer() {
		switch (this.column.getColumn()) {
			case LINK:
				return LINK_RENDERER;
			case FILE:
				return FILE_RENDERER;
			case OPEN:
				return OPEN_RENDERER;
			default:
				return super.getCellRenderer();
		}
	}
	
	@Override
	public TableCellEditor getCellEditor() {
		switch (this.column.getColumn()) {
			case LINK:
				return LINK_EDITOR;
			case FILE:
				return FILE_EDITOR;
			default:
				return super.getCellEditor();
		}
	}
	
}
