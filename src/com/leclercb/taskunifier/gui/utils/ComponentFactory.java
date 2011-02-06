package com.leclercb.taskunifier.gui.utils;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.explodingpixels.macwidgets.IAppWidgetFactory;
import com.leclercb.commons.api.utils.OsUtils;
import com.leclercb.commons.gui.swing.lookandfeel.LookAndFeelUtils;
import com.leclercb.taskunifier.gui.images.Images;

public final class ComponentFactory {
	
	private ComponentFactory() {

	}
	
	public static JPanel createSearchField(JTextField textField) {
		if (OsUtils.isMacOSX() && LookAndFeelUtils.isCurrentLafSystemLaf()) {
			textField.putClientProperty("JTextField.variant", "search");
			
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(textField, BorderLayout.CENTER);
			
			return panel;
		} else {
			JPanel panel = new JPanel(new BorderLayout(5, 0));
			panel.add(
					new JLabel(Images.getResourceImage("search.png", 16, 16)),
					BorderLayout.WEST);
			panel.add(textField, BorderLayout.CENTER);
			
			return panel;
		}
	}
	
	public static JScrollPane createJScrollPane(JComponent component) {
		JScrollPane scrollPane = new JScrollPane(component);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		
		if (OsUtils.isMacOSX() && LookAndFeelUtils.isCurrentLafSystemLaf())
			IAppWidgetFactory.makeIAppScrollPane(scrollPane);
		
		return scrollPane;
	}
	
}
