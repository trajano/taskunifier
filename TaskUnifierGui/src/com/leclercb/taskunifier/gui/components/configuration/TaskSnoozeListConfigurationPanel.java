package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

import com.leclercb.taskunifier.gui.commons.comparators.TimeValueComparator;
import com.leclercb.taskunifier.gui.commons.values.StringValueTimeValue;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.timevalueedit.EditTimeValueDialog;
import com.leclercb.taskunifier.gui.swing.buttons.TUAddButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUEditButton;
import com.leclercb.taskunifier.gui.swing.buttons.TURemoveButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.TaskSnoozeList;
import com.leclercb.taskunifier.gui.utils.TaskSnoozeList.SnoozeItem;

public class TaskSnoozeListConfigurationPanel extends ConfigurationPanel {
	
	private JXList list;
	
	private JButton addButton;
	private JButton editButton;
	private JButton removeButton;
	
	public TaskSnoozeListConfigurationPanel(
			ConfigurationGroup configurationGroup) {
		super(configurationGroup);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		this.initializeList();
	}
	
	private void initializeList() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		
		panel.add(
				new JLabel(Translations.getString("general.tasksnoozelist")),
				BorderLayout.NORTH);
		
		EventList<SnoozeItem> eventList = new SortedList<SnoozeItem>(
				TaskSnoozeList.getInstance().getEventList(),
				new TimeValueComparator());
		
		this.list = new JXList();
		
		EventListModel<SnoozeItem> model = new EventListModel<SnoozeItem>(
				eventList);
		
		this.list.setModel(model);
		this.list.setSelectionModel(new EventSelectionModel<SnoozeItem>(
				eventList));
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setCellRenderer(new DefaultListRenderer(
				StringValueTimeValue.INSTANCE));
		
		panel.add(
				ComponentFactory.createJScrollPane(this.list, true),
				BorderLayout.CENTER);
		
		this.addButton = new TUAddButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				SnoozeItem item = new SnoozeItem();
				TaskSnoozeList.getInstance().add(item);
				EditTimeValueDialog.getInstance().setTimeValue(item);
				EditTimeValueDialog.getInstance().setVisible(true);
			}
			
		});
		
		this.editButton = new TUEditButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TaskSnoozeListConfigurationPanel.this.list.getSelectedValue() == null)
					return;
				
				SnoozeItem item = (SnoozeItem) TaskSnoozeListConfigurationPanel.this.list.getSelectedValue();
				EditTimeValueDialog.getInstance().setTimeValue(item);
				EditTimeValueDialog.getInstance().setVisible(true);
			}
			
		});
		
		this.removeButton = new TURemoveButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				for (Object value : TaskSnoozeListConfigurationPanel.this.list.getSelectedValues()) {
					TaskSnoozeList.getInstance().remove((SnoozeItem) value);
				}
			}
			
		});
		
		panel.add(new TUButtonsPanel(
				true,
				this.addButton,
				this.editButton,
				this.removeButton), BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
	@Override
	public void saveAndApplyConfig() {
		
	}
	
	@Override
	public void cancelConfig() {
		
	}
	
}
