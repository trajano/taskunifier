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

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.logger.ApiLogger;
import com.leclercb.commons.api.utils.FileUtils;
import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.plugins.PluginLogger;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionGetLogs extends AbstractAction {
	
	public ActionGetLogs(int width, int height) {
		super(
				Translations.getString("action.get_logs"),
				ImageUtils.getResourceImage("download.png", width, height));
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.get_logs"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionGetLogs.getLogs();
	}
	
	public static void getLogs() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = fileChooser.showSaveDialog(FrameUtils.getCurrentFrame());
			
			if (result != JFileChooser.APPROVE_OPTION)
				return;
			
			String file = fileChooser.getSelectedFile().getAbsolutePath();
			
			if (!file.endsWith(".zip")) {
				file += ".zip";
			}
			
			ZipOutputStream zos = new ZipOutputStream(
					new FileOutputStream(file));
			
			List<Handler> handlers = new ArrayList<Handler>();
			handlers.addAll(Arrays.asList(ApiLogger.getLogger().getHandlers()));
			handlers.addAll(Arrays.asList(GuiLogger.getLogger().getHandlers()));
			handlers.addAll(Arrays.asList(PluginLogger.getLogger().getHandlers()));
			
			for (Handler handler : handlers) {
				if (handler instanceof FileHandler)
					((FileHandler) handler).flush();
			}
			
			File[] logFiles = new File(Main.getDataFolder()).listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File file) {
					if (!file.isFile())
						return false;
					
					if (!file.getName().contains(".log")
							&& !FileUtils.hasExtention(file.getName(), "lck"))
						return false;
					
					return true;
				}
				
			});
			
			for (File logFile : logFiles) {
				writeIntoZip(zos, logFile.getName(), new FileInputStream(
						logFile));
			}
			
			zos.close();
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					e.getMessage(),
					null,
					"GUI",
					e,
					Level.WARNING,
					null);
			
			JXErrorPane.showDialog(FrameUtils.getCurrentFrame(), info);
		}
	}
	
	private static void writeIntoZip(
			ZipOutputStream output,
			String name,
			InputStream input) throws Exception {
		output.putNextEntry(new ZipEntry(name));
		
		int size = 0;
		byte[] buffer = new byte[1024];
		
		while ((size = input.read(buffer, 0, buffer.length)) > 0) {
			output.write(buffer, 0, size);
		}
		
		output.closeEntry();
		input.close();
	}
	
}
