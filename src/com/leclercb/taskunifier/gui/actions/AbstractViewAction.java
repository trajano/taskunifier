package com.leclercb.taskunifier.gui.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.main.MainView;

public abstract class AbstractViewAction extends AbstractAction {
	
	private ViewType[] enabledViews;
	
	public AbstractViewAction(ViewType... enabledViews) {
		super();
		this.initialize(enabledViews);
	}
	
	public AbstractViewAction(String title, Icon icon, ViewType... enabledViews) {
		super(title, icon);
		this.initialize(enabledViews);
	}
	
	public AbstractViewAction(String title, ViewType... enabledViews) {
		super(title);
		this.initialize(enabledViews);
	}
	
	private void initialize(final ViewType... enabledViews) {
		CheckUtils.isNotNull(enabledViews);
		
		this.enabledViews = enabledViews;
		
		MainFrame.getInstance().addPropertyChangeListener(
				MainView.PROP_SELECTED_VIEW,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						AbstractViewAction.this.setEnabled(AbstractViewAction.this.shouldBeEnabled());
					}
					
				});
	}
	
	protected boolean shouldBeEnabled() {
		if (this.enabledViews != null && this.enabledViews.length != 0) {
			for (ViewType view : this.enabledViews)
				if (view.equals(MainFrame.getInstance().getSelectedViewType()))
					return true;
			
			return false;
		} else {
			return true;
		}
	}
	
}
