package com.leclercb.taskunifier.gui.components.configuration.fields.lists;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationFieldType;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.buttons.TUAddButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TURemoveButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.TaskStatusList;

public class TaskStatusesFieldType extends ConfigurationFieldType.Panel implements PropertyChangeListener {
	
	private JPanel panel;
	
	private JXList list;
	
	private JButton addButton;
	private JButton removeButton;
	
	public TaskStatusesFieldType() {
		this.initialize();
		this.setPanel(this.panel);
	}
	
	private void initialize() {
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout(10, 10));
		
		this.initializeList();
	}
	
	private void initializeList() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		
		panel.add(
				new JLabel(Translations.getString("general.task.status")),
				BorderLayout.NORTH);
		
		EventList<String> eventList = new SortedList<String>(
				TaskStatusList.getInstance().getEventList());
		
		this.list = new JXList();
		
		EventListModel<String> model = new EventListModel<String>(eventList);
		
		this.list.setModel(model);
		this.list.setSelectionModel(new EventSelectionModel<String>(eventList));
		this.list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.list.setCellRenderer(new DefaultListRenderer(
				StringValueTaskStatus.INSTANCE));
		
		panel.add(
				ComponentFactory.createJScrollPane(this.list, true),
				BorderLayout.CENTER);
		
		this.addButton = new TUAddButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				String status = JOptionPane.showInputDialog(
						TaskStatusesFieldType.this.panel,
						Translations.getString("configuration.taskstatuses.enter_status"),
						Translations.getString("general.question"),
						JOptionPane.QUESTION_MESSAGE);
				
				if (status == null)
					return;
				
				TaskStatusList.getInstance().addStatus(status);
				TaskStatusesFieldType.this.list.setSelectedValue(status, true);
			}
			
		});
		
		this.removeButton = new TURemoveButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				for (Object value : TaskStatusesFieldType.this.list.getSelectedValues()) {
					TaskStatusList.getInstance().removeStatus(value.toString());
					
					if (TaskStatusesFieldType.this.list.getModel().getSize() > 0)
						TaskStatusesFieldType.this.list.setSelectedIndex(TaskStatusesFieldType.this.list.getModel().getSize() - 1);
				}
			}
			
		});
		
		panel.add(
				new TUButtonsPanel(true, this.addButton, this.removeButton),
				BorderLayout.SOUTH);
		
		this.panel.add(panel, BorderLayout.CENTER);
		
		this.refreshButtons();
		Main.getUserSettings().addPropertyChangeListener(
				"plugin.synchronizer.id",
				new WeakPropertyChangeListener(Main.getUserSettings(), this));
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		this.refreshButtons();
	}
	
	public void refreshButtons() {
		boolean enabled = TaskStatusList.getInstance().isEditable();
		this.addButton.setEnabled(enabled);
		this.removeButton.setEnabled(enabled);
	}
	
}
