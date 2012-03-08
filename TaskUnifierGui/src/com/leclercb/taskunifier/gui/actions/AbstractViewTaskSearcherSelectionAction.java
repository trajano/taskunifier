package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.TaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.WeakTaskSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.tasksearchertree.TaskSearcherView;
import com.leclercb.taskunifier.gui.components.views.CalendarView;
import com.leclercb.taskunifier.gui.components.views.TaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewTaskSearcherSelectionAction extends AbstractViewAction implements TaskSearcherSelectionListener, PropertyChangeListener {
	
	public AbstractViewTaskSearcherSelectionAction() {
		this(null, null);
	}
	
	public AbstractViewTaskSearcherSelectionAction(String title) {
		this(title, null);
	}
	
	public AbstractViewTaskSearcherSelectionAction(String title, Icon icon) {
		super(title, icon, ViewType.TASKS, ViewType.CALENDAR);
		this.initialize();
	}
	
	private void initialize() {
		ViewList.getInstance().addPropertyChangeListener(
				ViewList.PROP_CURRENT_VIEW,
				new WeakPropertyChangeListener(ViewList.getInstance(), this));
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event != null && event.getOldValue() != null) {
			ViewItem oldView = (ViewItem) event.getOldValue();
			
			TaskSearcherView view = null;
			
			if (oldView.getViewType() == ViewType.CALENDAR)
				view = ((CalendarView) oldView.getView()).getTaskSearcherView();
			
			if (oldView.getViewType() == ViewType.TASKS)
				view = ((TaskView) oldView.getView()).getTaskSearcherView();
			
			if (view != null)
				view.removeTaskSearcherSelectionChangeListener(this);
		}
		
		if (ViewList.getInstance().getCurrentView().isLoaded()) {
			TaskSearcherView view = null;
			
			if (ViewUtils.getCurrentViewType() == ViewType.CALENDAR)
				view = ViewUtils.getCurrentCalendarView().getTaskSearcherView();
			
			if (ViewUtils.getCurrentViewType() == ViewType.TASKS)
				view = ViewUtils.getCurrentTaskView().getTaskSearcherView();
			
			if (view != null)
				view.addTaskSearcherSelectionChangeListener(new WeakTaskSearcherSelectionListener(
						view,
						this));
		}
	}
	
	@Override
	public void taskSearcherSelectionChange(
			TaskSearcherSelectionChangeEvent event) {
		this.setEnabled(this.shouldBeEnabled());
	}
	
}
