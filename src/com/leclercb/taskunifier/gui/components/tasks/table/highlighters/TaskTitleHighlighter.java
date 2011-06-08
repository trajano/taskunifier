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
package com.leclercb.taskunifier.gui.components.tasks.table.highlighters;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.JRendererLabel;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.components.tasks.TaskColumn;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class TaskTitleHighlighter extends AbstractHighlighter {
	
	private Color progressColor;
	
	public TaskTitleHighlighter(HighlightPredicate predicate) {
		super(predicate);
		
		this.resetColors();
		
		Main.SETTINGS.addPropertyChangeListener(
				"theme.color.progress",
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						TaskTitleHighlighter.this.resetColors();
						MainFrame.getInstance().getTaskView().refreshTasks();
					}
					
				});
	}
	
	@Override
	protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
		JRendererLabel r = (JRendererLabel) renderer;
		
		Object value = adapter.getFilteredValueAt(
				adapter.row,
				adapter.getColumnIndex(TaskColumn.MODEL));
		
		if (value == null || !(value instanceof Task))
			return r;
		
		final Task task = (Task) value;
		
		String title = task.getTitle();
		
		if (title.length() == 0) {
			title = Translations.getString("task.default.title");
		}
		
		// Set Foreground
		if (task.getTitle().length() == 0)
			r.setForeground(Color.GRAY);
		else
			r.setForeground(Color.BLACK);
		
		// Set Text & Font
		if (task.getParent() == null) {
			r.setFont(r.getFont().deriveFont(Font.BOLD));
			r.setText(title);
		} else {
			r.setFont(r.getFont().deriveFont(Font.PLAIN));
			r.setText("          " + title);
		}
		
		// Set Icon
		if (!task.isCompleted() && task.isOverDue())
			r.setIcon(Images.getResourceImage("warning.gif"));
		else
			r.setIcon(Images.getResourceImage("transparent.png"));
		
		// Set Progress
		r.setPainter(new Painter<JLabel>() {
			
			@Override
			public void paint(Graphics2D g, JLabel object, int width, int height) {
				int x = 18;
				int y = 3;
				width = (int) ((width - (x + 3)) * task.getProgress());
				height = height - (y + 3);
				
				Color c = g.getColor();
				g.setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(TaskTitleHighlighter.this.progressColor);
				g.fillRoundRect(x, y, width, height, 10, 10);
				g.setColor(c);
			}
			
		});
		
		return renderer;
	}
	
	private void resetColors() {
		if (Main.SETTINGS.getColorProperty("theme.color.progress") != null)
			this.progressColor = Main.SETTINGS.getColorProperty("theme.color.progress");
		else
			this.progressColor = Color.WHITE;
	}
	
}
