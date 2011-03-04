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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.NumberFormatter;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.GuiLocation;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.models.LocationModel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class LocationConfigurationPanel extends JSplitPane {
	
	public LocationConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setBorder(null);
		
		// Initialize Fields
		final JTextField locationTitle = new JTextField(30);
		final JTextArea locationDescription = new JTextArea(5, 20);
		final JFormattedTextField locationLatitude = new JFormattedTextField(
				new NumberFormatter());
		final JFormattedTextField locationLongitude = new JFormattedTextField(
				new NumberFormatter());
		final JLabel locationColor = new JLabel();
		final JColorChooser locationColorChooser = new JColorChooser();
		
		// Initialize Model List
		final ModelList modelList = new ModelList(new LocationModel(false)) {
			
			private BeanAdapter<Location> adapter;
			
			{
				this.adapter = new BeanAdapter<Location>((Location) null, true);
				
				ValueModel titleModel = this.adapter.getValueModel(Model.PROP_TITLE);
				Bindings.bind(locationTitle, titleModel);
				
				ValueModel descriptionModel = this.adapter.getValueModel(Location.PROP_DESCRIPTION);
				Bindings.bind(locationDescription, descriptionModel);
				
				ValueModel latitudeModel = this.adapter.getValueModel(Location.PROP_LATITUDE);
				Bindings.bind(locationLatitude, latitudeModel);
				
				ValueModel longitudeModel = this.adapter.getValueModel(Location.PROP_LONGITUDE);
				Bindings.bind(locationLongitude, longitudeModel);
			}
			
			@Override
			public void addModel() {
				Model model = LocationFactory.getInstance().create(
						Translations.getString("location.default.title"));
				this.setSelectedModel(model);
				LocationConfigurationPanel.this.focusAndSelectTextInTextField(locationTitle);
			}
			
			@Override
			public void removeModel(Model model) {
				this.modelSelected(null);
				LocationFactory.getInstance().markToDelete(model);
			}
			
			@Override
			public void modelSelected(Model model) {
				this.adapter.setBean(model != null ? (Location) model : null);
				locationTitle.setEnabled(model != null);
				locationDescription.setEnabled(model != null);
				locationLatitude.setEnabled(model != null);
				locationLongitude.setEnabled(model != null);
				locationColor.setEnabled(model != null);
				
				if (model == null) {
					locationColor.setBackground(Color.GRAY);
					locationColorChooser.setColor(Color.GRAY);
				} else {
					locationColor.setBackground(((GuiLocation) model).getColor());
					locationColorChooser.setColor(((GuiLocation) model).getColor());
				}
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
		
		// Location Title
		label = new JLabel(Translations.getString("general.location.title")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		locationTitle.setEnabled(false);
		info.add(locationTitle);
		
		// Location Description
		label = new JLabel(
				Translations.getString("general.location.description") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		locationDescription.setEnabled(false);
		info.add(new JScrollPane(locationDescription));
		
		// Location Latitude
		label = new JLabel(Translations.getString("general.location.latitude")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		locationLatitude.setEnabled(false);
		info.add(locationLatitude);
		
		// Location Longitude
		label = new JLabel(Translations.getString("general.location.longitude")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		locationLongitude.setEnabled(false);
		info.add(locationLongitude);
		
		// Location Color
		label = new JLabel(Translations.getString("general.location.color")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		locationColor.setEnabled(false);
		locationColor.setOpaque(true);
		locationColor.setBackground(Color.GRAY);
		locationColor.setBorder(new LineBorder(Color.BLACK));
		
		locationColorChooser.setColor(Color.GRAY);
		
		final JDialog colorDialog = JColorChooser.createDialog(
				this,
				"Color",
				true,
				locationColorChooser,
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent event) {
						locationColor.setBackground(locationColorChooser.getColor());
						((GuiLocation) modelList.getSelectedModel()).setColor(locationColorChooser.getColor());
					}
					
				},
				null);
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (locationColor.isEnabled())
					colorDialog.setVisible(true);
			}
			
		});
		
		info.add(locationColor);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 5, 2, // rows, cols
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
