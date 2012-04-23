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
package com.leclercb.taskunifier.gui.components.users;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.commons.comparators.UserComparator;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.commons.models.UserModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueUser;
import com.leclercb.taskunifier.gui.commons.values.StringValueUser;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class UserList extends JPanel {
	
	private JXList userList;
	
	public UserList() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 3));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.userList = new JXList();
		
		this.userList.setModel(new UserModel());
		this.userList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.userList.setCellRenderer(new DefaultListRenderer(
				StringValueUser.INSTANCE,
				IconValueUser.INSTANCE));
		
		this.userList.setAutoCreateRowSorter(true);
		this.userList.setComparator(UserComparator.INSTANCE);
		this.userList.setSortOrder(SortOrder.ASCENDING);
		this.userList.setSortsOnUpdates(true);
		
		this.userList.setHighlighters(new AlternateHighlighter());
		
		this.add(
				ComponentFactory.createJScrollPane(this.userList, true),
				BorderLayout.CENTER);
	}
	
	public JXList getList() {
		return this.userList;
	}
	
	public String getSelectedUser() {
		return (String) this.userList.getSelectedValue();
	}
	
	public void setSelectedUser(String user) {
		this.userList.setSelectedValue(user, true);
	}
	
}
