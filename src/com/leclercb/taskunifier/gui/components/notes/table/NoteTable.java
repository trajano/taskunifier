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
package com.leclercb.taskunifier.gui.components.notes.table;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SortOrder;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXTable;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.Note;
import com.leclercb.taskunifier.gui.actions.ActionDelete;
import com.leclercb.taskunifier.gui.api.searchers.NoteSearcher;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionChangeSupport;
import com.leclercb.taskunifier.gui.commons.events.ModelSelectionListener;
import com.leclercb.taskunifier.gui.commons.events.NoteSearcherSelectionChangeEvent;
import com.leclercb.taskunifier.gui.commons.highlighters.AlternateHighlighter;
import com.leclercb.taskunifier.gui.components.notes.NoteColumn;
import com.leclercb.taskunifier.gui.components.notes.NoteTableView;
import com.leclercb.taskunifier.gui.components.notes.table.draganddrop.NoteTransferHandler;
import com.leclercb.taskunifier.gui.components.notes.table.highlighters.NoteTitleHighlightPredicate;
import com.leclercb.taskunifier.gui.components.notes.table.highlighters.NoteTitleHighlighter;
import com.leclercb.taskunifier.gui.components.notes.table.highlighters.NoteTooltipHighlightPredicate;
import com.leclercb.taskunifier.gui.components.notes.table.highlighters.NoteTooltipHighlighter;
import com.leclercb.taskunifier.gui.components.notes.table.menu.NoteTableMenu;
import com.leclercb.taskunifier.gui.components.notes.table.sorter.NoteRowComparator;
import com.leclercb.taskunifier.gui.components.notes.table.sorter.NoteRowFilter;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public class NoteTable extends JXTable implements NoteTableView {
	
	private UndoSupport undoSupport;
	
	private ModelSelectionChangeSupport noteSelectionChangeSupport;
	
	private NoteTableMenu noteTableMenu;
	
	public NoteTable() {
		this.undoSupport = new UndoSupport();
		this.noteSelectionChangeSupport = new ModelSelectionChangeSupport(this);
		this.initialize();
	}
	
	public Note getNote(int row) {
		try {
			int index = this.getRowSorter().convertRowIndexToModel(row);
			return ((NoteTableModel) this.getModel()).getNote(index);
		} catch (IndexOutOfBoundsException exc) {
			return null;
		}
	}
	
	@Override
	public Note[] getSelectedNotes() {
		int[] indexes = this.getSelectedRows();
		
		List<Note> notes = new ArrayList<Note>();
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1) {
				Note note = this.getNote(indexes[i]);
				
				if (note != null)
					notes.add(note);
			}
		}
		
		return notes.toArray(new Note[0]);
	}
	
	@Override
	public void setSelectedNotes(Note[] notes) {
		NoteTableModel model = (NoteTableModel) this.getModel();
		
		this.getSelectionModel().setValueIsAdjusting(true);
		this.getSelectionModel().clearSelection();
		
		int firstRowIndex = -1;
		for (Note note : notes) {
			for (int i = 0; i < model.getRowCount(); i++) {
				if (note.equals(model.getNote(i))) {
					int index = this.getRowSorter().convertRowIndexToView(i);
					
					if (index != -1) {
						this.getSelectionModel().addSelectionInterval(
								index,
								index);
						
						if (firstRowIndex == -1)
							firstRowIndex = index;
					}
				}
			}
		}
		
		this.getSelectionModel().setValueIsAdjusting(false);
		
		if (firstRowIndex != -1)
			this.scrollRowToVisible(firstRowIndex);
	}
	
	@Override
	public void setSelectedNoteAndStartEdit(Note note) {
		this.setSelectedNotes(new Note[] { note });
		
		NoteTableColumnModel columnModel = (NoteTableColumnModel) this.getColumnModel();
		NoteTableModel model = (NoteTableModel) this.getModel();
		
		for (int i = 0; i < model.getRowCount(); i++) {
			if (note.equals(model.getNote(i))) {
				int row = this.getRowSorter().convertRowIndexToView(i);
				int col = columnModel.getColumnIndex(NoteColumn.TITLE);
				
				if (row != -1) {
					if (this.editCellAt(row, col)) {
						Component editor = this.getEditorComponent();
						editor.requestFocusInWindow();
					}
				}
				
				break;
			}
		}
	}
	
	@Override
	public void refreshNotes() {
		this.getRowSorter().allRowsChanged();
	}
	
	public NoteSearcher getNoteSearcher() {
		return NoteRowComparator.getInstance().getNoteSearcher();
	}
	
	public void setNoteSearcher(NoteSearcher searcher) {
		CheckUtils.isNotNull(searcher, "Note searcher cannot be null");
		
		NoteRowComparator.getInstance().setNoteSearcher(searcher);
		
		this.setSortOrder(NoteColumn.MODEL, SortOrder.ASCENDING);
		this.getSortController().setRowFilter(
				new NoteRowFilter(searcher.getFilter()));
		this.refreshNotes();
	}
	
	@Override
	public void printNotes() throws HeadlessException, PrinterException {
		PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
		attributes.add(new JobName(Constants.TITLE, null));
		attributes.add(OrientationRequested.LANDSCAPE);
		
		this.print(PrintMode.FIT_WIDTH, new MessageFormat(Constants.TITLE
				+ " - Notes"), new MessageFormat(this.getRowCount()
				+ " notes | Page - {0}"), true, attributes, true);
	}
	
	@Override
	public void pasteNote() {
		TransferHandler.getPasteAction().actionPerformed(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		
		this.commitChanges();
	}
	
	@Override
	public void commitChanges() {
		if (this.getCellEditor() != null)
			this.getCellEditor().stopCellEditing();
	}
	
	@Override
	public void addModelSelectionChangeListener(ModelSelectionListener listener) {
		this.noteSelectionChangeSupport.addModelSelectionChangeListener(listener);
	}
	
	@Override
	public void removeModelSelectionChangeListener(
			ModelSelectionListener listener) {
		this.noteSelectionChangeSupport.removeModelSelectionChangeListener(listener);
	}
	
	@Override
	public void noteSearcherSelectionChange(
			NoteSearcherSelectionChangeEvent event) {
		if (event.getSelectedNoteSearcher() != null)
			this.setNoteSearcher(event.getSelectedNoteSearcher());
	}
	
	private void initialize() {
		this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		NoteTableColumnModel columnModel = new NoteTableColumnModel();
		NoteTableModel tableModel = new NoteTableModel(this.undoSupport);
		
		this.setModel(tableModel);
		this.setColumnModel(columnModel);
		this.setRowHeight(24);
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.FALSE);
		
		this.setSortable(true);
		this.setSortsOnUpdates(true);
		this.setSortOrderCycle(SortOrder.ASCENDING);
		this.setColumnControlVisible(true);
		
		this.initializeDeleteNote();
		this.initializeNoteTableMenu();
		this.initializeDragAndDrop();
		this.initializeUndoRedo();
		this.initializeCopyAndPaste();
		this.initializeHighlighters();
		
		this.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting())
							NoteTable.this.noteSelectionChangeSupport.fireModelSelectionChange(NoteTable.this.getSelectedNotes());
					}
					
				});
	}
	
	private void initializeDeleteNote() {
		this.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE)
					ActionDelete.delete();
			}
			
		});
	}
	
	private void initializeNoteTableMenu() {
		this.noteTableMenu = new NoteTableMenu(this);
		
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent event) {
				// Or BUTTON3 due to a bug with OSX
				if (event.isPopupTrigger()
						|| event.getButton() == MouseEvent.BUTTON3) {
					int rowIndex = NoteTable.this.getRowSorter().convertRowIndexToModel(
							NoteTable.this.rowAtPoint(event.getPoint()));
					
					Note note = ((NoteTableModel) NoteTable.this.getModel()).getNote(rowIndex);
					
					if (note == null)
						return;
					
					NoteTable.this.commitChanges();
					
					boolean found = false;
					Note[] selectedNotes = NoteTable.this.getSelectedNotes();
					for (Note selectedNote : selectedNotes) {
						if (selectedNote.equals(note)) {
							found = true;
							break;
						}
					}
					
					if (!found)
						NoteTable.this.setSelectedNotes(new Note[] { note });
					
					NoteTable.this.noteTableMenu.show(
							event.getComponent(),
							event.getX(),
							event.getY());
				}
			}
			
		});
	}
	
	private void initializeDragAndDrop() {
		this.setDragEnabled(true);
		this.setTransferHandler(new NoteTransferHandler());
	}
	
	private void initializeUndoRedo() {
		this.undoSupport.initializeMaps(this);
	}
	
	private void initializeCopyAndPaste() {
		ActionMap amap = this.getActionMap();
		amap.put(
				TransferHandler.getCutAction().getValue(Action.NAME),
				TransferHandler.getCutAction());
		amap.put(
				TransferHandler.getCopyAction().getValue(Action.NAME),
				TransferHandler.getCopyAction());
		amap.put(
				TransferHandler.getPasteAction().getValue(Action.NAME),
				TransferHandler.getPasteAction());
		
		InputMap imap = this.getInputMap();
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_X,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getCutAction().getValue(Action.NAME));
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_C,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getCopyAction().getValue(Action.NAME));
		imap.put(
				KeyStroke.getKeyStroke(
						KeyEvent.VK_V,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				TransferHandler.getPasteAction().getValue(Action.NAME));
	}
	
	private void initializeHighlighters() {
		this.setHighlighters(
				new AlternateHighlighter(),
				new NoteTitleHighlighter(new NoteTitleHighlightPredicate()),
				new NoteTooltipHighlighter(new NoteTooltipHighlightPredicate()));
	}
	
}
