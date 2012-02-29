package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.views.CalendarView;
import com.leclercb.taskunifier.gui.components.views.TaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewTaskSearcherSelectionAction extends AbstractViewAction implements TaskSearcherSelectionListener, PropertyChangeListener {
	
	public AbstractViewTaskSearcherSelectionAction() {
		super(ViewType.TASKS, ViewType.CALENDAR);
		this.initialize();
	}
	
	public AbstractViewTaskSearcherSelectionAction(String title) {
		super(title, ViewType.TASKS, ViewType.CALENDAR);
		this.initialize();
	}
	
	public AbstractViewTaskSearcherSelectionAction(String title, Icon icon) {
		super(title, icon, ViewType.TASKS, ViewType.CALENDAR);
		this.initialize();
	}
	
	private void initialize() {
		ViewList.getInstance().addPropertyChangeListener(
				ViewList.PROP_CURRENT_VIEW,
				this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event != null && event.getOldValue() != null) {
			ViewItem oldView = (ViewItem) event.getOldValue();
			
			if (oldView.getViewType() == ViewType.CALENDAR) {
				((CalendarView) oldView.getView()).getTaskSearcherView().removeTaskSearcherSelectionChangeListener(
						this);
			}
			
			if (oldView.getViewType() == ViewType.TASKS) {
				((TaskView) oldView.getView()).getTaskSearcherView().removeTaskSearcherSelectionChangeListener(
						this);
			}
		}
		
		if (ViewList.getInstance().getCurrentView().isLoaded()) {
			if (ViewUtils.getCurrentViewType() == ViewType.CALENDAR) {
				ViewUtils.getCurrentCalendarView().getTaskSearcherView().addTaskSearcherSelectionChangeListener(
						this);
			}
			
			if (ViewUtils.getCurrentViewType() == ViewType.TASKS) {
				ViewUtils.getCurrentTaskView().getTaskSearcherView().addTaskSearcherSelectionChangeListener(
						this);
			}
		}
	}
	
	@Override
	public void taskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		this.setEnabled(this.shouldBeEnabled2());
	}
	
}
