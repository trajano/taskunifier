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
package com.leclercb.taskunifier.gui.components.configuration.api;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
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

import com.leclercb.taskunifier.gui.utils.Images;

public interface ConfigurationFieldType<ComponentType extends JComponent, ValueType> {
	
	public static class Panel extends JPanel implements ConfigurationFieldType<JPanel, Void> {
		
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
		
	}
	
	public static class Separator extends JSeparator implements ConfigurationFieldType<JSeparator, Void> {
		
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
		
	}
	
	public static class Label extends JLabel implements ConfigurationFieldType<JLabel, Void> {
		
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
		public JButton getFieldComponent() {
			return this;
		}
		
		@Override
		public Void getFieldValue() {
			return null;
		}
		
	}
	
	public static class CheckBox extends JCheckBox implements ConfigurationFieldType<JCheckBox, Boolean> {
		
		public CheckBox(Boolean selected) {
			this.setSelected(selected);
		}
		
		@Override
		public JCheckBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Boolean getFieldValue() {
			return this.isSelected();
		}
		
	}
	
	public static class Spinner extends JSpinner implements ConfigurationFieldType<JSpinner, Object> {
		
		public Spinner() {

		}
		
		@Override
		public JSpinner getFieldComponent() {
			return this;
		}
		
		@Override
		public Object getFieldValue() {
			return this.getValue();
		}
		
	}
	
	public static class StarCheckBox extends JCheckBox implements ConfigurationFieldType<JCheckBox, Boolean> {
		
		public StarCheckBox(Boolean selected) {
			this.setIcon(Images.getResourceImage("checkbox_star.png", 18, 18));
			this.setSelectedIcon(Images.getResourceImage(
					"checkbox_star_selected.png",
					18,
					18));
			
			this.setSelected(selected);
		}
		
		@Override
		public JCheckBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Boolean getFieldValue() {
			return this.isSelected();
		}
		
	}
	
	public static class ComboBox extends JComboBox implements ConfigurationFieldType<JComboBox, Object> {
		
		public ComboBox(ComboBoxModel model, Object selectedItem) {
			super(model);
			this.setSelectedItem(selectedItem);
		}
		
		public ComboBox(Object[] items, Object selectedItem) {
			super(items);
			this.setSelectedItem(selectedItem);
		}
		
		@Override
		public JComboBox getFieldComponent() {
			return this;
		}
		
		@Override
		public Object getFieldValue() {
			return this.getSelectedItem();
		}
		
	}
	
	public static class TextArea extends JTextArea implements ConfigurationFieldType<JTextArea, String> {
		
		public TextArea(String text) {
			super(text, 5, 20);
			this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		}
		
		@Override
		public JTextArea getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
	}
	
	public static class TextField extends JTextField implements ConfigurationFieldType<JTextField, String> {
		
		public TextField(String text) {
			super(text);
		}
		
		@Override
		public JTextField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
	}
	
	public static class FormattedTextField extends JFormattedTextField implements ConfigurationFieldType<JFormattedTextField, String> {
		
		public FormattedTextField(AbstractFormatter formatter, String text) {
			super(formatter);
			this.setValue(text);
		}
		
		@Override
		public JFormattedTextField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return this.getText();
		}
		
	}
	
	public static class PasswordField extends JPasswordField implements ConfigurationFieldType<JPasswordField, String> {
		
		public PasswordField(String password) {
			super(password);
		}
		
		@Override
		public JPasswordField getFieldComponent() {
			return this;
		}
		
		@Override
		public String getFieldValue() {
			return new String(this.getPassword());
		}
		
	}
	
	public static class ColorChooser extends JLabel implements ConfigurationFieldType<JLabel, Color> {
		
		JColorChooser colorChooser;
		
		public ColorChooser(Color color) {
			this.setOpaque(true);
			this.setBackground(color);
			this.setBorder(new LineBorder(Color.BLACK));
			
			colorChooser = new JColorChooser();
			colorChooser.setColor(color);
			
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
		}
		
		@Override
		public JLabel getFieldComponent() {
			return this;
		}
		
		@Override
		public Color getFieldValue() {
			return this.colorChooser.getColor();
		}
		
	}
	
	public abstract ComponentType getFieldComponent();
	
	public abstract ValueType getFieldValue();
	
}
