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
package com.leclercb.taskunifier.gui.components.configuration.api;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.leclercb.commons.api.properties.PropertiesConfiguration;
import com.leclercb.taskunifier.gui.utils.Images;

public interface ConfigurationFieldTypeExt<ComponentType extends JComponent, ValueType> extends ConfigurationFieldType<ComponentType, ValueType> {
	
	public static class Panel extends JPanel implements ConfigurationFieldTypeExt<JPanel, Void> {
		
		public Panel(JPanel panel) {
			this.setLayout(new BorderLayout());
			this.add(panel, BorderLayout.CENTER);
		}
		
		@Override
		public JPanel getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
		@Override
		public Void getPropertyValue() {
			return null;
		}
		
		@Override
		public void saveAndApplyConfig() {

		}
		
	}
	
	public static class Separator extends JSeparator implements ConfigurationFieldTypeExt<JSeparator, Void> {
		
		public Separator() {

		}
		
		@Override
		public JSeparator getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
		@Override
		public Void getPropertyValue() {
			return null;
		}
		
		@Override
		public void saveAndApplyConfig() {

		}
		
	}
	
	public static class Label extends JLabel implements ConfigurationFieldTypeExt<JLabel, Void> {
		
		public Label(String label) {
			super(label);
			this.setEnabled(false);
		}
		
		@Override
		public JLabel getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
		@Override
		public Void getPropertyValue() {
			return null;
		}
		
		@Override
		public void saveAndApplyConfig() {

		}
		
	}
	
	public static class Button extends JButton implements ConfigurationFieldTypeExt<JButton, Void> {
		
		public Button(Action action) {
			super(action);
		}
		
		public Button(String label, ActionListener listener) {
			super(label);
			this.addActionListener(listener);
		}
		
		public Button(String label, Icon icon, ActionListener listener) {
			super(label, icon);
			this.addActionListener(listener);
		}
		
		@Override
		public JButton getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
		@Override
		public Void getPropertyValue() {
			return null;
		}
		
		@Override
		public void saveAndApplyConfig() {

		}
		
	}
	
	public static abstract class CheckBox extends JCheckBox implements ConfigurationFieldTypeExt<JCheckBox, Boolean> {
		
		public CheckBox(PropertiesConfiguration properties, String propertyName) {
			this.setSelected(this.getPropertyValue());
			
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							CheckBox.this.setSelected(CheckBox.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JCheckBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Boolean getFieldValue() {
			return this.isSelected();
		}
		
		@Override
		public abstract Boolean getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public static abstract class Spinner extends JSpinner implements ConfigurationFieldTypeExt<JSpinner, Object> {
		
		public Spinner(PropertiesConfiguration properties, String propertyName) {
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							Spinner.this.setValue(Spinner.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JSpinner getFieldComponent() {
			return this;
		}
		
		@Override
		public Object getFieldValue() {
			return this.getValue();
		}
		
		@Override
		public abstract Object getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public static abstract class StarCheckBox extends CheckBox {
		
		public StarCheckBox(
				PropertiesConfiguration properties,
				String propertyName) {
			super(properties, propertyName);
			
			this.setIcon(Images.getResourceImage("checkbox_star.png", 18, 18));
			this.setSelectedIcon(Images.getResourceImage(
					"checkbox_star_selected.png",
					18,
					18));
		}
		
	}
	
	public static abstract class ComboBox extends JComboBox implements ConfigurationFieldTypeExt<JComboBox, Object> {
		
		public ComboBox(
				Object[] items,
				PropertiesConfiguration properties,
				String propertyName) {
			this(new DefaultComboBoxModel(items), properties, propertyName);
		}
		
		public ComboBox(
				ComboBoxModel model,
				PropertiesConfiguration properties,
				String propertyName) {
			super(model);
			this.setSelectedItem(this.getPropertyValue());
			
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							ComboBox.this.setSelectedItem(ComboBox.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JComboBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Object getFieldValue() {
			return this.getSelectedItem();
		}
		
		@Override
		public abstract Object getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public static abstract class TextArea extends JTextArea implements ConfigurationFieldTypeExt<JTextArea, String> {
		
		public TextArea(PropertiesConfiguration properties, String propertyName) {
			super(5, 20);
			this.setText(this.getPropertyValue());
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							TextArea.this.setText(TextArea.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JTextArea getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
		@Override
		public abstract String getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public static abstract class TextField extends JTextField implements ConfigurationFieldTypeExt<JTextField, String> {
		
		public TextField(PropertiesConfiguration properties, String propertyName) {
			this.setText(this.getPropertyValue());
			
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							TextField.this.setText(TextField.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JTextField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
		@Override
		public abstract String getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public static abstract class FormattedTextField extends JFormattedTextField implements ConfigurationFieldTypeExt<JFormattedTextField, String> {
		
		public FormattedTextField(
				AbstractFormatter formatter,
				PropertiesConfiguration properties,
				String propertyName) {
			super(formatter);
			this.setText(this.getPropertyValue());
			
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							FormattedTextField.this.setText(FormattedTextField.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JFormattedTextField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
		@Override
		public abstract String getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public static abstract class PasswordField extends JPasswordField implements ConfigurationFieldTypeExt<JPasswordField, String> {
		
		public PasswordField(
				PropertiesConfiguration properties,
				String propertyName) {
			this.setText(this.getPropertyValue());
			
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							PasswordField.this.setText(PasswordField.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JPasswordField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return new String(this.getPassword());
		}
		
		@Override
		public abstract String getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public static abstract class ColorChooser extends JLabel implements ConfigurationFieldTypeExt<JLabel, Color> {
		
		private JColorChooser colorChooser;
		
		public ColorChooser(
				PropertiesConfiguration properties,
				String propertyName) {
			this.setOpaque(true);
			this.setBackground(this.getPropertyValue());
			this.setBorder(new LineBorder(Color.BLACK));
			
			colorChooser = new JColorChooser();
			colorChooser.setColor(this.getPropertyValue());
			
			final JDialog colorDialog = JColorChooser.createDialog(
					this,
					"Color",
					true,
					colorChooser,
					new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent event) {
							ColorChooser.this.setBackground(colorChooser.getColor());
						}
						
					},
					null);
			
			this.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					colorDialog.setVisible(true);
				}
				
			});
			
			properties.addPropertyChangeListener(
					propertyName,
					new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							ColorChooser.this.setBackground(ColorChooser.this.getPropertyValue());
							ColorChooser.this.colorChooser.setColor(ColorChooser.this.getPropertyValue());
						}
						
					});
		}
		
		@Override
		public JLabel getFieldComponent() {
			return this;
		}
		
		@Override
		public Color getFieldValue() {
			return this.colorChooser.getColor();
		}
		
		@Override
		public abstract Color getPropertyValue();
		
		@Override
		public abstract void saveAndApplyConfig();
		
	}
	
	public abstract ValueType getPropertyValue();
	
	public abstract void saveAndApplyConfig();
	
}
