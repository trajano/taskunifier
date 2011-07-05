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
package com.leclercb.taskunifier.gui.api.synchronizer.dummy;

import java.util.Properties;

import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.review.Reviewed;

@Reviewed
public class DummyApi extends SynchronizerApi {
	
	private static DummyApi INSTANCE;
	
	public static final DummyApi getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DummyApi();
		
		return INSTANCE;
	}
	
	protected DummyApi() {
		super("DUMMY", "No Synchronization", "http://www.taskunifier.net");
	}
	
	@Override
	public String getRepeatHelpFile() {
		return Help.getHelpFile("task_repeat.html");
	}
	
	@Override
	public String[] getDefaultRepeatValues() {
		return new String[] {
				"Daily",
				"Weekly",
				"Biweekly",
				"Monthly",
				"Bimonthly",
				"Quarterly",
				"Semiannually",
				"Yearly" };
	}
	
	@Override
	public boolean isValidRepeatValue(String repeat) {
		return RepeatUtils.isValidRepeatValue(repeat);
	}
	
	@Override
	public void createRepeatTask(Task task) {
		RepeatUtils.createRepeatTask(task);
	}
	
	@Override
	public Connection getConnection(Properties properties)
			throws SynchronizerException {
		throw new SynchronizerException(
				true,
				Translations.getString("synchronizer.select_an_api"));
	}
	
	@Override
	public Synchronizer getSynchronizer(
			Properties properties,
			Connection connection) {
		return null;
	}
	
	@Override
	public void resetConnectionParameters(Properties properties) {

	}
	
	@Override
	public void resetSynchronizerParameters(Properties properties) {

	}
	
}
