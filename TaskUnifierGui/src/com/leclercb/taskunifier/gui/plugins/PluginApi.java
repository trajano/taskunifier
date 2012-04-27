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
package com.leclercb.taskunifier.gui.plugins;

import java.awt.Frame;
import java.util.Locale;

import javax.help.HelpSet;

import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.taskunifier.gui.api.synchronizer.SynchronizerGuiPlugin;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.main.frames.FrameView;
import com.leclercb.taskunifier.gui.swing.TUSwingUtilities;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.SynchronizerUtils;

public final class PluginApi {
	
	private PluginApi() {
		
	}
	
	public static void invokeLater(Runnable doRun) {
		TUSwingUtilities.invokeLater(doRun);
	}
	
	public static void invokeAndWait(Runnable doRun) {
		TUSwingUtilities.invokeAndWait(doRun);
	}
	
	public static void executeOrInvokeAndWait(Runnable doRun) {
		TUSwingUtilities.executeOrInvokeAndWait(doRun);
	}
	
	public static Frame getCurrentFrame() {
		return FrameUtils.getCurrentFrame();
	}
	
	public static FrameView getCurrentFrameView() {
		return FrameUtils.getCurrentFrameView();
	}
	
	public static PropertyMap getSettings() {
		return Main.getSettings();
	}
	
	public static PropertyMap getUserSettings() {
		return Main.getUserSettings();
	}
	
	public static SynchronizerGuiPlugin getPlugin(String pluginId) {
		return SynchronizerUtils.getPlugin(pluginId);
	}
	
	public static void initializeProxy(SynchronizerGuiPlugin plugin) {
		SynchronizerUtils.initializeProxy(plugin);
	}
	
	public static Locale getLocale() {
		return Translations.getLocale();
	}
	
	public static String getTranslation(String key) {
		return Translations.getString(key);
	}
	
	public static String getTranslation(String key, Object... args) {
		return Translations.getString(key, args);
	}
	
	public static void resetAllSynchronizersAndDeleteModels() {
		SynchronizerUtils.resetAllSynchronizersAndDeleteModels();
	}
	
	public static void addHelpSet(HelpSet helpSet) {
		Help.getInstance().getHelpSet().add(helpSet);
	}
	
	public static void removeHelpSet(HelpSet helpSet) {
		Help.getInstance().getHelpSet().remove(helpSet);
	}
	
}
