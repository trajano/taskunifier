package com.leclercb.taskunifier.gui.swing;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.event.action.ActionSupported;
import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.commons.api.progress.ProgressMessage;
import com.leclercb.commons.api.progress.ProgressMonitor;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.translations.Translations;

public abstract class TUWorker<T> extends SwingWorker<T, ProgressMessage> implements ActionSupported {
	
	public static final String ACTION_FINISHED = "ACTION_FINISHED";
	
	private ActionSupport actionSupport;
	
	private ProgressMonitor workerMonitor;
	private ProgressMonitor monitor;
	
	public TUWorker(ProgressMonitor monitor) {
		this.actionSupport = new ActionSupport(this);
		this.workerMonitor = new ProgressMonitor();
		
		this.workerMonitor.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				if (event.getChangeType() == ListChangeEvent.VALUE_ADDED)
					TUWorker.this.monitor.addMessage((ProgressMessage) event.getValue());
			}
			
		});
		
		this.setMonitor(monitor);
	}
	
	public ProgressMonitor getWorkerMonitor() {
		return this.workerMonitor;
	}
	
	public ProgressMonitor getMonitor() {
		return this.monitor;
	}
	
	public void setMonitor(ProgressMonitor monitor) {
		CheckUtils.isNotNull(monitor);
		this.monitor = monitor;
	}
	
	@Override
	protected void process(List<ProgressMessage> messages) {
		for (ProgressMessage message : messages) {
			this.monitor.addMessage(message);
		}
	}
	
	protected abstract T longTask() throws Exception;
	
	@Override
	protected final T doInBackground() throws Exception {
		try {
			return this.longTask();
		} catch (final Throwable e) {
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					ErrorInfo info = new ErrorInfo(
							Translations.getString("general.error"),
							e.getMessage(),
							null,
							null,
							e,
							null,
							null);
					
					JXErrorPane.showDialog(
							MainFrame.getInstance().getFrame(),
							info);
				}
				
			});
			
			return null;
		} finally {
			Thread.sleep(1000);
		}
	}
	
	@Override
	protected void done() {
		super.done();
		this.actionSupport.fireActionPerformed(0, ACTION_FINISHED);
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
