package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.views.CalendarView;
import com.leclercb.taskunifier.gui.components.views.TaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewTaskSelectionAction extends AbstractViewAction implements ModelSelectionListener, PropertyChangeListener {
	
	public AbstractViewTaskSelectionAction() {
		super(ViewType.TASKS, ViewType.CALENDAR);
		this.initialize();
	}
	
	public AbstractViewTaskSelectionAction(String title) {
		super(title, ViewType.TASKS, ViewType.CALENDAR);
		this.initialize();
	}
	
	public AbstractViewTaskSelectionAction(String title, Icon icon) {
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
				((CalendarView) oldView.getView()).getTaskCalendarView().removeModelSelectionChangeListener(
						this);
			}
			
			if (oldView.getViewType() == ViewType.TASKS) {
				((TaskView) oldView.getView()).getTaskTableView().removeModelSelectionChangeListener(
						this);
			}
		}
		
		if (ViewList.getInstance().getCurrentView().isLoaded()) {
			if (ViewUtils.getCurrentViewType() == ViewType.CALENDAR) {
				ViewUtils.getCurrentCalendarView().getTaskCalendarView().addModelSelectionChangeListener(
						this);
			}
			
			if (ViewUtils.getCurrentViewType() == ViewType.TASKS) {
				ViewUtils.getCurrentTaskView().getTaskTableView().addModelSelectionChangeListener(
						this);
			}
		}
	}
	
	@Override
	public final void modelSelectionChange(ModelSelectionChangeEvent event) {
		this.setEnabled(this.shouldBeEnabled2());
	}
	
}
