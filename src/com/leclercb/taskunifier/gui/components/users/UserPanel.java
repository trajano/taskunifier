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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.UserUtils;

public class UserPanel extends JPanel {
	
	public static final String ACTION_CHANGE_USER_NAME = "ACTION_CHANGE_USER_NAME";
	public static final String ACTION_SWITCH_TO_USER = "ACTION_SWITCH_TO_USER";
	public static final String ACTION_CREATE_NEW_USER = "ACTION_CREATE_NEW_USER";
	public static final String ACTION_DELETE_USER = "ACTION_DELETE_USER";
	
	private ActionSupport actionSupport;
	
	private JTextField userName;
	private UserList userList;
	
	private JButton switchToUserButton;
	private JButton createNewUserButton;
	private JButton deleteUserButton;
	
	public UserPanel() {
		this.actionSupport = new ActionSupport(this);
		
		this.initialize();
	}
	
	public UserList getUserList() {
		return this.userList;
	}
	
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
	public void changeUserName() {
		String user = this.userList.getSelectedUser();
		
		if (user == null) {
			this.selectUser();
			return;
		}
		
		UserUtils.getInstance().setUserName(user, this.userName.getText());
		this.userName.setText("");
		
		this.actionSupport.fireActionPerformed(0, ACTION_CHANGE_USER_NAME);
	}
	
	public void createNewUser() {
		String userName = JOptionPane.showInputDialog(
				this,
				Translations.getString("manage_users.new_user_name"),
				Translations.getString("general.manage_users"),
				JOptionPane.QUESTION_MESSAGE);
		
		if (userName == null)
			return;
		
		UserUtils.getInstance().createNewUser(userName);
		
		this.actionSupport.fireActionPerformed(0, ACTION_CREATE_NEW_USER);
	}
	
	public void deleteUser() {
		String user = this.userList.getSelectedUser();
		
		if (user == null) {
			this.selectUser();
			return;
		}
		
		String[] options = new String[] {
				Translations.getString("general.yes"),
				Translations.getString("general.no") };
		
		int result = JOptionPane.showOptionDialog(
				MainFrame.getInstance().getFrame(),
				Translations.getString(
						"manage_users.delete_user.confirm",
						UserUtils.getInstance().getUserName(user)),
				Translations.getString("general.question"),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		
		if (result == JOptionPane.NO_OPTION)
			return;
		
		UserUtils.getInstance().deleteUser(user);
		
		this.actionSupport.fireActionPerformed(0, ACTION_DELETE_USER);
	}
	
	public void switchToUser() {
		String user = this.userList.getSelectedUser();
		
		if (user == null) {
			this.selectUser();
			return;
		}
		
		Main.changeUser(user);
		
		this.actionSupport.fireActionPerformed(0, ACTION_SWITCH_TO_USER);
	}
	
	private void selectUser() {
		ErrorInfo info = new ErrorInfo(
				Translations.getString("general.error"),
				Translations.getString("manage_users.select_one_user"),
				null,
				null,
				null,
				null,
				null);
		
		JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 10));
		
		JPanel topPanel = new JPanel(new BorderLayout(5, 0));
		
		this.userName = new JTextField();
		
		JButton changeUserNameButton = new JButton(new ChangeUserNameAction());
		
		topPanel.add(new JLabel(Translations.getString("general.user_name")
				+ ": "), BorderLayout.WEST);
		topPanel.add(this.userName, BorderLayout.CENTER);
		topPanel.add(changeUserNameButton, BorderLayout.EAST);
		
		this.userList = new UserList();
		
		this.add(topPanel, BorderLayout.NORTH);
		this.add(this.userList, BorderLayout.CENTER);
		
		this.initializeButtonsPanel();
		
		this.userList.getList().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent evt) {
						boolean isMainUser = EqualsUtils.equals(
								UserPanel.this.userList.getSelectedUser(),
								Main.getUserId());
						
						UserPanel.this.switchToUserButton.setEnabled(!isMainUser);
						UserPanel.this.deleteUserButton.setEnabled(!isMainUser);
					}
					
				});
		
		this.actionSupport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				boolean isMainUser = EqualsUtils.equals(
						UserPanel.this.userList.getSelectedUser(),
						Main.getUserId());
				
				UserPanel.this.switchToUserButton.setEnabled(!isMainUser);
				UserPanel.this.deleteUserButton.setEnabled(!isMainUser);
			}
			
		});
	}
	
	private void initializeButtonsPanel() {
		this.switchToUserButton = new JButton(new SwitchToUserAction());
		this.createNewUserButton = new JButton(new CreateNewUserAction());
		this.deleteUserButton = new JButton(new DeleteUserAction());
		JPanel panel = new TUButtonsPanel(
				this.switchToUserButton,
				this.createNewUserButton,
				this.deleteUserButton);
		
		this.add(panel, BorderLayout.SOUTH);
	}
	
	private class ChangeUserNameAction extends AbstractAction {
		
		public ChangeUserNameAction() {
			super(
					Translations.getString("manage_users.change_user_name"),
					ImageUtils.getResourceImage("user.png", 16, 16));
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			UserPanel.this.changeUserName();
		}
		
	}
	
	private class CreateNewUserAction extends AbstractAction {
		
		public CreateNewUserAction() {
			super(
					Translations.getString("manage_users.create_new_user"),
					ImageUtils.getResourceImage("user.png", 16, 16));
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			UserPanel.this.createNewUser();
		}
		
	}
	
	private class DeleteUserAction extends AbstractAction {
		
		public DeleteUserAction() {
			super(
					Translations.getString("manage_users.delete_user"),
					ImageUtils.getResourceImage("user.png", 16, 16));
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			UserPanel.this.deleteUser();
		}
		
	}
	
	private class SwitchToUserAction extends AbstractAction {
		
		public SwitchToUserAction() {
			super(
					Translations.getString("manage_users.switch_user"),
					ImageUtils.getResourceImage("user.png", 16, 16));
			
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			UserPanel.this.switchToUser();
		}
		
	}
	
}
