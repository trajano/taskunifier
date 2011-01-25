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

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
import com.leclercb.taskunifier.gui.models.LocationListModel;
import com.leclercb.taskunifier.gui.translations.Translations;

public class LocationConfigurationPanel extends JSplitPane {
	
	public LocationConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		// Initialize Fields
		final JTextField locationTitle = new JTextField(30);
		final JTextArea locationDescription = new JTextArea(5, 20);
		final JFormattedTextField locationLatitude = new JFormattedTextField(
				new NumberFormatter());
		final JFormattedTextField locationLongitude = new JFormattedTextField(
				new NumberFormatter());
		
		// Initialize Model List
		final ModelList modelList = new ModelList(new LocationListModel(false)) {
			
			private BeanAdapter<Location> adapter;
			
			{
				this.adapter = new BeanAdapter<Location>((Location) null, true);
				
				ValueModel titleModel = this.adapter.getValueModel(Location.PROP_TITLE);
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
		
		locationDescription.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		locationDescription.setEnabled(false);
		info.add(locationDescription);
		
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
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(info, 4, 2, // rows, cols
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
