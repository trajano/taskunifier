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
package com.leclercb.taskunifier.api.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import com.leclercb.commons.api.coder.exc.FactoryCoderException;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.properties.PropertyMap;
import com.leclercb.taskunifier.api.models.ContactFactory;
import com.leclercb.taskunifier.api.models.ContextFactory;
import com.leclercb.taskunifier.api.models.FolderFactory;
import com.leclercb.taskunifier.api.models.GoalFactory;
import com.leclercb.taskunifier.api.models.LocationFactory;
import com.leclercb.taskunifier.api.models.ModelId;
import com.leclercb.taskunifier.api.models.NoteFactory;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.models.TaskFactory;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerChoice;

public class Example {
	
	@SuppressWarnings("null")
	public static void main(String[] args) throws Exception {
		SynchronizerApi api = null;
		// api = new XxxSynchronizerPlugin().getSynchronizerApi();
		System.out.println("API: " + api.getApiName());
		
		// Initialize API
		// See API documentation
		
		// Load settings
		PropertyMap settings = new PropertyMap(new Properties());
		
		loadSettings(settings);
		
		// Load models from files
		loadModels();
		
		// Initialize proxy
		// api.setProxyHost(proxyHost);
		// api.setProxyPort(proxyPort);
		
		// Initialize connection
		Connection connection = api.getConnection(settings);
		connection.loadParameters(settings);
		
		connection.connect();
		
		// Declare a progress monitor
		ProgressMonitor monitor = new ProgressMonitor();
		monitor.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				if (event.getChangeType() == ListChangeEvent.VALUE_ADDED)
					System.out.println(event.getValue());
			}
			
		});
		
		// Get synchronizer
		Synchronizer synchronizer = api.getSynchronizer(settings, connection);
		synchronizer.loadParameters(settings);
		
		// Synchronize
		synchronizer.synchronize(SynchronizerChoice.KEEP_LAST_UPDATED, monitor);
		
		// Add, edit or delete contexts, folders, goals, locations and tasks
		Task task = TaskFactory.getInstance().get(new ModelId("12345"));
		task.setTitle("My new title");
		
		// Synchronize
		synchronizer.synchronize(monitor);
		
		// Save models to file
		saveModels();
		
		// Save settings
		connection.saveParameters(settings);
		synchronizer.saveParameters(settings);
		
		settings.store(
				new FileOutputStream("data/settings.properties"),
				"Settings");
	}
	
	private static void loadSettings(PropertyMap settings) {
		try {
			settings.load(new FileInputStream("data/settings.properties"));
		} catch (Exception e) {
			System.err.println("Cannot load property file");
			throw new RuntimeException();
		}
	}
	
	private static void loadModels() throws FactoryCoderException {
		try {
			ContactFactory.getInstance().decodeFromXML(
					new FileInputStream("data/contacts.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			ContextFactory.getInstance().decodeFromXML(
					new FileInputStream("data/contexts.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			FolderFactory.getInstance().decodeFromXML(
					new FileInputStream("data/folders.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			GoalFactory.getInstance().decodeFromXML(
					new FileInputStream("data/goals.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			LocationFactory.getInstance().decodeFromXML(
					new FileInputStream("data/locations.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			NoteFactory.getInstance().decodeFromXML(
					new FileInputStream("data/notes.xml"));
		} catch (FileNotFoundException e) {}
		
		try {
			TaskFactory.getInstance().decodeFromXML(
					new FileInputStream("data/tasks.xml"));
		} catch (FileNotFoundException e) {}
	}
	
	private static void saveModels() throws FileNotFoundException,
			FactoryCoderException {
		ContactFactory.getInstance().encodeToXML(
				new FileOutputStream("data/contacts.xml"));
		ContextFactory.getInstance().encodeToXML(
				new FileOutputStream("data/contexts.xml"));
		FolderFactory.getInstance().encodeToXML(
				new FileOutputStream("data/folders.xml"));
		GoalFactory.getInstance().encodeToXML(
				new FileOutputStream("data/goals.xml"));
		LocationFactory.getInstance().encodeToXML(
				new FileOutputStream("data/locations.xml"));
		NoteFactory.getInstance().encodeToXML(
				new FileOutputStream("data/notes.xml"));
		TaskFactory.getInstance().encodeToXML(
				new FileOutputStream("data/tasks.xml"));
	}
	
}
