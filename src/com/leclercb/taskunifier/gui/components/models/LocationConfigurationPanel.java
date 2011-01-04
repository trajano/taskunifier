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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.models.LocationListModel;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SpringUtils;

public class LocationConfigurationPanel extends JSplitPane implements PropertyChangeListener {
	
	private Location selectedLocation;
	
	private JTextField locationTitle;
	private JTextArea locationDescription;
	private JTextField locationLatitude;
	private JTextField locationLongitude;
	
	public LocationConfigurationPanel() {
		this.initialize();
	}
	
	private void initialize() {
		// Initialize Fields
		this.locationTitle = new JTextField(30);
		this.locationDescription = new JTextArea(5, 20);
		this.locationLatitude = new JTextField();
		this.locationLongitude = new JTextField();
		
		// Initialize Model List
		final ModelList modelList = new ModelList(new LocationListModel()) {
			
			@Override
			public void addModel() {
				Model model = LocationFactory.getInstance().create(
						Translations.getString("location.default.title"));
				this.setSelectedModel(model);
				LocationConfigurationPanel.this.focusAndSelectTextInTextField(LocationConfigurationPanel.this.locationTitle);
			}
			
			@Override
			public void removeModel(Model model) {
				this.modelSelected(null);
				LocationFactory.getInstance().markToDelete(
						this.getSelectedModel());
			}
			
			@Override
			public void modelSelected(Model model) {
				if (LocationConfigurationPanel.this.selectedLocation != null)
					LocationConfigurationPanel.this.selectedLocation.removePropertyChangeListener(LocationConfigurationPanel.this);
				
				LocationConfigurationPanel.this.selectedLocation = (Location) model;
				
				if (LocationConfigurationPanel.this.selectedLocation != null)
					LocationConfigurationPanel.this.selectedLocation.addPropertyChangeListener(LocationConfigurationPanel.this);
				
				if (model == null) {
					LocationConfigurationPanel.this.locationTitle.setEnabled(false);
					LocationConfigurationPanel.this.locationTitle.setText("");
					
					LocationConfigurationPanel.this.locationDescription.setEnabled(false);
					LocationConfigurationPanel.this.locationDescription.setText("");
					
					LocationConfigurationPanel.this.locationLatitude.setEnabled(false);
					LocationConfigurationPanel.this.locationLatitude.setText("");
					
					LocationConfigurationPanel.this.locationLongitude.setEnabled(false);
					LocationConfigurationPanel.this.locationLongitude.setText("");
					return;
				}
				
				Location location = (Location) model;
				
				LocationConfigurationPanel.this.locationTitle.setEnabled(true);
				LocationConfigurationPanel.this.locationTitle.setText(location.getTitle());
				
				LocationConfigurationPanel.this.locationDescription.setEnabled(true);
				LocationConfigurationPanel.this.locationDescription.setText(location.getDescription());
				
				LocationConfigurationPanel.this.locationLatitude.setEnabled(true);
				LocationConfigurationPanel.this.locationLatitude.setText(location.getLatitude()
						+ "");
				
				LocationConfigurationPanel.this.locationLongitude.setEnabled(true);
				LocationConfigurationPanel.this.locationLongitude.setText(location.getLongitude()
						+ "");
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
		
		this.locationTitle.setEnabled(false);
		this.locationTitle.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent event) {
				Location location = (Location) modelList.getSelectedModel();
				location.setTitle(LocationConfigurationPanel.this.locationTitle.getText());
			}
			
		});
		info.add(this.locationTitle);
		
		// Location Description
		label = new JLabel(
				Translations.getString("general.location.description") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		this.locationDescription.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.locationDescription.setEnabled(false);
		this.locationDescription.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent event) {
				Location location = (Location) modelList.getSelectedModel();
				location.setDescription(LocationConfigurationPanel.this.locationDescription.getText());
			}
			
		});
		info.add(this.locationDescription);
		
		// Location Latitude
		label = new JLabel(Translations.getString("general.location.latitude")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		this.locationLatitude.setEnabled(false);
		this.locationLatitude.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent event) {
				try {
					double latitude = Double.parseDouble(LocationConfigurationPanel.this.locationLatitude.getText());
					LocationConfigurationPanel.this.locationLatitude.setBackground(UIManager.getColor("TextField.background"));
					Location location = (Location) modelList.getSelectedModel();
					location.setLatitude(latitude);
				} catch (NumberFormatException e) {
					LocationConfigurationPanel.this.locationLatitude.setBackground(Color.RED);
				}
			}
			
		});
		info.add(this.locationLatitude);
		
		// Location Longitude
		label = new JLabel(Translations.getString("general.location.longitude")
				+ ":", SwingConstants.TRAILING);
		info.add(label);
		
		this.locationLongitude.setEnabled(false);
		this.locationLongitude.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent event) {
				try {
					double longitude = Double.parseDouble(LocationConfigurationPanel.this.locationLongitude.getText());
					LocationConfigurationPanel.this.locationLongitude.setBackground(UIManager.getColor("TextField.background"));
					Location location = (Location) modelList.getSelectedModel();
					location.setLongitude(longitude);
				} catch (NumberFormatException e) {
					LocationConfigurationPanel.this.locationLongitude.setBackground(Color.RED);
				}
			}
			
		});
		info.add(this.locationLongitude);
		
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
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Location.PROP_MODEL_TITLE)) {
			if (!EqualsUtils.equals(
					this.locationTitle.getText(),
					evt.getNewValue()))
				this.locationTitle.setText((String) evt.getNewValue());
		}
		
		if (evt.getPropertyName().equals(Location.PROP_DESCRIPTION)) {
			if (!EqualsUtils.equals(
					this.locationDescription.getText(),
					evt.getNewValue()))
				this.locationDescription.setText((String) evt.getNewValue());
		}
		
		if (evt.getPropertyName().equals(Location.PROP_LATITUDE)) {
			if (!EqualsUtils.equals(
					Double.parseDouble(this.locationLatitude.getText()),
					evt.getNewValue()))
				this.locationLatitude.setText(evt.getNewValue() + "");
		}
		
		if (evt.getPropertyName().equals(Location.PROP_LONGITUDE)) {
			if (!EqualsUtils.equals(
					Double.parseDouble(this.locationLongitude.getText()),
					evt.getNewValue()))
				this.locationLongitude.setText(evt.getNewValue() + "");
		}
	}
	
}
