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

import com.leclercb.taskunifier.gui.commons.models.TaskPostponeListModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPostponeItem;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.timevalueedit.EditTimeValueDialog;
import com.leclercb.taskunifier.gui.swing.buttons.TUAddButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUEditButton;
import com.leclercb.taskunifier.gui.swing.buttons.TURemoveButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.TaskPostponeList;
import com.leclercb.taskunifier.gui.utils.TaskPostponeList.PostponeItem;

public class TaskPostponeListConfigurationPanel extends ConfigurationPanel {
	
	private JXList list;
	
	private TaskPostponeListModel model;
	
	private JButton addButton;
	private JButton editButton;
	private JButton removeButton;
	
	public TaskPostponeListConfigurationPanel(
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
				new JLabel(Translations.getString("general.taskpostponelist")),
				BorderLayout.NORTH);
		
		this.list = new JXList();
		
		this.model = new TaskPostponeListModel(false);
		
		this.list.setModel(this.model);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setCellRenderer(new DefaultListRenderer(
				StringValueTaskPostponeItem.INSTANCE));
		
		panel.add(
				ComponentFactory.createJScrollPane(this.list, true),
				BorderLayout.CENTER);
		
		this.addButton = new TUAddButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				PostponeItem item = new PostponeItem();
				TaskPostponeList.getInstance().add(item);
				EditTimeValueDialog.getInstance().setTimeValue(item);
				EditTimeValueDialog.getInstance().setVisible(true);
			}
			
		});
		
		this.editButton = new TUEditButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				if (TaskPostponeListConfigurationPanel.this.list.getSelectedValue() == null)
					return;
				
				PostponeItem item = (PostponeItem) TaskPostponeListConfigurationPanel.this.list.getSelectedValue();
				EditTimeValueDialog.getInstance().setTimeValue(item);
				EditTimeValueDialog.getInstance().setVisible(true);
			}
			
		});
		
		this.removeButton = new TURemoveButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				for (Object value : TaskPostponeListConfigurationPanel.this.list.getSelectedValues()) {
					TaskPostponeList.getInstance().remove((PostponeItem) value);
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
