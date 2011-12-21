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
package com.leclercb.taskunifier.gui.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilter;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueModelId;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;

public final class NoteUtils {
	
	private NoteUtils() {
		
	}
	
	public static String toText(Note[] notes, NoteColumn[] columns, boolean html) {
		return toText(notes, columns, html, null, null);
	}
	
	public static String toText(Note[] notes, NoteColumn[] columns, boolean html, String header, String footer) {
		String[][] data = toStringData(notes, columns);
		StringBuffer buffer = new StringBuffer();
		
		if (data == null)
			return null;
		
		if (html)
			buffer.append("<html>");
		
		if (header != null)
			buffer.append(header);
		
		int i = 0;
		for (String[] row : data) {
			if (i == 0) {
				i++;
				continue;
			}
			
			for (int j = 0; j < row.length; j++) {
				if (!html) {
					buffer.append(data[0][j] + ": ");
					buffer.append(row[j]);
				} else {
					buffer.append("<b>" + data[0][j] + ":</b> ");
					
					String text = row[j];
					
					if (columns[j] == NoteColumn.NOTE)
						text = Text2HTML.convert(text);
					else
						text = StringEscapeUtils.escapeHtml4(text);
					
					buffer.append(text);
				}
				
				if (!html)
					buffer.append(System.getProperty("line.separator"));
				else
					buffer.append("<br />");
			}
			
			if (!html)
				buffer.append(System.getProperty("line.separator"));
			else
				buffer.append("<br />");
			
			i++;
		}
		
		if (footer != null)
			buffer.append(footer);
		
		if (html)
			buffer.append("</html>");
		
		return buffer.toString();
	}
	
	public static String toHtml(Note[] notes, NoteColumn[] columns) {
		String[][] data = toStringData(notes, columns);
		StringBuffer buffer = new StringBuffer();
		
		if (data == null)
			return null;
		
		buffer.append("<html>");
		buffer.append("<table>");
		
		int i = 0;
		for (String[] row : data) {
			if (i == 0)
				buffer.append("<tr style=\"font-weight:bold;\">");
			else
				buffer.append("<tr>");
			
			for (int j = 0; j < row.length; j++) {
				String text = row[j];
				
				if (columns[j] == NoteColumn.NOTE)
					text = Text2HTML.convert(text);
				else
					text = StringEscapeUtils.escapeHtml3(text);
				
				buffer.append("<td>" + text + "</td>");
			}
			
			buffer.append("</tr>");
			
			i++;
		}
		
		buffer.append("</table>");
		buffer.append("</html>");
		
		return buffer.toString();
	}
	
	public static String[][] toStringData(Note[] notes, NoteColumn[] columns) {
		CheckUtils.isNotNull(notes);
		CheckUtils.isNotNull(columns);
		
		List<String[]> data = new ArrayList<String[]>();
		
		int i = 0;
		String[] row = new String[columns.length];
		
		for (NoteColumn column : columns) {
			if (column == null)
				continue;
			
			row[i++] = column.getLabel();
		}
		
		data.add(row);
		
		for (Note note : notes) {
			if (note == null)
				continue;
			
			i = 0;
			row = new String[columns.length];
			
			for (NoteColumn column : columns) {
				if (column == null)
					continue;
				
				String content = null;
				Object value = column.getProperty(note);
				
				switch (column) {
					case FOLDER:
						content = StringValueModel.INSTANCE.getString(value);
						break;
					case MODEL:
						content = StringValueModelId.INSTANCE.getString(value);
						break;
					case MODEL_CREATION_DATE:
						content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
						break;
					case MODEL_UPDATE_DATE:
						content = StringValueCalendar.INSTANCE_DATE_TIME.getString(value);
						break;
					case NOTE:
						content = (value == null ? null : "\n"
								+ value.toString());
						break;
					case TITLE:
						content = (value == null ? null : value.toString());
						break;
				}
				
				if (content == null)
					content = "";
				
				row[i++] = content;
			}
			
			data.add(row);
		}
		
		return data.toArray(new String[0][]);
	}
	
	public static boolean showNote(Note note, NoteFilter filter) {
		if (!note.getModelStatus().isEndUserStatus()) {
			return false;
		}
		
		if (filter == null)
			return true;
		
		return filter.include(note);
	}
	
}
