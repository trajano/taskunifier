package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.WeakModelSelectionListener;
import com.leclercb.taskunifier.gui.components.notes.NoteTableView;
import com.leclercb.taskunifier.gui.components.views.NoteView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewNoteSelectionAction extends AbstractViewAction implements ModelSelectionListener, PropertyChangeListener {
	
	public AbstractViewNoteSelectionAction() {
		this(null, null);
	}
	
	public AbstractViewNoteSelectionAction(String title) {
		this(title, null);
	}
	
	public AbstractViewNoteSelectionAction(String title, Icon icon) {
		super(title, icon, ViewType.NOTES);
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
			
			if (oldView.getViewType() == ViewType.NOTES) {
				NoteTableView view = ((NoteView) oldView.getView()).getNoteTableView();
				view.removeModelSelectionChangeListener(this);
			}
		}
		
		if (ViewList.getInstance().getCurrentView().isLoaded()) {
			if (ViewUtils.getCurrentViewType() == ViewType.NOTES) {
				NoteTableView view = ViewUtils.getCurrentNoteView().getNoteTableView();
				view.addModelSelectionChangeListener(new WeakModelSelectionListener(
						view,
						this));
			}
		}
	}
	
	@Override
	public final void modelSelectionChange(ModelSelectionChangeEvent event) {
		this.setEnabled(this.shouldBeEnabled());
	}
	
}
