/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.components.models.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import javax.swing.text.NumberFormatter;

import org.jdesktop.swingx.JXColorSelectionButton;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.commons.gui.utils.SpringUtils;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.models.GuiLocation;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.commons.converters.ColorConverter;
import com.leclercb.taskunifier.gui.commons.models.LocationModel;
import com.leclercb.taskunifier.gui.components.models.lists.IModelList;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class LocationConfigurationPanel extends JSplitPane implements IModelList {
	
	private ModelList modelList;
	
	public LocationConfigurationPanel() {
		this.initialize();
	}
	
	@Override
	public Model getSelectedModel() {
		return this.modelList.getSelectedModel();
	}
	
	@Override
	public void setSelectedModel(Model model) {
		this.modelList.setSelectedModel(model);
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
		final JXColorSelectionButton locationColor = new JXColorSelectionButton();
		final JButton removeColor = new JButton();
		
		// Initialize Model List
		this.modelList = new ModelList(new LocationModel(false)) {
			
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
				
				ValueModel colorModel = this.adapter.getValueModel(GuiModel.PROP_COLOR);
				Bindings.bind(locationColor, "background", new ColorConverter(
						colorModel));
			}
			
			@Override
			public void addModel() {
				LocationFactory.getInstance().create(
						Translations.getString("location.default.title"));
				LocationConfigurationPanel.this.focusAndSelectTextInTextField(locationTitle);
			}
			
			@Override
			public void removeModel(Model model) {
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
				removeColor.setEnabled(model != null);
			}
			
		};
		
		this.setLeftComponent(this.modelList);
		
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
		label = new JLabel(
				Translations.getString("general.color") + ":",
				SwingConstants.TRAILING);
		info.add(label);
		
		locationColor.setEnabled(false);
		locationColor.setPreferredSize(new Dimension(24, 24));
		locationColor.setBorder(BorderFactory.createEmptyBorder());
		
		removeColor.setEnabled(false);
		removeColor.setIcon(Images.getResourceImage("remove.png", 16, 16));
		removeColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((GuiLocation) LocationConfigurationPanel.this.modelList.getSelectedModel()).setColor(null);
			}
			
		});
		
		JPanel p = new JPanel(new BorderLayout(5, 0));
		p.add(locationColor, BorderLayout.WEST);
		p.add(removeColor, BorderLayout.EAST);
		
		info.add(p);
		
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
