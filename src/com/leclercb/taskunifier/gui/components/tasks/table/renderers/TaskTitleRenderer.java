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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.table.TaskTable;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class TaskTitleRenderer extends DefaultRenderer {
	
	public TaskTitleRenderer() {

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
		
		if (value == null) {
			this.setForeground(Color.BLACK);
			component.setFont(this.getFont().deriveFont(Font.PLAIN));
			this.setText("");
			this.setIcon(null);
			return component;
		}
		
		Task task = ((TaskTable) table).getTask(row);
		
		Color foreground = Color.BLACK;
		String title = task.getTitle();
		
		if (title.length() == 0) {
			foreground = Color.GRAY;
			title = Translations.getString("task.default.title");
		}
		
		this.setForeground(foreground);
		
		// Set Text & Font
		if (task.getParent() == null) {
			component.setFont(this.getFont().deriveFont(Font.BOLD));
			this.setText(title);
		} else {
			component.setFont(this.getFont().deriveFont(Font.PLAIN));
			this.setText("          " + title);
		}
		
		// Set Icon
		if (!task.isCompleted() && task.isOverDue()) {
			this.setIcon(Images.getResourceImage("warning.gif"));
		} else {
			this.setIcon(Images.getResourceImage("transparent.png"));
		}
		
		return component;
	}
	
}
