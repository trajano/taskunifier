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
package com.leclercb.taskunifier.gui.components.plugins.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import com.leclercb.taskunifier.gui.api.plugins.Plugin;
import com.leclercb.taskunifier.gui.translations.Translations;

public class PluginListRenderer implements ListCellRenderer {
	
	private JPanel panel;
	private JLabel icon;
	private JLabel text;
	
	public PluginListRenderer() {
		this.panel = new JPanel(new BorderLayout());
		this.icon = new JLabel();
		this.text = new JLabel();
		
		this.icon.setOpaque(true);
		this.text.setOpaque(true);
		
		this.icon.setBorder(null);
		this.text.setBorder(null);
		
		this.panel.add(this.icon, BorderLayout.EAST);
		this.panel.add(this.text, BorderLayout.CENTER);
	}
	
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
		Plugin plugin = (Plugin) value;
		
		String price = null;
		if (plugin.getPrice() == null || plugin.getPrice().length() == 0) {
			price = Translations.getString("plugin.price.free");
		} else {
			price = plugin.getPrice()
					+ " ("
					+ Translations.getString("plugin.price.free_trial")
					+ ")";
		}
		
		StringBuffer text = new StringBuffer();
		text.append("<b>"
				+ plugin.getName()
				+ "</b> - "
				+ plugin.getVersion()
				+ "<br />");
		text.append(Translations.getString("plugin.author")
				+ ": "
				+ plugin.getAuthor()
				+ "<br />");
		text.append(Translations.getString("plugin.price") + ": " + price);
		
		if (isSelected)
			this.text.setBackground(UIManager.getColor("List.selectionBackground"));
		else
			this.text.setBackground(Color.WHITE);
		
		this.icon.setIcon(plugin.getLogo());
		this.text.setText("<html>" + text.toString() + "</html>");
		
		return this.panel;
	}
	
}
