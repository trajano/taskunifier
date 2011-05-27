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
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXColorSelectionButton;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.api.models.GuiContext;
import com.leclercb.taskunifier.gui.api.models.GuiModel;
import com.leclercb.taskunifier.gui.commons.converters.ColorConverter;
import com.leclercb.taskunifier.gui.commons.models.ContextModel;
import com.leclercb.taskunifier.gui.components.models.lists.IModelList;
import com.leclercb.taskunifier.gui.components.models.lists.ModelList;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.FormBuilder;
import com.leclercb.taskunifier.gui.utils.Images;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class ContextConfigurationPanel extends JSplitPane implements IModelList {
	
	private ModelList modelList;
	
	public ContextConfigurationPanel() {
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
		final JTextField contextTitle = new JTextField();
		final JXColorSelectionButton contextColor = new JXColorSelectionButton();
		final JButton removeColor = new JButton();
		
		// Set Disabled
		contextTitle.setEnabled(false);
		contextColor.setEnabled(false);
		removeColor.setEnabled(false);
		
		// Initialize Model List
		this.modelList = new ModelList(new ContextModel(false), contextTitle) {
			
			private BeanAdapter<Context> adapter;
			
			{
				this.adapter = new BeanAdapter<Context>((Context) null, true);
				
				ValueModel titleModel = this.adapter.getValueModel(Model.PROP_TITLE);
				Bindings.bind(contextTitle, titleModel);
				
				ValueModel colorModel = this.adapter.getValueModel(GuiModel.PROP_COLOR);
				Bindings.bind(contextColor, "background", new ColorConverter(
						colorModel));
			}
			
			@Override
			public Model addModel() {
				return ContextFactory.getInstance().create(
						Translations.getString("context.default.title"));
			}
			
			@Override
			public void removeModel(Model model) {
				ContextFactory.getInstance().markToDelete(model);
			}
			
			@Override
			public void modelSelected(Model model) {
				this.adapter.setBean(model != null ? (Context) model : null);
				contextTitle.setEnabled(model != null);
				contextColor.setEnabled(model != null);
				removeColor.setEnabled(model != null);
			}
			
		};
		
		this.setLeftComponent(this.modelList);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new BorderLayout());
		this.setRightComponent(ComponentFactory.createJScrollPane(
				rightPanel,
				false));
		
		FormBuilder builder = new FormBuilder(
				"right:pref, 4dlu, fill:default:grow");
		
		// Context Title
		builder.appendI15d("general.context.title", true, contextTitle);
		
		// Context Color
		JPanel p = new JPanel(new BorderLayout(5, 0));
		
		builder.appendI15d("general.color", true, p);
		
		contextColor.setPreferredSize(new Dimension(24, 24));
		contextColor.setBorder(BorderFactory.createEmptyBorder());
		
		removeColor.setIcon(Images.getResourceImage("remove.png", 16, 16));
		removeColor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((GuiContext) ContextConfigurationPanel.this.modelList.getSelectedModel()).setColor(null);
			}
			
		});
		
		p.add(contextColor, BorderLayout.WEST);
		p.add(removeColor, BorderLayout.EAST);
		
		// Lay out the panel
		rightPanel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.setDividerLocation(200);
	}
	
}
