package com.leclercb.taskunifier.gui.actions;

import javax.swing.Action;
import javax.swing.Icon;

import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public enum ActionList {
	
	OBJECT("", null, false),
	ABOUT(Translations.getString("action.about"), "information.png", true),
	ADD_NOTE(Translations.getString("action.add_note"), "note.png", true),
	ADD_NOTE_SEARCHER(Translations.getString("action.add_note_searcher"), "add.png", true),
	ADD_QUICK_TASK(Translations.getString("action.add_task"), "task.png", false),
	ADD_SUBTASK(Translations.getString("action.add_subtask"), "subtask.png", true),
	ADD_SUBTASK_AT_SAME_LEVEL(Translations.getString("action.add_subtask_at_same_level"), "subtask.png", true),
	ADD_TASK(Translations.getString("action.add_task"), "task.png", true),
	ADD_TASK_SEARCHER(Translations.getString("action.add_task_searcher"), "add.png", true),
	ADD_TASK_SEARCHER_SELECTED_TASKS(Translations.getString("action.add_task_searcher_selected_tasks"), "add.png", true),
	ADD_TEMPLATE_TASK(Translations.getString("action.add_template_task"), "template.png", false),
	ADD_TEMPLATE_TASK_MENU(Translations.getString("action.add_template_task"), "template.png", true),
	BATCH_ADD_TASKS(Translations.getString("action.batch_add_tasks"), "batch.png", true),
	CHANGE_DATE_FOLDER_LOCATION(Translations.getString("action.change_data_folder_location"), null, false),
	CHANGE_VIEW(Translations.getString("action.change_view"), "change_view_tasks.png", true),
	CHANGE_VIEW_CALENDAR(Translations.getString("action.change_view_calendar"), "change_view_calendar.png", true),
	CHANGE_VIEW_NOTES(Translations.getString("action.change_view_notes"), "change_view_notes.png", true),
	CHANGE_VIEW_TASKS(Translations.getString("action.change_view_tasks"), "change_view_tasks.png", true),
	CHECK_PLUGIN_VERSION(Translations.getString("action.check_plugin_version"), "download.png", true),
	CHECK_VERSION(Translations.getString("action.check_version"), "download.png", true),
	COLLAPSE_ALL(Translations.getString("action.collapse_all"), "tree_expanded.png", true),
	COMPLETE_TASKS(Translations.getString("action.complete_tasks"), "check.png", true),
	CONFIGURATION(Translations.getString("action.configuration"), "settings.png", true),
	COPY(Translations.getString("action.copy"), "copy.png", false),
	CREATE_ACCOUNT(Translations.getString("action.create_account"), "user.png", false),
	CREATE_NEW_BACKUP(Translations.getString("action.create_new_backup"), "save.png", true),
	CREATE_NOTE_FROM_CLIPBOARD(Translations.getString("action.create_note_from_clipboard"), "information.png", true),
	CREATE_TASK_FROM_CLIPBOARD(Translations.getString("action.create_task_from_clipboard"), "information.png", true),
	CUT(Translations.getString("action.cut"), "cut.png", false),
	DELETE(Translations.getString("action.delete"), "remove.png", true),
	DELETE_NOTE_SEARCHER(Translations.getString("action.delete_note_searcher"), "remove.png", true),
	DELETE_TASK_SEARCHER(Translations.getString("action.delete_task_searcher"), "remove.png", true),
	DONATE(Translations.getString("action.donate"), "dollar.png", true),
	DUPLICATE_NOTES(Translations.getString("action.duplicate_notes"), "duplicate.png", true),
	DUPLICATE_TASKS(Translations.getString("action.duplicate_tasks"), "duplicate.png", true),
	EDIT_TASKS(Translations.getString("action.edit_tasks"), "edit.png", true),
	EDIT_NOTE_SEARCHER(Translations.getString("action.edit_note_searcher"), "edit.png", true),
	EDIT_TASK_SEARCHER(Translations.getString("action.edit_task_searcher"), "edit.png", true),
	EXPAND_ALL(Translations.getString("action.expand_all"), "tree_collapsed.png", true),
	EXPORT_MODELS(Translations.getString("action.export_models"), "upload.png", true),
	EXPORT_NOTE_SEARCHERS(Translations.getString("action.export_note_searchers"), "upload.png", true),
	EXPORT_SETTINGS(Translations.getString("action.export_settings"), "upload.png", true),
	EXPORT_TASK_SEARCHERS(Translations.getString("action.export_task_searchers"), "upload.png", true),
	EXPORT_TASK_TEMPLATES(Translations.getString("action.export_task_templates"), "upload.png", true),
	EXPORT_VCARD(Translations.getString("action.export_vcard"), "upload.png", true),
	GET_SERIAL(Translations.getString("action.get_serial"), "key.png", false),
	HELP(Translations.getString("action.help"), "help.png", true),
	IMPORT_COM_FILE(Translations.getString("action.import_com_file"), "download.png", true),
	IMPORT_MODELS(Translations.getString("action.import_models"), "download.png", true),
	IMPORT_NOTE_SEARCHERS(Translations.getString("action.import_note_searchers"), "download.png", true),
	IMPORT_SETTINGS(Translations.getString("action.import_settings"), "download.png", true),
	IMPORT_TASK_SEARCHERS(Translations.getString("action.import_task_searchers"), "download.png", true),
	IMPORT_TASK_TEMPLATES(Translations.getString("action.import_task_templates"), "download.png", true),
	IMPORT_VCARD(Translations.getString("action.import_vcard"), "download.png", true),
	LOG_BUG(Translations.getString("action.log_bug"), null, false),
	LOG_FEATURE_REQUEST(Translations.getString("action.log_feature_request"), null, false),
	MAIL_TO(Translations.getString("action.mail_to"), "mail.png", true),
	MANAGE_BACKUPS(Translations.getString("action.manage_backups"), "save.png", true),
	MANAGE_MODELS(Translations.getString("action.manage_models"), "folder.png", true),
	MANAGE_PUBLISHER_PLUGINS(Translations.getString("action.manage_publisher_plugins"), "download.png", true),
	MANAGE_SYNCHRONIZER_PLUGINS(Translations.getString("action.manage_synchronizer_plugins"), "download.png", true),
	MANAGE_TASK_TEMPLATES(Translations.getString("action.manage_task_templates"), "template.png", true),
	MANAGE_USERS(Translations.getString("action.manage_users"), "user.png", true),
	PASTE(Translations.getString("action.paste"), "paste.png", false),
	PLUGIN_CONFIGURATION(Translations.getString("action.plugin_configuration"), "settings.png", true),
	POSTPONE_TASK_BEANS(Translations.getString("action.postpone_tasks"), "calendar.png", false),
	POSTPONE_TASKS(Translations.getString("action.postpone_tasks"), "calendar.png", false),
	POSTPONE_TASKS_MENU(Translations.getString("action.postpone_tasks"), "calendar.png", true),
	PRINT(Translations.getString("action.print"), "print.png", true),
	PRINT_SELECTED_MODELS(Translations.getString("action.print_selection"), "print.png", true),
	QUIT(Translations.getString("action.quit"), "exit.png", true),
	REDO(Translations.getString("action.redo"), "redo.png", true),
	REFRESH(Translations.getString("action.refresh"), "synchronize.png", false),
	RESET_GENERAL_SEARCHERS(Translations.getString("action.reset_general_searchers"), "undo.png", false),
	REVIEW(Translations.getString("action.review"), "information.png", true),
	SAVE(Translations.getString("action.save"), "save.png", false),
	SCHEDULED_SYNC(Translations.getString("action.scheduled_sync"), "synchronize_play.png", true),
	SELECT_PARENT_TASKS(Translations.getString("action.select_parent_tasks"), "task.png", true),
	SHOW_TIPS(Translations.getString("action.show_tips"), "information.png", true),
	SWITCH_TO_USER(Translations.getString("action.switch_user"), "user.png", false),
	SWITCH_TO_USER_MENU(Translations.getString("action.switch_user_menu"), "user.png", true),
	SYNCHRONIZE(Translations.getString("action.synchronize"), "synchronize.png", true),
	TASK_REMINDERS(Translations.getString("action.task_reminders"), "clock.png", true),
	UNDO(Translations.getString("action.undo"), "undo.png", true);
	
	private String title;
	private Icon icon;
	private boolean fitToolBar;
	
	private ActionList(String title, String icon, boolean fitToolBar) {
		this.title = title;
		
		if (icon == null)
			this.icon = null;
		else
			this.icon = ImageUtils.getResourceImage(icon, 16, 16);
		
		this.fitToolBar = fitToolBar;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Icon getIcon() {
		return this.icon;
	}
	
	public boolean isFitToolBar() {
		return this.fitToolBar;
	}
	
	public Action newInstance() {
		return this.newInstance(32, 32);
	}
	
	public Action newInstance(int width, int height) {
		switch (this) {
			case ABOUT:
				return new ActionAbout(width, height);
			case ADD_NOTE:
				return new ActionAddNote(width, height);
			case ADD_NOTE_SEARCHER:
				return new ActionAddNoteSearcher(width, height);
			case ADD_QUICK_TASK:
				return new ActionAddQuickTask(width, height);
			case ADD_SUBTASK:
				return new ActionAddSubTask(width, height);
			case ADD_SUBTASK_AT_SAME_LEVEL:
				return new ActionAddSubTaskAtSameLevel(width, height);
			case ADD_TASK:
				return new ActionAddTask(width, height);
			case ADD_TASK_SEARCHER:
				return new ActionAddTaskSearcher(width, height);
			case ADD_TASK_SEARCHER_SELECTED_TASKS:
				return new ActionAddTaskSearcherSelectedTasks(width, height);
			case ADD_TEMPLATE_TASK:
				return null;
			case ADD_TEMPLATE_TASK_MENU:
				return new ActionAddTemplateTaskMenu(
						ActionAddTemplateTask.ADD_TASK_LISTENER,
						width,
						height);
			case BATCH_ADD_TASKS:
				return new ActionBatchAddTasks(width, height);
			case CHANGE_DATE_FOLDER_LOCATION:
				return new ActionChangeDataFolderLocation(width, height);
			case CHANGE_VIEW:
				return new ActionChangeView(width, height);
			case CHANGE_VIEW_CALENDAR:
				return new ActionChangeViewCalendar(width, height);
			case CHECK_PLUGIN_VERSION:
				return new ActionCheckPluginVersion(false, width, height);
			case CHECK_VERSION:
				return new ActionCheckVersion(false, width, height);
			case COLLAPSE_ALL:
				return new ActionCollapseAll();
			case COMPLETE_TASKS:
				return new ActionCompleteTasks(width, height);
			case CONFIGURATION:
				return new ActionConfiguration(width, height);
			case COPY:
				return new ActionCopy(width, height);
			case CREATE_ACCOUNT:
				return null;
			case CREATE_NEW_BACKUP:
				return new ActionCreateNewBackup(width, height);
			case CREATE_NOTE_FROM_CLIPBOARD:
				return new ActionCreateNoteFromClipboard(width, height);
			case CREATE_TASK_FROM_CLIPBOARD:
				return new ActionCreateTaskFromClipboard(width, height);
			case CUT:
				return new ActionCut(width, height);
			case DELETE:
				return new ActionDelete(width, height);
			case DELETE_NOTE_SEARCHER:
				return new ActionDeleteNoteSearcher(width, height);
			case DELETE_TASK_SEARCHER:
				return new ActionDeleteTaskSearcher(width, height);
			case DONATE:
				return new ActionDonate(width, height);
			case DUPLICATE_NOTES:
				return new ActionDuplicateNotes(width, height);
			case DUPLICATE_TASKS:
				return new ActionDuplicateTasks(width, height);
			case EDIT_NOTE_SEARCHER:
				return new ActionEditNoteSearcher(width, height);
			case EDIT_TASK_SEARCHER:
				return new ActionEditTaskSearcher(width, height);
			case EDIT_TASKS:
				return new ActionEditTasks(width, height);
			case EXPAND_ALL:
				return new ActionExpandAll();
			case EXPORT_MODELS:
				return new ActionExportModels(width, height);
			case EXPORT_NOTE_SEARCHERS:
				return new ActionExportNoteSearchers(width, height);
			case EXPORT_SETTINGS:
				return new ActionExportSettings(width, height);
			case EXPORT_TASK_SEARCHERS:
				return new ActionExportTaskSearchers(width, height);
			case EXPORT_TASK_TEMPLATES:
				return new ActionExportTaskTemplates(width, height);
			case EXPORT_VCARD:
				return new ActionExportVCard(width, height);
			case GET_SERIAL:
				return null;
			case HELP:
				return new ActionHelp(width, height);
			case IMPORT_COM_FILE:
				return new ActionImportComFile(width, height);
			case IMPORT_MODELS:
				return new ActionImportModels(width, height);
			case IMPORT_NOTE_SEARCHERS:
				return new ActionImportNoteSearchers(width, height);
			case IMPORT_SETTINGS:
				return new ActionImportSettings(width, height);
			case IMPORT_TASK_SEARCHERS:
				return new ActionImportTaskSearchers(width, height);
			case IMPORT_TASK_TEMPLATES:
				return new ActionImportTaskTemplates(width, height);
			case IMPORT_VCARD:
				return new ActionImportVCard(width, height);
			case LOG_BUG:
				return new ActionLogBug();
			case LOG_FEATURE_REQUEST:
				return new ActionLogFeatureRequest();
			case MAIL_TO:
				return new ActionMailTo(width, height);
			case MANAGE_MODELS:
				return new ActionManageModels(width, height);
			case MANAGE_PUBLISHER_PLUGINS:
				return new ActionManagePublisherPlugins(width, height);
			case MANAGE_SYNCHRONIZER_PLUGINS:
				return new ActionManageSynchronizerPlugins(width, height);
			case MANAGE_TASK_TEMPLATES:
				return new ActionManageTaskTemplates(width, height);
			case MANAGE_USERS:
				return new ActionManageUsers(width, height);
			case PASTE:
				return new ActionPaste(width, height);
			case PLUGIN_CONFIGURATION:
				return new ActionPluginConfiguration(width, height);
			case POSTPONE_TASK_BEANS:
				return null;
			case POSTPONE_TASKS:
				return null;
			case POSTPONE_TASKS_MENU:
				return new ActionPostponeTasksMenu(width, height, null);
			case PRINT:
				return new ActionPrint(width, height);
			case PRINT_SELECTED_MODELS:
				return new ActionPrintSelectedModels(width, height);
			case QUIT:
				return new ActionQuit(width, height);
			case REDO:
				return new ActionRedo(
						Constants.UNDO_MANAGER,
						Constants.EDIT_SUPPORT,
						width,
						height);
			case REFRESH:
				return new ActionRefresh(width, height);
			case RESET_GENERAL_SEARCHERS:
				return new ActionResetGeneralSearchers(width, height);
			case REVIEW:
				return new ActionReview(width, height);
			case SAVE:
				return new ActionSave(width, height);
			case SCHEDULED_SYNC:
				return new ActionScheduledSync(width, height);
			case SELECT_PARENT_TASKS:
				return new ActionSelectParentTasks(width, height);
			case SHOW_TIPS:
				return new ActionShowTips(width, height);
			case SWITCH_TO_USER:
				return null;
			case SWITCH_TO_USER_MENU:
				return new ActionSwitchToUserMenu(width, height);
			case SYNCHRONIZE:
				return new ActionSynchronize(false, width, height);
			case TASK_REMINDERS:
				return new ActionTaskReminders(width, height);
			case UNDO:
				return new ActionUndo(
						Constants.UNDO_MANAGER,
						Constants.EDIT_SUPPORT,
						width,
						height);
			default:
				return null;
		}
	}
	
}
