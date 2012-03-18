package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.models.TaskStatusModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.buttons.TUAddButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TURemoveButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.TaskStatusList;

public class PostponeListConfigurationPanel extends ConfigurationPanel implements PropertyChangeListener {
	
	private JXTable list;
	
	private TaskStatusModel model;
	
	private JButton addButton;
	private JButton removeButton;
	
	public PostponeListConfigurationPanel(ConfigurationGroup configurationGroup) {
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
				new JLabel(Translations.getString("general.task.status")),
				BorderLayout.NORTH);
		
		this.list = new JXList();
		
		this.model = new TaskStatusModel(false);
		
		this.list.setModel(this.model);
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
						PostponeListConfigurationPanel.this,
						Translations.getString("configuration.taskstatuses.enter_status"),
						Translations.getString("general.question"),
						JOptionPane.QUESTION_MESSAGE);
				
				if (status == null)
					return;
				
				TaskStatusList.getInstance().addStatus(status);
			}
			
		});
		
		this.removeButton = new TURemoveButton(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				for (Object value : PostponeListConfigurationPanel.this.list.getSelectedValues()) {
					TaskStatusList.getInstance().removeStatus(value.toString());
				}
			}
			
		});
		
		panel.add(
				new TUButtonsPanel(true, this.addButton, this.removeButton),
				BorderLayout.SOUTH);
		
		this.add(panel, BorderLayout.CENTER);
		
		this.refreshButtons();
		Main.getUserSettings().addPropertyChangeListener(
				"plugin.synchronizer.id",
				new WeakPropertyChangeListener(Main.getUserSettings(), this));
	}
	
	@Override
	public void saveAndApplyConfig() {
		
	}
	
	@Override
	public void cancelConfig() {
		
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
