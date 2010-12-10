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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.models.ContextListModel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SpringUtils;

public class ContextConfigurationPanel extends JSplitPane implements PropertyChangeListener {

	private Context selectedContext;

	private JTextField contextTitle;

	public ContextConfigurationPanel() {
		this.initialize();
	}

	private void initialize() {
		// Initialize Fields
		this.contextTitle = new JTextField(30);

		// Initialize Model List
		final ModelList modelList = new ModelList(new ContextListModel()) {

			@Override
			public void addModel() {
				Model model = ContextFactory.getInstance().create(Translations.getString("context.default.title"));
				this.setSelectedModel(model);
				focusAndSelectTextInTextField(contextTitle);
			}

			@Override
			public void removeModel(Model model) {
				modelSelected(null);
				ContextFactory.getInstance().markToDelete((Context) this.getSelectedModel());
			}

			@Override
			public void modelSelected(Model model) {
				if (selectedContext != null)
					selectedContext.removePropertyChangeListener(ContextConfigurationPanel.this);

				selectedContext = (Context) model;

				if (selectedContext != null)
					selectedContext.addPropertyChangeListener(ContextConfigurationPanel.this);

				if (model == null) {
					contextTitle.setEnabled(false);
					contextTitle.setText("");
					return;
				}

				Context context = (Context) model;

				contextTitle.setEnabled(true);
				contextTitle.setText(context.getTitle());
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

		// Context Title
		label = new JLabel(Translations.getString("general.context.title") + ":", JLabel.TRAILING);
		info.add(label);

		contextTitle.setEnabled(false);
		contextTitle.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent event) {
				Context context = (Context) modelList.getSelectedModel();
				context.setTitle(contextTitle.getText());
			}

		});
		info.add(contextTitle);

		// Lay out the panel
		SpringUtils.makeCompactGrid(info,
				1, 2, //rows, cols
				6, 6, //initX, initY
				6, 6); //xPad, yPad

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
		if (evt.getPropertyName().equals(Context.PROP_MODEL_TITLE)) {
			if (!this.contextTitle.getText().equals(evt.getNewValue()))
				this.contextTitle.setText((String) evt.getNewValue());
		}
	}

}
