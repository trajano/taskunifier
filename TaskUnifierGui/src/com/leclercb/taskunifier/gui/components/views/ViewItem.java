package com.leclercb.taskunifier.gui.components.views;

import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.Icon;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.gui.logger.GuiLogger;

public class ViewItem implements ActionSupported {
	
	public static final String ACTION_VIEW_LOADED = "ACTION_VIEW_LOADED";
	
	private ActionSupport actionSupport;
	
	private ViewType viewType;
	private View view;
	private String label;
	private Icon icon;
	private int frameId;
	private boolean removable;
	
	public ViewItem(
			ViewType viewType,
			String label,
			Icon icon,
			int frameId,
			boolean removable) {
		CheckUtils.isNotNull(viewType);
		CheckUtils.isNotNull(label);
		CheckUtils.isNotNull(icon);
		
		this.actionSupport = new ActionSupport(this);
		
		this.viewType = viewType;
		this.view = null;
		this.label = label;
		this.icon = icon;
		this.frameId = frameId;
		this.removable = removable;
	}
	
	public boolean isLoaded() {
		return this.view != null;
	}
	
	public ViewType getViewType() {
		return this.viewType;
	}
	
	public View getView() {
		if (!this.isLoaded())
			GuiLogger.getLogger().log(
					Level.SEVERE,
					"View \"" + this.label + "\" is not loaded yet",
					new Throwable().getStackTrace());
		
		return this.view;
	}
	
	public void setView(View view) {
		CheckUtils.isNotNull(view);
		
		if (!this.viewType.match(view))
			throw new RuntimeException("View \""
					+ this.label
					+ "\" has an incorrect type");
		
		if (this.view != null)
			throw new RuntimeException("View \""
					+ this.label
					+ "\" is already loaded");
		
		this.view = view;
		this.actionSupport.fireActionPerformed(0, ACTION_VIEW_LOADED);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public Icon getIcon() {
		return this.icon;
	}
	
	public int getFrameId() {
		return this.frameId;
	}
	
	public boolean isRemovable() {
		return this.removable;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
	
	@Override
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
}
