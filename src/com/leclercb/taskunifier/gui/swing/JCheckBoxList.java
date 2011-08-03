package com.leclercb.taskunifier.gui.swing;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class JCheckBoxList extends JList {
	
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	
	public JCheckBoxList() {
		this.setCellRenderer(new CellRenderer());
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				int index = JCheckBoxList.this.locationToIndex(e.getPoint());
				
				if (index != -1) {
					JCheckBox checkbox = (JCheckBox) JCheckBoxList.this.getModel().getElementAt(
							index);
					checkbox.setSelected(!checkbox.isSelected());
					JCheckBoxList.this.repaint();
				}
			}
			
		});
		
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	protected class CellRenderer implements ListCellRenderer {
		
		@Override
		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;
			checkbox.setBackground(isSelected ? JCheckBoxList.this.getSelectionBackground() : JCheckBoxList.this.getBackground());
			checkbox.setForeground(isSelected ? JCheckBoxList.this.getSelectionForeground() : JCheckBoxList.this.getForeground());
			checkbox.setEnabled(JCheckBoxList.this.isEnabled());
			checkbox.setFont(JCheckBoxList.this.getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);
			checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
			return checkbox;
		}
		
	}
	
}
