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
package com.leclercb.taskunifier.gui.utils;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.jdesktop.swingx.renderer.DefaultListRenderer;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.actions.ActionPostponeTaskBeans;
import com.leclercb.taskunifier.gui.actions.ActionPostponeTasks;
import com.leclercb.taskunifier.gui.actions.PostponeType;
import com.leclercb.taskunifier.gui.commons.values.IconValueModel;
import com.leclercb.taskunifier.gui.commons.values.IconValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueModel;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskPriority;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskRepeatFrom;
import com.leclercb.taskunifier.gui.commons.values.StringValueTaskStatus;
import com.leclercb.taskunifier.gui.translations.Translations;

public final class ComponentFactory {
	
	private ComponentFactory() {
		
	}
	
	public static void createRepeatComboBox(JComboBox repeatComboBox) {
		CheckUtils.isNotNull(repeatComboBox);
		
		repeatComboBox.setEditable(true);
		
		final JTextField repeatTextField = (JTextField) repeatComboBox.getEditor().getEditorComponent();
		repeatTextField.getDocument().addDocumentListener(
				new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent event) {
						this.update();
					}
					
					@Override
					public void insertUpdate(DocumentEvent event) {
						this.update();
					}
					
					@Override
					public void changedUpdate(DocumentEvent event) {
						this.update();
					}
					
					private void update() {
						if (SynchronizerUtils.getSynchronizerPlugin().getSynchronizerApi().isValidRepeatValue(
								repeatTextField.getText()))
							repeatTextField.setForeground(Color.BLACK);
						else
							repeatTextField.setForeground(Color.RED);
					}
					
				});
	}
	
	@SuppressWarnings("unused")
	public static JXComboBox createModelComboBox(
			ComboBoxModel model,
			boolean autoComplete) {
		JXComboBox comboBox = new JXComboBox();
		
		if (model != null)
			comboBox.setModel(model);
		
		comboBox.setRenderer(new DefaultListRenderer(
				StringValueModel.INSTANCE_INDENTED,
				IconValueModel.INSTANCE));
		
		if (false) {
			AutoCompleteDecorator.decorate(
					comboBox,
					new ObjectToStringConverter() {
						
						@Override
						public String getPreferredStringForItem(Object item) {
							if (item == null)
								return null;
							
							return ((Model) item).getTitle();
						}
						
					});
		}
		
		return comboBox;
	}
	
	@SuppressWarnings("unused")
	public static JXComboBox createTaskPriorityComboBox(
			ComboBoxModel model,
			boolean autoComplete) {
		JXComboBox comboBox = new JXComboBox();
		
		if (model != null)
			comboBox.setModel(model);
		
		comboBox.setRenderer(new DefaultListRenderer(
				StringValueTaskPriority.INSTANCE,
				IconValueTaskPriority.INSTANCE));
		
		if (false) {
			AutoCompleteDecorator.decorate(
					comboBox,
					new ObjectToStringConverter() {
						
						@Override
						public String getPreferredStringForItem(Object item) {
							return StringValueTaskPriority.INSTANCE.getString(item);
						}
						
					});
		}
		
		return comboBox;
	}
	
	@SuppressWarnings("unused")
	public static JXComboBox createTaskRepeatFromComboBox(
			ComboBoxModel model,
			boolean autoComplete) {
		JXComboBox comboBox = new JXComboBox();
		
		if (model != null)
			comboBox.setModel(model);
		
		comboBox.setRenderer(new DefaultListRenderer(
				StringValueTaskRepeatFrom.INSTANCE));
		
		if (false) {
			AutoCompleteDecorator.decorate(
					comboBox,
					new ObjectToStringConverter() {
						
						@Override
						public String getPreferredStringForItem(Object item) {
							return StringValueTaskRepeatFrom.INSTANCE.getString(item);
						}
						
					});
		}
		
		return comboBox;
	}
	
	@SuppressWarnings("unused")
	public static JXComboBox createTaskStatusComboBox(
			ComboBoxModel model,
			boolean autoComplete) {
		JXComboBox comboBox = new JXComboBox();
		
		if (model != null)
			comboBox.setModel(model);
		
		comboBox.setRenderer(new DefaultListRenderer(
				StringValueTaskStatus.INSTANCE));
		
		if (false) {
			AutoCompleteDecorator.decorate(
					comboBox,
					new ObjectToStringConverter() {
						
						@Override
						public String getPreferredStringForItem(Object item) {
							return StringValueTaskStatus.INSTANCE.getString(item);
						}
						
					});
		}
		
		return comboBox;
	}
	
	public static JScrollPane createJScrollPane(
			JComponent component,
			boolean border) {
		JScrollPane scrollPane = new JScrollPane(component);
		
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf())
			IAppWidgetFactory.makeIAppScrollPane(scrollPane);
		
		if (border)
			scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		else
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		return scrollPane;
	}
	
	public static JSplitPane createThinJSplitPane(int orientation) {
		JSplitPane splitPane = new JSplitPane(orientation);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerSize(1);
		((BasicSplitPaneUI) splitPane.getUI()).getDivider().setBorder(
				BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0xa5a5a5)));
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		return splitPane;
	}
	
	public static JMenu createPostponeMenu() {
		final JMenu postponeMenu = new JMenu(
				Translations.getString("action.postpone_tasks"));
		final JMenu postponeStartDateMenu = new JMenu(
				Translations.getString("general.task.start_date"));
		final JMenu postponeDueDateMenu = new JMenu(
				Translations.getString("general.task.due_date"));
		final JMenu postponeBothMenu = new JMenu(
				Translations.getString("action.postpone_tasks.both"));
		
		postponeMenu.setToolTipText(Translations.getString("action.postpone_tasks"));
		postponeMenu.setIcon(ImageUtils.getResourceImage("calendar.png", 16, 16));
		
		postponeStartDateMenu.setToolTipText(Translations.getString("general.task.start_date"));
		postponeStartDateMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeDueDateMenu.setToolTipText(Translations.getString("general.task.due_date"));
		postponeDueDateMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeBothMenu.setToolTipText(Translations.getString("action.postpone_tasks.both"));
		postponeBothMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		ActionPostponeTasks[] actions = null;
		
		actions = ActionPostponeTasks.createDefaultActions(
				16,
				16,
				null,
				PostponeType.START_DATE);
		for (ActionPostponeTasks action : actions) {
			postponeStartDateMenu.add(action);
		}
		
		actions = ActionPostponeTasks.createDefaultActions(
				16,
				16,
				null,
				PostponeType.DUE_DATE);
		for (ActionPostponeTasks action : actions) {
			postponeDueDateMenu.add(action);
		}
		
		actions = ActionPostponeTasks.createDefaultActions(
				16,
				16,
				null,
				PostponeType.BOTH);
		for (ActionPostponeTasks action : actions) {
			postponeBothMenu.add(action);
		}
		
		postponeMenu.add(postponeStartDateMenu);
		postponeMenu.add(postponeDueDateMenu);
		postponeMenu.add(postponeBothMenu);
		
		return postponeMenu;
	}
	
	public static JButton createPostponeButton(
			final int width,
			final int height,
			ActionListener listener) {
		final JButton button = new JButton();
		
		button.setText(Translations.getString("action.postpone_tasks"));
		button.setToolTipText(Translations.getString("action.postpone_tasks"));
		button.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				width,
				height));
		
		final JPopupMenu postponeMenu = new JPopupMenu();
		
		final JMenu postponeStartDateMenu = new JMenu(
				Translations.getString("general.task.start_date"));
		final JMenu postponeDueDateMenu = new JMenu(
				Translations.getString("general.task.due_date"));
		final JMenu postponeBothMenu = new JMenu(
				Translations.getString("action.postpone_tasks.both"));
		
		postponeStartDateMenu.setToolTipText(Translations.getString("general.task.start_date"));
		postponeStartDateMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeDueDateMenu.setToolTipText(Translations.getString("general.task.due_date"));
		postponeDueDateMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		postponeBothMenu.setToolTipText(Translations.getString("action.postpone_tasks.both"));
		postponeBothMenu.setIcon(ImageUtils.getResourceImage(
				"calendar.png",
				16,
				16));
		
		ActionPostponeTaskBeans[] actions = null;
		
		actions = ActionPostponeTaskBeans.createDefaultActions(
				listener,
				PostponeType.START_DATE,
				16,
				16);
		for (ActionPostponeTaskBeans action : actions) {
			postponeStartDateMenu.add(action);
		}
		
		actions = ActionPostponeTaskBeans.createDefaultActions(
				listener,
				PostponeType.DUE_DATE,
				16,
				16);
		for (ActionPostponeTaskBeans action : actions) {
			postponeDueDateMenu.add(action);
		}
		
		actions = ActionPostponeTaskBeans.createDefaultActions(
				listener,
				PostponeType.BOTH,
				16,
				16);
		for (ActionPostponeTaskBeans action : actions) {
			postponeBothMenu.add(action);
		}
		
		postponeMenu.add(postponeStartDateMenu);
		postponeMenu.add(postponeDueDateMenu);
		postponeMenu.add(postponeBothMenu);
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				postponeMenu.show(button, 0, 0);
			}
			
		});
		
		return button;
	}
	
}
