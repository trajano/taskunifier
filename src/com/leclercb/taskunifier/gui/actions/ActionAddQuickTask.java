package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.Images;

public class ActionAddQuickTask extends AbstractAction {
	
	public ActionAddQuickTask() {
		this(32, 32);
	}
	
	public ActionAddQuickTask(int width, int height) {
		super(
				Translations.getString("action.add_task"),
				Images.getResourceImage("task.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_task"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String task = e.getActionCommand();
		ActionAddQuickTask.addQuickTask(task, true);
	}
	
	public static Task addQuickTask(String task, boolean edit) {
		TaskBean taskBean = new TaskBean();
		
		Pattern pattern = Pattern.compile("[^&@*]+");
		Matcher matcher = pattern.matcher(task);
		
		if (!matcher.find())
			return null;
		
		taskBean.setTitle(matcher.group().trim());
		
		pattern = Pattern.compile("[&@*][^&@*]+");
		matcher = pattern.matcher(task);
		
		while (matcher.find()) {
			String s = matcher.group().trim();
			char c = s.charAt(0);
			s = s.substring(1);
			
			if (c == '&') { // Tag
				taskBean.getTags().addTag(s);
			} else if (c == '@') { // Context, Folder, Goal, Location
			
			} else if (c == '*') { // Priority, Status
			
			}
		}
		
		return ActionAddTask.addTask(taskBean, edit);
	}
	
	public static void main(String[] args) {
		addQuickTask(
				"task title &tag1 &tag2 @Work @Accenture @Goal @Bruxelles *basse *active",
				false);
	}
	
}
