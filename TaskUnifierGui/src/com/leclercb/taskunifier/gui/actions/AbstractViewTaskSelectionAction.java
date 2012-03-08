package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupported;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.WeakModelSelectionListener;
import com.leclercb.taskunifier.gui.components.views.CalendarView;
import com.leclercb.taskunifier.gui.components.views.TaskView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewTaskSelectionAction extends AbstractViewAction implements ModelSelectionListener, PropertyChangeListener {
	
	public AbstractViewTaskSelectionAction() {
		this(null, null);
	}
	
	public AbstractViewTaskSelectionAction(String title) {
		this(title, null);
	}
	
	public AbstractViewTaskSelectionAction(String title, Icon icon) {
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
			
			ModelSelectionChangeSupported view = null;
			
			if (oldView.getViewType() == ViewType.CALENDAR)
				view = ((CalendarView) oldView.getView()).getTaskCalendarView();
			
			if (oldView.getViewType() == ViewType.TASKS)
				view = ((TaskView) oldView.getView()).getTaskTableView();
			
			if (view != null)
				view.removeModelSelectionChangeListener(this);
		}
		
		if (ViewList.getInstance().getCurrentView().isLoaded()) {
			ModelSelectionChangeSupported view = null;
			
			if (ViewUtils.getCurrentViewType() == ViewType.CALENDAR)
				view = ViewUtils.getCurrentCalendarView().getTaskCalendarView();
			
			if (ViewUtils.getCurrentViewType() == ViewType.TASKS)
				view = ViewUtils.getCurrentTaskView().getTaskTableView();
			
			if (view != null)
				view.addModelSelectionChangeListener(new WeakModelSelectionListener(
						view,
						this));
		}
	}
	
	@Override
	public final void modelSelectionChange(ModelSelectionChangeEvent event) {
		this.setEnabled(this.shouldBeEnabled());
	}
	
}
