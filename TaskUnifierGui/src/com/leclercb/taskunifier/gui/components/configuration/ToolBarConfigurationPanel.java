package com.leclercb.taskunifier.gui.components.configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.MappedValue;

import com.leclercb.taskunifier.gui.actions.ActionList;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationGroup;
import com.leclercb.taskunifier.gui.components.configuration.api.ConfigurationPanel;
import com.leclercb.taskunifier.gui.components.configuration.toolbar.IconValueAction;
import com.leclercb.taskunifier.gui.components.configuration.toolbar.StringValueAction;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ToolBarConfigurationPanel extends ConfigurationPanel {
	
	private JXList leftList;
	private JXList rightList;
	
	private DefaultListModel leftModel;
	private DefaultListModel rightModel;
	
	public ToolBarConfigurationPanel(ConfigurationGroup configurationGroup) {
		super(configurationGroup);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(10, 10));
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		this.initializeLeftList();
		this.initializeRightList();
		
		JLabel afterRestartLabel = new JLabel(
				Translations.getString("configuration.general.settings_changed_after_restart"));
		afterRestartLabel.setForeground(Color.RED);
		
		this.add(afterRestartLabel, BorderLayout.SOUTH);
		
		this.reloadConfig();
	}
	
	private void initializeLeftList() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		
		panel.add(
				new JLabel(Translations.getString("general.actions")),
				BorderLayout.NORTH);
		
		this.leftList = new JXList();
		
		this.leftModel = new DefaultListModel();
		
		for (ActionList action : ActionList.values())
			if (action.isFitToolBar())
				this.leftModel.addElement(action);
		
		this.leftList.setModel(this.leftModel);
		this.leftList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.leftList.setCellRenderer(new DefaultListRenderer(new MappedValue(
				StringValueAction.INSTANCE,
				IconValueAction.INSTANCE,
				null)));
		
		this.leftList.setAutoCreateRowSorter(true);
		this.leftList.setComparator(new Comparator<ActionList>() {
			
			@Override
			public int compare(ActionList a1, ActionList a2) {
				return a1.getTitle().compareToIgnoreCase(a2.getTitle());
			}
			
		});
		this.leftList.setSortOrder(SortOrder.ASCENDING);
		this.leftList.setSortsOnUpdates(true);
		
		panel.add(
				ComponentFactory.createJScrollPane(this.leftList, true),
				BorderLayout.CENTER);
		
		JButton rightButton = new JButton(ImageUtils.getResourceImage(
				"right.png",
				16,
				16));
		rightButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ActionList[] actions = ToolBarConfigurationPanel.this.getLeftSelectedActions();
				for (ActionList action : actions) {
					ToolBarConfigurationPanel.this.rightModel.addElement(action);
				}
			}
			
		});
		
		JButton leftButton = new JButton(ImageUtils.getResourceImage(
				"left.png",
				16,
				16));
		leftButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] indices = ToolBarConfigurationPanel.this.rightList.getSelectedIndices();
				for (int i = indices.length - 1; i >= 0; i--) {
					ToolBarConfigurationPanel.this.rightModel.remove(indices[i]);
				}
			}
			
		});
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		buttonsPanel.add(Box.createVerticalGlue());
		buttonsPanel.add(rightButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		buttonsPanel.add(leftButton);
		buttonsPanel.add(Box.createVerticalGlue());
		
		panel.add(buttonsPanel, BorderLayout.EAST);
		
		this.add(panel, BorderLayout.WEST);
	}
	
	private void initializeRightList() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		
		panel.add(
				new JLabel(Translations.getString("configuration.tab.toolbar")),
				BorderLayout.NORTH);
		
		this.rightList = new JXList();
		
		this.rightModel = new DefaultListModel();
		
		this.rightList.setModel(this.rightModel);
		this.rightList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		this.rightList.setCellRenderer(new DefaultListRenderer(new MappedValue(
				StringValueAction.INSTANCE,
				IconValueAction.INSTANCE,
				null)));
		
		panel.add(
				ComponentFactory.createJScrollPane(this.rightList, true),
				BorderLayout.CENTER);
		
		JButton upButton = new JButton(ImageUtils.getResourceImage(
				"up.png",
				16,
				16));
		upButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] indices = ToolBarConfigurationPanel.this.rightList.getSelectedIndices();
				ActionList[] actions = ToolBarConfigurationPanel.this.getRightSelectedActions();
				
				if (indices.length == 0 || indices[0] == 0)
					return;
				
				for (int i = indices.length - 1; i >= 0; i--) {
					ToolBarConfigurationPanel.this.rightModel.remove(indices[i]);
				}
				
				for (ActionList action : actions) {
					ToolBarConfigurationPanel.this.rightModel.add(
							indices[0] - 1,
							action);
				}
				
				for (int i = 0; i < indices.length; i++) {
					indices[i]--;
				}
				
				ToolBarConfigurationPanel.this.rightList.setSelectedIndices(indices);
			}
			
		});
		
		JButton downButton = new JButton(ImageUtils.getResourceImage(
				"down.png",
				16,
				16));
		downButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] indices = ToolBarConfigurationPanel.this.rightList.getSelectedIndices();
				ActionList[] actions = ToolBarConfigurationPanel.this.getRightSelectedActions();
				
				if (indices.length == 0
						|| indices[indices.length - 1] == ToolBarConfigurationPanel.this.rightModel.getSize() - 1)
					return;
				
				for (int i = indices.length - 1; i >= 0; i--) {
					ToolBarConfigurationPanel.this.rightModel.remove(indices[i]);
				}
				
				for (int i = actions.length - 1; i >= 0; i--) {
					ToolBarConfigurationPanel.this.rightModel.add(
							indices[0] + 1,
							actions[i]);
				}
				
				for (int i = 0; i < indices.length; i++) {
					indices[i]++;
				}
				
				ToolBarConfigurationPanel.this.rightList.setSelectedIndices(indices);
			}
			
		});
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		buttonsPanel.add(Box.createVerticalGlue());
		buttonsPanel.add(upButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		buttonsPanel.add(downButton);
		buttonsPanel.add(Box.createVerticalGlue());
		
		panel.add(buttonsPanel, BorderLayout.EAST);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
	private ActionList[] getLeftSelectedActions() {
		Object[] values = this.leftList.getSelectedValues();
		List<ActionList> actions = new ArrayList<ActionList>();
		
		for (Object value : values) {
			actions.add((ActionList) value);
		}
		
		return actions.toArray(new ActionList[0]);
	}
	
	private ActionList[] getRightSelectedActions() {
		Object[] values = this.rightList.getSelectedValues();
		List<ActionList> actions = new ArrayList<ActionList>();
		
		for (Object value : values) {
			actions.add((ActionList) value);
		}
		
		return actions.toArray(new ActionList[0]);
	}
	
	@Override
	public void saveAndApplyConfig() {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < this.rightModel.getSize(); i++) {
			ActionList action = (ActionList) this.rightModel.getElementAt(i);
			buffer.append(action.name() + ";");
		}
		
		Main.getSettings().setStringProperty(
				"general.toolbar",
				buffer.toString());
	}
	
	@Override
	public void cancelConfig() {
		this.reloadConfig();
	}
	
	public void reloadConfig() {
		this.rightModel.removeAllElements();
		
		String value = Main.getSettings().getStringProperty("general.toolbar");
		
		if (value == null)
			return;
		
		String[] actions = StringUtils.split(value, ';');
		for (String action : actions) {
			action = action.trim();
			
			try {
				ActionList a = ActionList.valueOf(action);
				
				if (!a.isFitToolBar())
					continue;
				
				this.rightModel.addElement(a);
			} catch (Throwable t) {
				
			}
		}
	}
	
}
