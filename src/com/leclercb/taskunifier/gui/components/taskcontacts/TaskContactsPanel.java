package com.leclercb.taskunifier.gui.components.taskcontacts;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.api.models.Contact;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ContactList.ContactItem;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.actions.ActionManageModels;
import com.leclercb.taskunifier.gui.commons.comparators.ModelComparator;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.models.ModelConfigurationDialog.ModelConfigurationTab;
import com.leclercb.taskunifier.gui.components.taskcontacts.table.TaskContactsTable;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class TaskContactsPanel extends JPanel implements TaskContactsView, ModelSelectionListener {
	
	private JToolBar toolBar;
	private TaskContactsTable table;
	
	private Task previousSelectedTask;
	
	public TaskContactsPanel() {
		this.previousSelectedTask = null;
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		this.toolBar.setFloatable(false);
		
		this.toolBar.add(new AbstractAction("", ImageUtils.getResourceImage(
				"add.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				List<Contact> contacts = new ArrayList<Contact>(
						ContactFactory.getInstance().getList());
				
				Collections.sort(contacts, new ModelComparator());
				
				Contact newContact = null;
				for (Contact contact : contacts) {
					if (contact.getModelStatus().isEndUserStatus()) {
						newContact = contact;
						break;
					}
				}
				
				if (newContact == null) {
					ActionManageModels.manageModels(ModelConfigurationTab.CONTACT);
					return;
				}
				
				TaskContactsPanel.this.table.getContactList().add(
						new ContactItem(newContact, null));
			}
			
		});
		
		this.toolBar.add(new AbstractAction("", ImageUtils.getResourceImage(
				"removed.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				
			}
			
		});
		
		this.table = new TaskContactsTable();
		
		this.add(this.toolBar, BorderLayout.NORTH);
		this.add(
				ComponentFactory.createJScrollPane(this.table, false),
				BorderLayout.CENTER);
	}
	
	@Override
	public void modelSelectionChange(ModelSelectionChangeEvent event) {
		if (this.previousSelectedTask != null)
			this.previousSelectedTask.setContacts(this.table.getContactList());
		
		Model[] models = event.getSelectedModels();
		
		if (models.length != 1 || !(models[0] instanceof Task)) {
			this.table.setContactList(null);
			this.toolBar.setEnabled(false);
			this.table.setEnabled(false);
			return;
		}
		
		Task task = (Task) models[0];
		
		if (EqualsUtils.equals(task, this.previousSelectedTask))
			return;
		
		this.table.setContactList(task.getContacts());
		this.toolBar.setEnabled(true);
		this.table.setEnabled(true);
	}
	
}
