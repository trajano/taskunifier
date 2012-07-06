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
package com.leclercb.taskunifier.gui.components.tasks.table.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.api.models.GuiTask;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ShowChildrenRenderer extends DefaultTableCellRenderer {
	
	public ShowChildrenRenderer() {
		
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		Component component = super.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				row,
				column);
		
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setText("");
		this.setIcon(null);
		
		if (value == null)
			return component;
		
		GuiTask task = (GuiTask) ((TaskTable) table).getTask(row);
		
		boolean hasChildren = false;
		for (Task child : task.getChildren()) {
			if (child.getModelStatus().isEndUserStatus()) {
				hasChildren = true;
				break;
			}
		}
		
		if (hasChildren)
			if (task.isShowChildren())
				this.setIcon(ImageUtils.getResourceImage("collapse.png", 16, 16));
			else
				this.setIcon(ImageUtils.getResourceImage("expand.png", 16, 16));
		
		return component;
	}
	
}
