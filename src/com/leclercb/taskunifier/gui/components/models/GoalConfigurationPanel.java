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
package com.leclercb.taskunifier.gui.components.models;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.enums.GoalLevel;
import com.leclercb.taskunifier.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.models.GoalContributeComboBoxModel;
import com.leclercb.taskunifier.gui.models.GoalListModel;
import com.leclercb.taskunifier.gui.renderers.GoalLevelListCellRenderer;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SpringUtils;

public class GoalConfigurationPanel extends JSplitPane implements PropertyChangeListener {

	private Goal selectedGoal;

	private JTextField goalTitle;
	private JComboBox goalLevel;
	private JComboBox goalContributes;

	public GoalConfigurationPanel() {
		this.initialize();
	}

	private void initialize() {
		// Initialize Fields
		this.goalTitle = new JTextField(30);
		this.goalLevel = new JComboBox(GoalLevel.values());
		this.goalContributes = new JComboBox(new GoalContributeComboBoxModel());

		// Initialize Model List
		final ModelList modelList = new ModelList(new GoalListModel()) {

			@Override
			public void addModel() {
				Model model = GoalFactory.getInstance().create(Translations.getString("goal.default.title"));
				this.setSelectedModel(model);
				GoalConfigurationPanel.this.focusAndSelectTextInTextField(GoalConfigurationPanel.this.goalTitle);
			}

			@Override
			public void removeModel(Model model) {
				this.modelSelected(null);
				GoalFactory.getInstance().markToDelete(this.getSelectedModel());
			}

			@Override
			public void modelSelected(Model model) {
				if (GoalConfigurationPanel.this.selectedGoal != null)
					GoalConfigurationPanel.this.selectedGoal.removePropertyChangeListener(GoalConfigurationPanel.this);

				GoalConfigurationPanel.this.selectedGoal = (Goal) model;

				if (GoalConfigurationPanel.this.selectedGoal != null)
					GoalConfigurationPanel.this.selectedGoal.addPropertyChangeListener(GoalConfigurationPanel.this);

				if (model == null) {
					GoalConfigurationPanel.this.goalTitle.setEnabled(false);
					GoalConfigurationPanel.this.goalTitle.setText("");

					GoalConfigurationPanel.this.goalLevel.setEnabled(false);
					GoalConfigurationPanel.this.goalLevel.setSelectedItem(GoalLevel.LIFE_TIME);

					GoalConfigurationPanel.this.goalContributes.setEnabled(false);
					GoalConfigurationPanel.this.goalContributes.setSelectedItem(null);
					return;
				}

				Goal goal = (Goal) model;

				GoalConfigurationPanel.this.goalTitle.setEnabled(true);
				GoalConfigurationPanel.this.goalTitle.setText(goal.getTitle());

				GoalConfigurationPanel.this.goalLevel.setEnabled(true);
				GoalConfigurationPanel.this.goalLevel.setSelectedItem(goal.getLevel());

				GoalConfigurationPanel.this.goalContributes.setEnabled(!goal.getLevel().equals(GoalLevel.LIFE_TIME));
				GoalConfigurationPanel.this.goalContributes.setSelectedItem(goal.getContributes());
			}

		};

		this.setLeftComponent(modelList);

		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(rightPanel);

		JPanel info = new JPanel();
		info.setBorder(new LineBorder(Color.BLACK));
		info.setLayout(new SpringLayout());
		rightPanel.add(info, BorderLayout.NORTH);

		JLabel label = null;

		// Goal Title
		label = new JLabel(Translations.getString("general.goal.title") + ":", SwingConstants.TRAILING);
		info.add(label);

		this.goalTitle.setEnabled(false);
		this.goalTitle.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent event) {
				Goal goal = (Goal) modelList.getSelectedModel();
				goal.setTitle(GoalConfigurationPanel.this.goalTitle.getText());
			}

		});
		info.add(this.goalTitle);

		// Goal Level
		label = new JLabel(Translations.getString("general.goal.level") + ":", SwingConstants.TRAILING);
		info.add(label);

		this.goalLevel.setEnabled(false);
		this.goalLevel.setRenderer(new GoalLevelListCellRenderer());
		this.goalLevel.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Goal goal = (Goal) modelList.getSelectedModel();
				goal.setLevel((GoalLevel) GoalConfigurationPanel.this.goalLevel.getSelectedItem());

				GoalConfigurationPanel.this.goalContributes.setEnabled(!goal.getLevel().equals(GoalLevel.LIFE_TIME));
			}

		});
		info.add(this.goalLevel);

		// Goal Contributes
		label = new JLabel(Translations.getString("general.goal.contributes") + ":", SwingConstants.TRAILING);
		info.add(label);

		this.goalContributes.setEnabled(false);
		this.goalContributes.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				Goal goal = (Goal) modelList.getSelectedModel();
				if (!EqualsUtils.equals(goal.getContributes(),
						GoalConfigurationPanel.this.goalContributes.getSelectedItem()))
					goal.setContributes((Goal) GoalConfigurationPanel.this.goalContributes.getSelectedItem());
			}

		});
		info.add(this.goalContributes);

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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Goal.PROP_MODEL_TITLE)) {
			if (!EqualsUtils.equals(this.goalTitle.getText(), evt.getNewValue()))
				this.goalTitle.setText((String) evt.getNewValue());
		}

		if (evt.getPropertyName().equals(Goal.PROP_LEVEL)) {
			if (!EqualsUtils.equals(this.goalLevel.getSelectedItem(), evt.getNewValue()))
				this.goalLevel.setSelectedItem(evt.getNewValue());
		}

		if (evt.getPropertyName().equals(Goal.PROP_CONTRIBUTES)) {
			if (!EqualsUtils.equals(this.goalContributes.getSelectedItem(), evt.getNewValue())) {
				System.out.println("here " + this.goalContributes.getSelectedItem() + " " + evt.getNewValue());
				this.goalContributes.setSelectedItem(evt.getNewValue());
			}
		}
	}

}
