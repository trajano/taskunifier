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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXColorSelectionButton;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public interface ConfigurationFieldType<ComponentType extends JComponent, ValueType> {
	
	public static class Panel implements ConfigurationFieldType<JPanel, Void> {
		
		private JPanel panel;
		
		public Panel(JPanel panel) {
			this.panel = panel;
		}
		
		@Override
		public void initializeFieldComponent() {
			
		}
		
		@Override
		public JPanel getFieldComponent() {
			return this.panel;
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
	
	public static class Separator extends JSeparator implements ConfigurationFieldType<JSeparator, Void> {
		
		public Separator() {
			
		}
		
		@Override
		public void initializeFieldComponent() {
			
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
	
	public static class Label extends JLabel implements ConfigurationFieldType<JLabel, Void> {
		
		public Label(String label) {
			super(label);
			this.setEnabled(false);
		}
		
		@Override
		public void initializeFieldComponent() {
			
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
	
	public static class Button extends JButton implements ConfigurationFieldType<JButton, Void> {
		
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
		public void initializeFieldComponent() {
			
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
	
	public static class RadioButton extends JPanel implements ConfigurationFieldType<JPanel, String> {
		
		private boolean first;
		private ButtonGroup group;
		private String propertyName;
		
		public RadioButton(String propertyName, String[] labels, String[] values) {
			this.first = true;
			this.propertyName = propertyName;
			
			CheckUtils.isNotNull(labels, "Labels cannot be null");
			CheckUtils.isNotNull(values, "Values cannot be null");
			
			if (labels.length != values.length)
				throw new IllegalArgumentException();
			
			this.setLayout(new GridLayout(0, 1));
			
			group = new ButtonGroup();
			
			for (int i = 0; i < labels.length; i++) {
				JRadioButton radioButton = new JRadioButton(labels[i]);
				radioButton.setActionCommand(values[i]);
				
				this.add(radioButton);
				group.add(radioButton);
			}
		}
		
		@Override
		public void initializeFieldComponent() {
			this.setSelectedButton(this.getPropertyValue());
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								RadioButton.this.setSelectedButton(getPropertyValue());
							}
							
						});
			}
		}
		
		@Override
		public JPanel getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return group.getSelection().getActionCommand();
		}
		
		@Override
		public String getPropertyValue() {
			return Main.SETTINGS.getStringProperty(this.propertyName);
		}
		
		@Override
		public void saveAndApplyConfig() {
			Main.SETTINGS.setStringProperty(
					this.propertyName,
					this.getFieldValue());
		}
		
		private void setSelectedButton(String value) {
			List<AbstractButton> buttons = Collections.list(group.getElements());
			for (AbstractButton button : buttons) {
				if (EqualsUtils.equals(button.getActionCommand(), value)) {
					group.setSelected(button.getModel(), true);
					break;
				}
			}
		}
		
	}
	
	public static class CheckBox extends JCheckBox implements ConfigurationFieldType<JCheckBox, Boolean> {
		
		private boolean first;
		private String propertyName;
		
		public CheckBox(String propertyName) {
			this.first = true;
			this.propertyName = propertyName;
		}
		
		@Override
		public void initializeFieldComponent() {
			Boolean selected = getPropertyValue();
			
			if (selected == null)
				selected = false;
			
			this.setSelected(selected);
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								Boolean selected = getPropertyValue();
								
								if (selected == null)
									selected = false;
								
								CheckBox.this.setSelected(selected);
							}
							
						});
			}
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
		public Boolean getPropertyValue() {
			return Main.SETTINGS.getBooleanProperty(propertyName);
		}
		
		@Override
		public void saveAndApplyConfig() {
			Main.SETTINGS.setBooleanProperty(propertyName, this.getFieldValue());
		}
		
	}
	
	public static abstract class Spinner extends JSpinner implements ConfigurationFieldType<JSpinner, Object> {
		
		private boolean first;
		private String propertyName;
		
		public Spinner(String propertyName) {
			this.first = true;
			this.propertyName = propertyName;
		}
		
		@Override
		public void initializeFieldComponent() {
			try {
				this.setValue(Spinner.this.getPropertyValue());
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								try {
									Spinner.this.setValue(Spinner.this.getPropertyValue());
								} catch (Throwable t) {
									t.printStackTrace();
								}
							}
							
						});
			}
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
	
	public static class StarCheckBox extends CheckBox {
		
		public StarCheckBox(String propertyName) {
			super(propertyName);
			
			this.setIcon(ImageUtils.getResourceImage(
					"checkbox_star.png",
					18,
					18));
			this.setSelectedIcon(ImageUtils.getResourceImage(
					"checkbox_star_selected.png",
					18,
					18));
		}
		
	}
	
	public static abstract class ComboBox extends JComboBox implements ConfigurationFieldType<JComboBox, Object> {
		
		private boolean first;
		private String propertyName;
		
		public ComboBox(Object[] items) {
			this(items, null);
		}
		
		public ComboBox(ComboBoxModel model) {
			this(model, null);
		}
		
		public ComboBox(Object[] items, String propertyName) {
			this(new DefaultComboBoxModel(items), propertyName);
		}
		
		public ComboBox(ComboBoxModel model, String propertyName) {
			super(model);
			
			this.first = true;
			this.propertyName = propertyName;
		}
		
		@Override
		public void initializeFieldComponent() {
			this.setSelectedItem(this.getPropertyValue());
			
			if (this.first) {
				this.first = false;
				
				if (this.propertyName != null) {
					Main.SETTINGS.addPropertyChangeListener(
							propertyName,
							new PropertyChangeListener() {
								
								@Override
								public void propertyChange(
										PropertyChangeEvent evt) {
									ComboBox.this.setSelectedItem(ComboBox.this.getPropertyValue());
								}
								
							});
				}
			}
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
	
	public static class TextArea extends JTextArea implements ConfigurationFieldType<JTextArea, String> {
		
		private boolean first;
		private String propertyName;
		
		public TextArea(String propertyName) {
			super(5, 20);
			
			this.first = true;
			this.propertyName = propertyName;
			
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		
		@Override
		public void initializeFieldComponent() {
			this.setText(this.getPropertyValue());
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								TextArea.this.setText(TextArea.this.getPropertyValue());
							}
							
						});
			}
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
		public String getPropertyValue() {
			return Main.SETTINGS.getStringProperty(this.propertyName);
		}
		
		@Override
		public void saveAndApplyConfig() {
			Main.SETTINGS.setStringProperty(
					this.propertyName,
					this.getFieldValue());
		}
		
	}
	
	public static class TextField extends JTextField implements ConfigurationFieldType<JTextField, String> {
		
		private boolean first;
		private String propertyName;
		
		public TextField(String propertyName) {
			this.first = true;
			this.propertyName = propertyName;
		}
		
		@Override
		public void initializeFieldComponent() {
			this.setText(this.getPropertyValue());
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								TextField.this.setText(TextField.this.getPropertyValue());
							}
							
						});
			}
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
		public String getPropertyValue() {
			return Main.SETTINGS.getStringProperty(this.propertyName);
		}
		
		@Override
		public void saveAndApplyConfig() {
			Main.SETTINGS.setStringProperty(
					this.propertyName,
					this.getFieldValue());
		}
		
	}
	
	public static abstract class FormattedTextField extends JFormattedTextField implements ConfigurationFieldType<JFormattedTextField, String> {
		
		private boolean first;
		private String propertyName;
		
		public FormattedTextField(
				AbstractFormatter formatter,
				String propertyName) {
			super(formatter);
			
			this.first = true;
			this.propertyName = propertyName;
		}
		
		@Override
		public void initializeFieldComponent() {
			this.setText(this.getPropertyValue());
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								FormattedTextField.this.setText(FormattedTextField.this.getPropertyValue());
							}
							
						});
			}
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
	
	public static class PasswordField extends JPasswordField implements ConfigurationFieldType<JPasswordField, String> {
		
		private boolean first;
		private String propertyName;
		
		public PasswordField(String propertyName) {
			this.first = true;
			this.propertyName = propertyName;
		}
		
		@Override
		public void initializeFieldComponent() {
			this.setText(this.getPropertyValue());
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								PasswordField.this.setText(PasswordField.this.getPropertyValue());
							}
							
						});
			}
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
		public String getPropertyValue() {
			return Main.SETTINGS.getStringProperty(this.propertyName);
		}
		
		@Override
		public void saveAndApplyConfig() {
			Main.SETTINGS.setStringProperty(
					this.propertyName,
					this.getFieldValue());
		}
		
	}
	
	public static class ColorChooser implements ConfigurationFieldType<JXColorSelectionButton, Color> {
		
		private JXColorSelectionButton component;
		
		private boolean first;
		private String propertyName;
		
		public ColorChooser(String propertyName) {
			this.component = new JXColorSelectionButton();
			this.component.setPreferredSize(new Dimension(24, 24));
			this.component.setBorder(BorderFactory.createEmptyBorder());
			
			this.first = true;
			this.propertyName = propertyName;
			
		}
		
		@Override
		public void initializeFieldComponent() {
			this.component.setBackground(this.getPropertyValue());
			
			if (this.first) {
				this.first = false;
				
				Main.SETTINGS.addPropertyChangeListener(
						propertyName,
						new PropertyChangeListener() {
							
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								ColorChooser.this.component.setBackground(ColorChooser.this.getPropertyValue());
							}
							
						});
			}
		}
		
		@Override
		public JXColorSelectionButton getFieldComponent() {
			return this.component;
		}
		
		@Override
		public Color getFieldValue() {
			return this.component.getBackground();
		}
		
		@Override
		public Color getPropertyValue() {
			return Main.SETTINGS.getColorProperty(this.propertyName);
		}
		
		@Override
		public void saveAndApplyConfig() {
			Main.SETTINGS.setColorProperty(
					this.propertyName,
					this.getFieldValue());
		}
		
	}
	
	public abstract void initializeFieldComponent();
	
	public abstract ComponentType getFieldComponent();
	
	public abstract ValueType getFieldValue();
	
	public abstract ValueType getPropertyValue();
	
	public abstract void saveAndApplyConfig();
	
}
