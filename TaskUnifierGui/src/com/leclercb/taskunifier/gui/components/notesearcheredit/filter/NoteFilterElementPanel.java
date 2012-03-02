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
package com.leclercb.taskunifier.gui.components.notesearcheredit.filter;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.leclercb.taskunifier.gui.api.searchers.filters.NoteFilterElement;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.Condition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.DaysCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.ModelCondition;
import com.leclercb.taskunifier.gui.api.searchers.filters.conditions.StringCondition;
import com.leclercb.taskunifier.gui.commons.models.FolderModel;
import com.leclercb.taskunifier.gui.commons.models.NoteModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueFilterCondition;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.utils.FormBuilder;

public class NoteFilterElementPanel extends JPanel {
	
	private NoteFilterElement element;
	
	private JXComboBox elementColumn;
	private JXComboBox elementCondition;
	private JXComboBox elementValueCb;
	private JTextField elementValueTf;
	
	public NoteFilterElementPanel() {
		this.initialize();
		this.setElement(null);
	}
	
	public NoteFilterElement getElement() {
		return this.element;
	}
	
	public void saveElement() {
		if (this.element != null) {
			Object elementValue = null;
			
			if (this.elementValueCb.isVisible())
				elementValue = this.elementValueCb.getSelectedItem();
			else
				elementValue = this.elementValueTf.getText();
			
			Object value = null;
			
			switch ((NoteColumn) this.elementColumn.getSelectedItem()) {
				case TITLE:
				case NOTE:
					value = elementValue.toString();
					break;
				case MODEL_CREATION_DATE:
				case MODEL_UPDATE_DATE:
					try {
						if (elementValue.toString().length() == 0)
							value = null;
						else
							value = Integer.parseInt(elementValue.toString());
					} catch (NumberFormatException e) {
						value = 0;
					}
					break;
				default:
					value = elementValue;
					break;
			}
			
			this.element.checkAndSet(
					(NoteColumn) this.elementColumn.getSelectedItem(),
					(Condition<?, ?>) this.elementCondition.getSelectedItem(),
					value);
		}
	}
	
	public void setElement(NoteFilterElement element) {
		if (element == null)
			this.resetFields(null, null, null);
		else
			this.resetFields(
					element.getProperty(),
					element.getCondition(),
					element.getValue());
		
		this.element = element;
	}
	
	private void resetFields(
			NoteColumn column,
			Condition<?, ?> condition,
			Object value) {
		NoteFilterElement currentElement = this.element;
		this.element = null;
		
		this.elementValueCb.setVisible(false);
		this.elementValueTf.setVisible(false);
		
		this.elementColumn.setEnabled(column != null);
		this.elementCondition.setEnabled(column != null);
		this.elementValueCb.setEnabled(column != null);
		this.elementValueTf.setEnabled(column != null);
		
		this.elementColumn.setModel(new DefaultComboBoxModel());
		this.elementCondition.setModel(new DefaultComboBoxModel());
		this.elementValueCb.setModel(new DefaultComboBoxModel());
		this.elementValueTf.setText("");
		
		if (column == null) {
			this.elementValueTf.setVisible(true);
			return;
		}
		
		DefaultComboBoxModel noteColumnsModel = new DefaultComboBoxModel(
				NoteColumn.values());
		
		this.elementColumn.setModel(noteColumnsModel);
		this.elementColumn.setSelectedItem(column);
		
		this.elementValueCb.setRenderer(new DefaultListCellRenderer());
		
		List<Condition<?, ?>> modelConditionList = new ArrayList<Condition<?, ?>>();
		modelConditionList.addAll(Arrays.asList(ModelCondition.values()));
		modelConditionList.addAll(Arrays.asList(StringCondition.values()));
		modelConditionList.remove(StringCondition.EQUALS);
		modelConditionList.remove(StringCondition.NOT_EQUALS);
		
		Object[] modelConditions = modelConditionList.toArray();
		
		switch (column) {
			case FOLDER:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						modelConditions));
				
				if (condition == null)
					condition = (Condition<?, ?>) modelConditions[0];
				
				if (condition instanceof StringCondition) {
					this.elementValueTf.setText(value == null ? "" : value.toString());
					this.elementValueTf.setVisible(true);
				}
				break;
		}
		
		switch (column) {
			case TITLE:
			case NOTE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						StringCondition.values()));
				this.elementValueTf.setText(value == null ? "" : value.toString());
				this.elementValueTf.setVisible(true);
				break;
			case MODEL_CREATION_DATE:
			case MODEL_UPDATE_DATE:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						DaysCondition.values()));
				this.elementValueTf.setText(value == null ? "" : value.toString());
				this.elementValueTf.setVisible(true);
				break;
			case MODEL:
				this.elementCondition.setModel(new DefaultComboBoxModel(
						ModelCondition.values()));
				this.elementValueCb.setModel(new NoteModel(true));
				this.elementValueCb.setRenderer(new DefaultListRenderer(
						StringValueModel.INSTANCE,
						IconValueModel.INSTANCE));
				this.elementValueCb.setSelectedItem(value);
				this.elementValueCb.setVisible(true);
				break;
			case FOLDER:
				if (condition instanceof ModelCondition) {
					this.elementValueCb.setModel(new FolderModel(true, true));
					this.elementValueCb.setRenderer(new DefaultListRenderer(
							StringValueModel.INSTANCE,
							IconValueModel.INSTANCE));
					this.elementValueCb.setSelectedItem(value);
					this.elementValueCb.setVisible(true);
				}
				break;
		}
		
		if (condition == null)
			this.elementCondition.setSelectedIndex(0);
		else
			this.elementCondition.setSelectedItem(condition);
		
		this.element = currentElement;
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		FormBuilder builder = new FormBuilder(
				"fill:default:grow, 10dlu, fill:default:grow, 10dlu, fill:default:grow");
		
		builder.appendI15d("searcheredit.element.column", true);
		builder.appendI15d("searcheredit.element.condition", true);
		builder.appendI15d("searcheredit.element.value", true);
		
		// Column
		this.elementColumn = new JXComboBox();
		this.elementColumn.setEnabled(false);
		this.elementColumn.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if (NoteFilterElementPanel.this.element == null)
					return;
				
				NoteFilterElementPanel.this.resetFields(
						(NoteColumn) NoteFilterElementPanel.this.elementColumn.getSelectedItem(),
						null,
						null);
			}
			
		});
		
		builder.append(this.elementColumn);
		
		// Condition
		this.elementCondition = new JXComboBox();
		this.elementCondition.setRenderer(new DefaultListRenderer(
				StringValueFilterCondition.INSTANCE));
		this.elementCondition.setEnabled(false);
		this.elementCondition.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent evt) {
				if (NoteFilterElementPanel.this.element == null)
					return;
				
				NoteColumn column = (NoteColumn) NoteFilterElementPanel.this.elementColumn.getSelectedItem();
				
				switch (column) {
					case FOLDER:
						break;
					default:
						return;
				}
				
				NoteFilterElementPanel.this.resetFields(
						(NoteColumn) NoteFilterElementPanel.this.elementColumn.getSelectedItem(),
						(Condition<?, ?>) NoteFilterElementPanel.this.elementCondition.getSelectedItem(),
						null);
			}
			
		});
		
		builder.append(this.elementCondition);
		
		// Value
		this.elementValueCb = new JXComboBox();
		this.elementValueCb.setEnabled(false);
		
		this.elementValueTf = new JTextField();
		this.elementValueTf.setEnabled(false);
		
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
		valuePanel.add(this.elementValueCb);
		valuePanel.add(this.elementValueTf);
		
		builder.append(valuePanel);
		
		// Lay out the panel
		panel.add(builder.getPanel(), BorderLayout.CENTER);
		
		this.add(panel, BorderLayout.CENTER);
	}
	
}
