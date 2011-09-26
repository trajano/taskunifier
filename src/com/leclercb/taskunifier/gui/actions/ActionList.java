package com.leclercb.taskunifier.gui.actions;

import javax.swing.Action;

import com.leclercb.taskunifier.gui.translations.Translations;

public enum ActionList {
	
	ABOUT(Translations.getString("action.about"), "information.png", true),
	ADD_NOTE(Translations.getString("action.add_note"), "note.png", true),
	ADD_QUICK_TASK(Translations.getString("action.add_task"), "task.png", false),
	ADD_SUBTASK(Translations.getString("action.add_subtask"), "subtask.png", true),
	ADD_TASK(Translations.getString("action.add_task"), "task.png", true),
	ADD_TASK_SEARCHER(Translations.getString("action.add_task_searcher"), "add.png", true),
	ADD_TASK_SEARCHER_SELECTED_TASKS(Translations.getString("action.add_task_searcher_selected_tasks"), "add.png", true),
	ADD_TEMPLATE_TASK(Translations.getString("action.add_template_task"), "template.png", false),
	ADD_TEMPLATE_TASK_MENU(Translations.getString("action.add_template_task"), "template.png", true),
	BATCH_ADD_TASKS(Translations.getString("action.batch_add_tasks"), "batch.png", true),
	CHANGE_DATE_FOLDER_LOCATION(Translations.getString("action.change_data_folder_location"), null, false),
	CHANGE_VIEW(Translations.getString("action.change_view"), "change_view_task.png", true),
	CHANGE_VIEW_CALENDAR(Translations.getString("action.change_view_calendar"), "change_view_calendar.png", true),
	CHECK_PLUGIN_VERSION(Translations.getString("action.check_plugin_version"), "download.png", true),
	CHECK_VERSION(Translations.getString("action.check_version"), "download.png", true),
	COLLAPSE_ALL(Translations.getString("action.collapse_all"), "tree_expanded.png", true),
	COMPLETED_TASKS(Translations.getString("action.complete_tasks"), "check.png", true),
	CONFIGURATION(Translations.getString(""), true),
	COPY(Translations.getString(""), false),
	CREATE_ACCOUNT(Translations.getString(""), false),
	CREATE_NOTE_FROM_CLIPBOARD(Translations.getString(""), true),
	CREATE_TASK_FROM_CLIPBOARD(Translations.getString(""), true),
	CUT(Translations.getString(""), false),
	DELETE(Translations.getString(""), true),
	DELETE_TASK_SEARCHER(Translations.getString(""), true),
	DONATE(Translations.getString(""), true),
	DUPLICATE_NOTES(Translations.getString(""), true),
	DUPLICATE_TASKS(Translations.getString(""), true),
	EDIT_TASKS(Translations.getString(""), true),
	EDIT_TASK_SEARCHER(Translations.getString(""), true),
	EXPAND_ALL(Translations.getString(""), true),
	EXPORT_MODELS(Translations.getString(""), true),
	EXPORT_SETTINGS(Translations.getString(""), true),
	EXPORT_TASK_SEARCHERS(Translations.getString(""), true),
	EXPORT_TASK_TEMPLATES(Translations.getString(""), true),
	GET_SERIAL(Translations.getString(""), false),
	HELP(Translations.getString(""), true),
	IMPORT_MODELS(Translations.getString(""), true),
	IMPORT_SETTINGS(Translations.getString(""), true),
	IMPORT_TASK_SEARCHERS(Translations.getString(""), true),
	IMPORT_TASK_TEMPLATES(Translations.getString(""), true),
	LOG_BUG(Translations.getString(""), true),
	LOG_FEATURE_REQUEST(Translations.getString(""), true),
	MANAGE_MODELS(Translations.getString(""), true),
	MANAGE_PLUGINS(Translations.getString(""), true),
	MANAGE_TASK_TEMPLATES(Translations.getString(""), true),
	PASTE(Translations.getString(""), false),
	POSTPONE_TASK_BEANS(Translations.getString(""), false),
	POSTPONE_TASKS(Translations.getString(""), false),
	POSTPONE_TASKS_MENU(Translations.getString(""), true),
	PRINT(Translations.getString(""), true),
	PRINT_SELECTED_MODELS(Translations.getString(""), true),
	QUIT(Translations.getString(""), true),
	REDO(Translations.getString(""), false),
	RESET_GENERAL_SEARCHERS(Translations.getString(""), false),
	REVIEW(Translations.getString(""), true),
	SCHEDULED_SYNC(Translations.getString(""), true),
	SELECT_PARENT_TASKS(Translations.getString(""), true),
	SHOW_TIPS(Translations.getString(""), true),
	SYNCHRONIZE(Translations.getString(""), true),
	TASK_REMINDERS(Translations.getString(""), true),
	UNDO(Translations.getString(""), false);
	
	private ActionList(String title, String icon, boolean canInstantiate) {
		
	}
	
	public Action newInstance() {
		
	}
	
	public Action newInstance(int width, int height) {
		
	}
	
}
