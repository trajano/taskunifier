package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;

import com.leclercb.taskunifier.api.models.Context;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.Folder;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.Goal;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.Location;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.beans.TaskBean;
import com.leclercb.taskunifier.api.models.enums.TaskPriority;
import com.leclercb.taskunifier.api.models.enums.TaskStatus;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.translations.TranslationsUtils;
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
		
		task = task.trim();
		
		Pattern pattern = Pattern.compile("[^&@*<>]+");
		Matcher matcher = pattern.matcher(task);
		
		if (!matcher.find())
			return null;
		
		taskBean.setTitle(matcher.group().trim());
		
		pattern = Pattern.compile("[&@*<>][^&@*<>]+");
		matcher = pattern.matcher(task);
		
		while (matcher.find()) {
			String s = matcher.group().trim();
			char c = s.charAt(0);
			s = s.substring(1).trim();
			
			if (c == '&') { // Tag
				taskBean.getTags().addTags(TagList.fromString(s));
			} else if (c == '@') { // Context, Folder, Goal, Location
				findModel(s, taskBean);
			} else if (c == '*') { // Priority, Status
				findStatusPriority(s, taskBean);
			} else if (c == '>') { // Start Date
				findDate(s, true, taskBean);
			} else if (c == '<') { // Due Date
				findDate(s, false, taskBean);
			}
		}
		
		return ActionAddTask.addTask(taskBean, edit);
	}
	
	private static void findModel(String title, TaskBean taskBean) {
		Model model = null;
		
		if (taskBean.getContext() == null) {
			List<Context> contexts = ContextFactory.getInstance().getList();
			for (Context context : contexts) {
				if (context.getTitle().equalsIgnoreCase(title)) {
					taskBean.setContext(context.getModelId());
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
		
		if (taskBean.getFolder() == null) {
			List<Folder> folders = FolderFactory.getInstance().getList();
			for (Folder folder : folders) {
				if (folder.getTitle().equalsIgnoreCase(title)) {
					taskBean.setFolder(folder.getModelId());
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
		
		if (taskBean.getGoal() == null) {
			List<Goal> goals = GoalFactory.getInstance().getList();
			for (Goal goal : goals) {
				if (goal.getTitle().equalsIgnoreCase(title)) {
					taskBean.setGoal(goal.getModelId());
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
		
		if (taskBean.getLocation() == null) {
			List<Location> locations = LocationFactory.getInstance().getList();
			for (Location location : locations) {
				if (location.getTitle().equalsIgnoreCase(title)) {
					taskBean.setLocation(location.getModelId());
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
			taskBean.setContext(((Context) model).getModelId());
		else if (model instanceof Folder)
			taskBean.setFolder(((Folder) model).getModelId());
		else if (model instanceof Goal)
			taskBean.setGoal(((Goal) model).getModelId());
		else if (model instanceof Location)
			taskBean.setLocation(((Location) model).getModelId());
	}
	
	private static void findStatusPriority(String title, TaskBean taskBean) {
		for (TaskStatus status : TaskStatus.values()) {
			String s = TranslationsUtils.translateTaskStatus(status);
			
			if (s.toLowerCase().startsWith(title.toLowerCase())) {
				taskBean.setStatus(status);
				return;
			}
		}
		
		for (TaskPriority priority : TaskPriority.values()) {
			String p = TranslationsUtils.translateTaskPriority(priority);
			
			if (p.toLowerCase().startsWith(title.toLowerCase())) {
				taskBean.setPriority(priority);
				return;
			}
		}
	}
	
	private static void findDate(
			String title,
			boolean startDate,
			TaskBean taskBean) {
		String dateFormat = Main.SETTINGS.getStringProperty("date.date_format");
		String timeFormat = Main.SETTINGS.getStringProperty("date.time_format");
		
		Pattern pattern = Pattern.compile("([+-]?)([0-9]+)([dwmy])(.*)");
		Matcher matcher = pattern.matcher(title);
		
		if (matcher.find()) {
			int num = 0;
			char type = 'd';
			
			num = Integer.parseInt(matcher.group(2));
			type = matcher.group(3).charAt(0);
			
			if (matcher.group(1).equals("-"))
				num *= -1;
			
			Calendar date = Calendar.getInstance();
			switch (type) {
				case 'd':
					date.add(Calendar.DAY_OF_MONTH, num);
					break;
				case 'w':
					date.add(Calendar.WEEK_OF_YEAR, num);
					break;
				case 'm':
					date.add(Calendar.MONTH, num);
					break;
				case 'y':
					date.add(Calendar.YEAR, num);
					break;
			}
			
			title = matcher.group(4).trim();
			
			try {
				SimpleDateFormat format = new SimpleDateFormat(timeFormat);
				Calendar time = Calendar.getInstance();
				time.setTime(format.parse(title));
				
				date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
				date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
			} catch (ParseException e) {
				
			}
			
			if (startDate)
				taskBean.setStartDate(date);
			else
				taskBean.setDueDate(date);
			
			return;
		}
		
		SimpleDateFormat[] formats = {
				new SimpleDateFormat(dateFormat + timeFormat),
				new SimpleDateFormat(dateFormat) };
		
		for (SimpleDateFormat format : formats) {
			try {
				Calendar date = Calendar.getInstance();
				date.setTime(format.parse(title));
				
				if (startDate)
					taskBean.setStartDate(date);
				else
					taskBean.setDueDate(date);
				
				return;
			} catch (ParseException e) {
				
			}
		}
	}
	
}
