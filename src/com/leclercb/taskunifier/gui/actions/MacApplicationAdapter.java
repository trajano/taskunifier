/*
 * TaskUnifier
 * Copyright (c) 2011, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.actions;

import java.awt.Frame;
import java.awt.PopupMenu;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.lang3.SystemUtils;

import com.apple.eawt.Application;
import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Model;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.gui.components.synchronize.Synchronizing;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.utils.TaskUtils;

@SuppressWarnings("deprecation")
public class MacApplicationAdapter extends ApplicationAdapter {
	
	public MacApplicationAdapter() {
		
	}
	
	@Override
	public void handleQuit(ApplicationEvent e) {
		e.setHandled(true);
		ActionQuit.quit();
	}
	
	@Override
	public void handleAbout(ApplicationEvent e) {
		e.setHandled(true);
		ActionAbout.about();
	}
	
	@Override
	public void handlePreferences(ApplicationEvent e) {
		e.setHandled(true);
		ActionConfiguration.configuration();
	}
	
	@Override
	public void handlePrintFile(ApplicationEvent e) {
		e.setHandled(true);
		ActionPrint.print();
	}
	
	@Override
	public void handleReOpenApplication(ApplicationEvent e) {
		e.setHandled(true);
		MainFrame.getInstance().getFrame().setVisible(true);
		MainFrame.getInstance().getFrame().setState(Frame.NORMAL);
	}
	
	public static void initializeApplicationAdapter() {
		if (!SystemUtils.IS_OS_MAC)
			return;
		
		try {
			Application application = Application.getApplication();
			MacApplicationAdapter adapter = new MacApplicationAdapter();
			application.setEnabledPreferencesMenu(true);
			application.addApplicationListener(adapter);
		} catch (Throwable t) {
			
		}
		
		initializeDockIconBadge();
	}
	
	private static void initializeDockIconBadge() {
		updateDockIconBadge();
		
		Synchronizing.addPropertyChangeListener(
				Synchronizing.PROP_SYNCHRONIZING,
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						if (!(Boolean) evt.getNewValue())
							updateDockIconBadge();
					}
					
				});
		
		TaskFactory.getInstance().addListChangeListener(
				new ListChangeListener() {
					
					@Override
					public void listChange(ListChangeEvent event) {
						if (Synchronizing.isSynchronizing())
							return;
						
						updateDockIconBadge();
					}
					
				});
		
		TaskFactory.getInstance().addPropertyChangeListener(
				new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent event) {
						if (Synchronizing.isSynchronizing())
							return;
						
						if (event.getPropertyName().equals(
								Model.PROP_MODEL_STATUS)
								|| event.getPropertyName().equals(
										Task.PROP_COMPLETED)
								|| event.getPropertyName().equals(
										Task.PROP_DUE_DATE))
							updateDockIconBadge();
					}
					
				});
	}
	
	private static void updateDockIconBadge() {
		if (!SystemUtils.IS_OS_MAC)
			return;
		
		try {
			String badge = TaskUtils.getOverdueTaskCount() + "";
			
			if (badge.equals("0"))
				badge = "";
			
			Application application = Application.getApplication();
			application.setDockIconBadge(badge);
		} catch (Throwable t) {
			
		}
	}
	
	public static void requestUserAttention() {
		if (!SystemUtils.IS_OS_MAC)
			return;
		
		try {
			Application application = Application.getApplication();
			application.requestUserAttention(true);
		} catch (Throwable t) {
			
		}
	}
	
	public static void setDockMenu(PopupMenu popupMenu) {
		try {
			Application application = Application.getApplication();
			application.setDockMenu(popupMenu);
		} catch (Throwable t) {
			
		}
	}
	
}
