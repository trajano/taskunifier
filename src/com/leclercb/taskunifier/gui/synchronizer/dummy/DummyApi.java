package com.leclercb.taskunifier.gui.synchronizer.dummy;

import java.util.Properties;

import com.leclercb.taskunifier.api.synchronizer.Connection;
import com.leclercb.taskunifier.api.synchronizer.Synchronizer;
import com.leclercb.taskunifier.api.synchronizer.SynchronizerApi;
import com.leclercb.taskunifier.api.synchronizer.exc.SynchronizerException;

public class DummyApi extends SynchronizerApi {

	private static DummyApi INSTANCE;

	public static final DummyApi getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DummyApi();

		return INSTANCE;
	}

	protected DummyApi() {
		super("DUMMY", "No Service", "http://www.taskunifier.net", "");
	}

	@Override
	public boolean isValidRepeatValue(String repeat) {
		return true;
	}

	@Override
	public void createAccount(Object[] parameters) throws SynchronizerException {

	}

	@Override
	public Connection getConnection(Object[] parameters)
	throws SynchronizerException {
		return null;
	}

	@Override
	public Synchronizer getSynchronizer(Connection connection) {
		return null;
	}

	@Override
	public void resetConnectionParameters(Properties properties) {

	}

	@Override
	public void resetSynchronizerParameters(Properties properties) {

	}

}
