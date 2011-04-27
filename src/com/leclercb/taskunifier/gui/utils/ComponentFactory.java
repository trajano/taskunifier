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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.jgoodies.common.base.SystemUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.gui.commons.renderers.ModelListCellRenderer;

public final class ComponentFactory {

	private ComponentFactory() {

	}

	public static void createRepeatComboBox(JComboBox repeatComboBox) {
		repeatComboBox.setEditable(true);

		final JTextField repeatTextField = (JTextField) repeatComboBox.getEditor().getEditorComponent();
		repeatTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent arg0) {
						this.update();
					}

					@Override
					public void insertUpdate(DocumentEvent arg0) {
						this.update();
					}

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						this.update();
					}

					private void update() {
						if (SynchronizerUtils.getPlugin().getSynchronizerApi().isValidRepeatValue(
								repeatTextField.getText()))
							repeatTextField.setForeground(Color.BLACK);
						else
							repeatTextField.setForeground(Color.RED);
					}

				});
	}

	public static JComboBox createModelComboBox(ComboBoxModel model) {
		JComboBox comboBox = new JComboBox();

		if (model != null)
			comboBox.setModel(model);

		comboBox.setRenderer(new ModelListCellRenderer());

		if (!SystemUtils.IS_OS_MAC || !LookAndFeelUtils.isCurrentLafSystemLaf()) {
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

	public static JPanel createSearchField(JTextField textField) {
		if (SystemUtils.IS_OS_MAC && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			textField.putClientProperty("JTextField.variant", "search");

			JPanel panel = new JPanel(new BorderLayout());
			panel.setOpaque(false);
			panel.add(textField, BorderLayout.CENTER);

			return panel;
		} else {
			JPanel panel = new JPanel(new BorderLayout(5, 0));
			panel.setOpaque(true);
			panel.setBackground(new Color(0xd6dde5));
			panel.add(
					new JLabel(Images.getResourceImage("search.png", 16, 16)),
					BorderLayout.WEST);
			panel.add(textField, BorderLayout.CENTER);
			panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			return panel;
		}
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

	public static JSplitPane createThinJScrollPane(int orientation) {
		JSplitPane splitPane = new JSplitPane(orientation);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerSize(1);
		((BasicSplitPaneUI) splitPane.getUI()).getDivider().setBorder(
				BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(0xa5a5a5)));
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		return splitPane;
	}

}
