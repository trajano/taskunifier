package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.components.views.NoteView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewNoteSelectionAction extends AbstractViewAction implements ModelSelectionListener, PropertyChangeListener {
	
	public AbstractViewNoteSelectionAction() {
		super(ViewType.NOTES);
		this.initialize();
	}
	
	public AbstractViewNoteSelectionAction(String title) {
		super(title, ViewType.NOTES);
		this.initialize();
	}
	
	public AbstractViewNoteSelectionAction(String title, Icon icon) {
		super(title, icon, ViewType.NOTES);
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
			
			if (oldView.getViewType() == ViewType.NOTES) {
				((NoteView) oldView.getView()).getNoteTableView().removeModelSelectionChangeListener(
						this);
			}
		}
		
		if (ViewList.getInstance().getCurrentView().isLoaded()) {
			if (ViewUtils.getCurrentViewType() == ViewType.NOTES) {
				ViewUtils.getCurrentNoteView().getNoteTableView().addModelSelectionChangeListener(
						this);
			}
		}
	}
	
	@Override
	public abstract void modelSelectionChange(ModelSelectionChangeEvent event);
	
}
