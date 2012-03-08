package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;

import com.leclercb.commons.api.event.propertychange.WeakPropertyChangeListener;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.WeakNoteSearcherSelectionListener;
import com.leclercb.taskunifier.gui.components.notesearchertree.NoteSearcherView;
import com.leclercb.taskunifier.gui.components.views.NoteView;
import com.leclercb.taskunifier.gui.components.views.ViewItem;
import com.leclercb.taskunifier.gui.components.views.ViewList;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;

public abstract class AbstractViewNoteSearcherSelectionAction extends AbstractViewAction implements NoteSearcherSelectionListener, PropertyChangeListener {
	
	public AbstractViewNoteSearcherSelectionAction() {
		this(null, null);
	}
	
	public AbstractViewNoteSearcherSelectionAction(String title) {
		this(title, null);
	}
	
	public AbstractViewNoteSearcherSelectionAction(String title, Icon icon) {
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
				NoteSearcherView view = ((NoteView) oldView.getView()).getNoteSearcherView();
				view.removeNoteSearcherSelectionChangeListener(this);
			}
		}
		
		if (ViewList.getInstance().getCurrentView().isLoaded()) {
			if (ViewUtils.getCurrentViewType() == ViewType.NOTES) {
				NoteSearcherView view = ViewUtils.getCurrentNoteView().getNoteSearcherView();
				view.addNoteSearcherSelectionChangeListener(new WeakNoteSearcherSelectionListener(
						view,
						this));
			}
		}
	}
	
	@Override
	public void noteSearcherSelectionChange(
			NoteSearcherSelectionChangeEvent event) {
		this.setEnabled(this.shouldBeEnabled());
	}
	
}
