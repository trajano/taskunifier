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
package com.leclercb.taskunifier.gui.components.modelselectiontable.table;

import java.util.Comparator;

import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.MappedValue;

import com.leclercb.taskunifier.gui.commons.comparators.BasicModelComparator;
import com.leclercb.taskunifier.gui.commons.values.BooleanValueBoolean;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueSelected;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.components.modelselectiontable.ModelSelectionColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableColumn;
import com.leclercb.taskunifier.gui.swing.table.TUTableProperties.TableColumnProperties;

public class ModelSelectionTableColumn extends TUTableColumn<ModelSelectionColumn> {
	
	private static final TableCellRenderer SELECT_RENDERER;
	private static final TableCellRenderer MODEL_RENDERER;
	
	private static final TableCellEditor BOOLEAN_EDITOR;
	
	static {
		SELECT_RENDERER = new DefaultTableRenderer(new MappedValue(
				null,
				IconValueSelected.INSTANCE,
				BooleanValueBoolean.INSTANCE), SwingConstants.CENTER);
		MODEL_RENDERER = new DefaultTableRenderer(new MappedValue(
				StringValueModel.INSTANCE_INDENTED,
				IconValueModel.INSTANCE));
		
		BOOLEAN_EDITOR = new JXTable.BooleanEditor();
	}
	
	public ModelSelectionTableColumn(
			TableColumnProperties<ModelSelectionColumn> column) {
		super(column);
	}
	
	@Override
	public Comparator<?> getComparator() {
		switch (this.column.getColumn()) {
			case SELECT:
				return super.getComparator();
			case MODEL:
				return BasicModelComparator.INSTANCE_NULL_LAST;
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
			case SELECT:
				return SELECT_RENDERER;
			case MODEL:
				return MODEL_RENDERER;
			default:
				return super.getCellRenderer();
		}
	}
	
	@Override
	public TableCellEditor getCellEditor() {
		switch (this.column.getColumn()) {
			case SELECT:
				return BOOLEAN_EDITOR;
			case MODEL:
				return super.getCellEditor();
			default:
				return super.getCellEditor();
		}
	}
	
}
