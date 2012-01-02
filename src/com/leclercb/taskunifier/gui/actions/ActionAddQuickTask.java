package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.DateUtils;
import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.api.models.templates.TaskTemplate;
import com.leclercb.taskunifier.api.models.templates.TaskTemplateFactory;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionAddQuickTask extends AbstractAction {
	
	public ActionAddQuickTask() {
		this(32, 32);
	}
	
	public ActionAddQuickTask(int width, int height) {
		super(
				Translations.getString("action.add_task"),
				ImageUtils.getResourceImage("task.png", width, height));
		
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
		return addQuickTask(
				TaskTemplateFactory.getInstance().getDefaultTemplate(), 
				task, 
				edit);
	}
	
	public static Task addQuickTask(TaskTemplate template, String task, boolean edit) {
		CheckUtils.isNotNull(task);
		
		if (template == null)
			template = new TaskTemplate();
		else
			template = template.clone();
		
		task = task.trim();
		
		Pattern pattern = Pattern.compile("[^&@*<>]+");
		Matcher matcher = pattern.matcher(task);
		
		if (!matcher.find())
			return null;
		
		String title = matcher.group();
		
		template.setTitle(title.trim());
		
		char lastChar = title.charAt(title.length() - 1);
		
		pattern = Pattern.compile("[&@*<>][^&@*<>]+");
		matcher = pattern.matcher(task);
		
		while (matcher.find()) {
			String s = matcher.group();
			
			if (lastChar != ' ') {
				lastChar = s.charAt(s.length() - 1);
				template.setTitle(template.getTitle() + s.trim());
				continue;
			}
			
			lastChar = s.charAt(s.length() - 1);
			s = s.trim();
			
			char c = s.charAt(0);
			s = s.substring(1).trim();
			
			if (c == '&') { // Tag
				if (template.getTaskTags() != null)
					template.setTaskTags(template.getTaskTags()
							+ ", "
							+ s);
				else
					template.setTaskTags(s);
			} else if (c == '@') { // Context, Folder, Goal, Location
				findModel(s, template);
			} else if (c == '*') { // Priority, Status
				findStatusPriority(s, template);
			} else if (c == '>') { // Start Date
				findDate(s, true, template);
			} else if (c == '<') { // Due Date
				findDate(s, false, template);
			}
		}
		
		return ActionAddTask.addTask(template, title, edit);
	}
	
	private static void findModel(String title, TaskTemplate template) {
		Model model = null;
		
		if (template.getTaskContext() == null) {
			List<Context> contexts = ContextFactory.getInstance().getList();
			for (Context context : contexts) {
				if (context.getTitle().equalsIgnoreCase(title)) {
					template.setTaskContext(context);
					return;
				}
				
				if (model == null
						&& context.getTitle().toLowerCase().startsWith(
								title.toLowerCase())) {
					model = context;
					break;
				}
			}
		}
		
		if (template.getTaskFolder() == null) {
			List<Folder> folders = FolderFactory.getInstance().getList();
			for (Folder folder : folders) {
				if (folder.getTitle().equalsIgnoreCase(title)) {
					template.setTaskFolder(folder);
					return;
				}
				
				if (model == null
						&& folder.getTitle().toLowerCase().startsWith(
								title.toLowerCase())) {
					model = folder;
					break;
				}
			}
		}
		
		if (template.getTaskGoal() == null) {
			List<Goal> goals = GoalFactory.getInstance().getList();
			for (Goal goal : goals) {
				if (goal.getTitle().equalsIgnoreCase(title)) {
					template.setTaskGoal(goal);
					return;
				}
				
				if (model == null
						&& goal.getTitle().toLowerCase().startsWith(
								title.toLowerCase())) {
					model = goal;
					break;
				}
			}
		}
		
		if (template.getTaskLocation() == null) {
			List<Location> locations = LocationFactory.getInstance().getList();
			for (Location location : locations) {
				if (location.getTitle().equalsIgnoreCase(title)) {
					template.setTaskLocation(location);
					return;
				}
				
				if (model == null
						&& location.getTitle().toLowerCase().startsWith(
								title.toLowerCase())) {
					model = location;
					break;
				}
			}
		}
		
		if (model instanceof Context)
			template.setTaskContext((Context) model);
		else if (model instanceof Folder)
			template.setTaskFolder((Folder) model);
		else if (model instanceof Goal)
			template.setTaskGoal((Goal) model);
		else if (model instanceof Location)
			template.setTaskLocation((Location) model);
	}
	
	private static void findStatusPriority(
			String title,
			TaskTemplate template) {
		for (TaskStatus status : TaskStatus.values()) {
			String s = TranslationsUtils.translateTaskStatus(status);
			
			if (s.toLowerCase().startsWith(title.toLowerCase())) {
				template.setTaskStatus(status);
				return;
			}
		}
		
		for (TaskPriority priority : TaskPriority.values()) {
			String p = TranslationsUtils.translateTaskPriority(priority);
			
			if (p.toLowerCase().startsWith(title.toLowerCase())) {
				template.setTaskPriority(priority);
				return;
			}
		}
	}
	
	private static void findDate(
			String title,
			boolean startDate,
			TaskTemplate template) {
		String dateFormat = Main.getSettings().getStringProperty(
				"date.date_format");
		String timeFormat = Main.getSettings().getStringProperty(
				"date.time_format");
		dateFormat = dateFormat.replace("yyyy", "yy");
		
		Pattern pattern = Pattern.compile("([+-]?)([0-9]*)(d|w|m|y|tt|t)(.*)");
		Matcher matcher = pattern.matcher(title);
		
		if (matcher.find()) {
			int num = 0;
			String type = "d";
			
			try {
				num = Integer.parseInt(matcher.group(2));
			} catch (NumberFormatException e) {
				
			}
			
			type = matcher.group(3);
			
			if (matcher.group(1).equals("-"))
				num *= -1;
			
			Calendar date = Calendar.getInstance();
			if ("d".equals(type))
				date.add(Calendar.DAY_OF_MONTH, num);
			else if ("w".equals(type))
				date.add(Calendar.WEEK_OF_YEAR, num);
			else if ("m".equals(type))
				date.add(Calendar.MONTH, num);
			else if ("y".equals(type))
				date.add(Calendar.YEAR, num);
			else if ("t".equals(type))
				date.add(Calendar.DAY_OF_MONTH, 0);
			else if ("tt".equals(type))
				date.add(Calendar.DAY_OF_MONTH, 1);
			
			title = matcher.group(4).trim();
			
			try {
				SimpleDateFormat format = new SimpleDateFormat(timeFormat);
				Calendar time = Calendar.getInstance();
				time.setTime(format.parse(title));
				
				date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
				date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
			} catch (ParseException e) {
				
			}
			
			double diffDays = DateUtils.getDiffInDays(
					Calendar.getInstance(),
					date,
					false);
			
			if (startDate)
				template.setTaskStartDate((int) diffDays);
			else
				template.setTaskDueDate((int) diffDays);
			
			return;
		}
		
		SimpleDateFormat[] formats = {
				new SimpleDateFormat(dateFormat + " " + timeFormat),
				new SimpleDateFormat(dateFormat) };
		
		for (SimpleDateFormat format : formats) {
			try {
				Calendar date = Calendar.getInstance();
				date.setTime(format.parse(title));
				
				double diffDays = DateUtils.getDiffInDays(
						Calendar.getInstance(),
						date,
						false);
				
				int minutes = (date.get(Calendar.HOUR_OF_DAY) * 60)
						+ date.get(Calendar.MINUTE);
				
				if (startDate) {
					template.setTaskStartDate((int) diffDays);
					template.setTaskStartTime(minutes);
				} else {
					template.setTaskDueDate((int) diffDays);
					template.setTaskDueTime(minutes);
				}
				
				return;
			} catch (ParseException e) {
				
			}
		}
	}
	
}
