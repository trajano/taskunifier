/*
 * TaskUnifier: Manage your tasks and synchronize them
 * Copyright (C) 2010  Benjamin Leclerc
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.leclercb.taskunifier.gui.components.models.panels;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.models.GoalContributeModel;
import com.leclercb.taskunifier.gui.models.GoalModel;
import com.leclercb.taskunifier.gui.renderers.GoalLevelListCellRenderer;
import com.leclercb.taskunifier.gui.translations.Translations;

public class GoalConfigurationPanel extends JSplitPane {
	
	public GoalConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		// Initialize Fields
		final JTextField goalTitle = new JTextField(30);
		final JComboBox goalLevel = new JComboBox();
		final JComboBox goalContributes = new JComboBox();
		
		// Initialize Model List
		final ModelList modelList = new ModelList(new GoalModel(false)) {
			
			private BeanAdapter<Goal> adapter;
			
			{
				this.adapter = new BeanAdapter<Goal>((Goal) null, true);
				
				ValueModel titleModel = this.adapter.getValueModel(Goal.PROP_TITLE);
				Bindings.bind(goalTitle, titleModel);
				
				ValueModel levelModel = this.adapter.getValueModel(Goal.PROP_LEVEL);
				goalLevel.setModel(new ComboBoxAdapter<GoalLevel>(
						GoalLevel.values(),
						levelModel));
				
				ValueModel contributesModel = this.adapter.getValueModel(Goal.PROP_CONTRIBUTES);
				goalContributes.setModel(new ComboBoxAdapter<Goal>(
						new GoalContributeModel(true),
						contributesModel));
			}
			
			@Override
			public void addModel() {
				Model model = GoalFactory.getInstance().create(
						Translations.getString("goal.default.title"));
				this.setSelectedModel(model);
				GoalConfigurationPanel.this.focusAndSelectTextInTextField(goalTitle);
			}
			
			@Override
			public void removeModel(Model model) {
				this.modelSelected(null);
				GoalFactory.getInstance().markToDelete(model);
			}
			
			@Override
			public void modelSelected(Model model) {
				this.adapter.setBean(model != null ? (Goal) model : null);
				goalTitle.setEnabled(model != null);
				goalLevel.setEnabled(model != null);
				goalContributes.setEnabled(model != null);
				
				if (model != null)
					goalContributes.setEnabled(!((Goal) model).getLevel().equals(
							GoalLevel.LIFE_TIME));
			}
			
		};
		
		this.setLeftComponent(modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(rightPanel);
		
		JPanel info = new JPanel();
		info.setLayout(new SpringLayout());
		rightPanel.add(info, BorderLayout.NORTH);
		
		JLabel label = null;
		
		// Goal Title
		label = new JLabel(
				Translations.getString("general.goal.title") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		goalTitle.setEnabled(false);
		info.add(goalTitle);
		
		// Goal Level
		label = new JLabel(
				Translations.getString("general.goal.level") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		goalLevel.setEnabled(false);
		goalLevel.setRenderer(new GoalLevelListCellRenderer());
		goalLevel.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				Goal goal = (Goal) modelList.getSelectedModel();
				goalContributes.setEnabled(!goal.getLevel().equals(
						GoalLevel.LIFE_TIME));
			}
			
		});
		info.add(goalLevel);
		
		// Goal Contributes
		label = new JLabel(Translations.getString("general.goal.contributes")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		goalContributes.setEnabled(false);
		info.add(goalContributes);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 3, 2, // rows, cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
		
		this.setDividerLocation(200);
	}
	
	private void focusAndSelectTextInTextField(JTextField field) {
		int length = field.getText().length();
		
		field.setSelectionStart(0);
		field.setSelectionEnd(length);
		
		field.requestFocus();
	}
	
}
