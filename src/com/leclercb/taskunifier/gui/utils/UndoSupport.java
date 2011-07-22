package com.leclercb.taskunifier.gui.utils;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEditSupport;

import com.leclercb.commons.gui.swing.undo.UndoFireManager;
import com.leclercb.taskunifier.gui.actions.ActionRedo;
import com.leclercb.taskunifier.gui.actions.ActionUndo;

public class UndoSupport implements UndoableEditListener {
	
	private UndoFireManager undoManager;
	private UndoableEditSupport editSupport;
	
	private ActionUndo undoAction;
	private ActionRedo redoAction;
	
	public UndoSupport() {
		this.undoManager = new UndoFireManager();
		this.editSupport = new UndoableEditSupport();
		this.editSupport.addUndoableEditListener(this.undoManager);
		
		this.undoAction = new ActionUndo(
				this.undoManager,
				this.editSupport,
				16,
				16);
		
		this.redoAction = new ActionRedo(
				this.undoManager,
				this.editSupport,
				16,
				16);
	}
	
	public ActionUndo getUndoAction() {
		return this.undoAction;
	}
	
	public ActionRedo getRedoAction() {
		return this.redoAction;
	}
	
	public void initializeMaps(JComponent component) {
		ActionMap amap = component.getActionMap();
		amap.put("undo", this.undoAction);
		amap.put("redo", this.redoAction);
		
		InputMap imap = component.getInputMap();
		imap.put(KeyStroke.getKeyStroke(
				KeyEvent.VK_Z,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "undo");
		imap.put(KeyStroke.getKeyStroke(
				KeyEvent.VK_Y,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "redo");
	}
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		this.editSupport.postEdit(e.getEdit());
	}
	
}
