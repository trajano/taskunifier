package com.leclercb.taskunifier.gui.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.leclercb.taskunifier.gui.actions.ActionAddTemplateTask;
import com.leclercb.taskunifier.gui.actions.ActionManageTemplates;
import com.leclercb.taskunifier.gui.api.templates.Template;
import com.leclercb.taskunifier.gui.api.templates.TemplateFactory;
import com.leclercb.taskunifier.gui.commons.comparators.TemplateComparator;

public final class TemplateUtils {
	
	private TemplateUtils() {

	}
	
	public static void updateTemplateList(JMenu menu, JPopupMenu popupMenu) {
		if (menu != null)
			menu.removeAll();
		
		if (popupMenu != null)
			popupMenu.removeAll();
		
		List<Template> templates = new ArrayList<Template>(
				TemplateFactory.getInstance().getList());
		Collections.sort(templates, new TemplateComparator());
		
		for (Template template : templates) {
			if (menu != null)
				menu.add(new ActionAddTemplateTask(template, 16, 16));
			
			if (popupMenu != null)
				popupMenu.add(new ActionAddTemplateTask(template, 16, 16));
		}
		
		if (menu != null) {
			menu.addSeparator();
			menu.add(new ActionManageTemplates(16, 16));
		}
		
		if (popupMenu != null) {
			popupMenu.addSeparator();
			popupMenu.add(new ActionManageTemplates(16, 16));
		}
	}
	
}
