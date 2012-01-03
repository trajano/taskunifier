package com.leclercb.taskunifier.gui.components.modelnote;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.swing.TUFileDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.DesktopUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public abstract class HTMLEditorPane extends JPanel {
	
	private UndoSupport undoSupport;
	
	private JXEditorPane htmlNote;
	private JTextArea textNote;
	private Action editAction;
	private boolean flagSetText;
	
	public HTMLEditorPane(String text, boolean canEdit) {
		this.initialize(text, canEdit);
	}
	
	public abstract void textChanged(String text);
	
	public String getText() {
		return this.textNote.getText();
	}
	
	public void setText(String text, boolean canEdit, boolean discardAllEdits) {
		this.htmlNote.setText(Text2HTML.convert(text));
		this.htmlNote.setEnabled(canEdit);
		this.editAction.setEnabled(canEdit);
		
		this.flagSetText = true;
		this.textNote.setText(StringEscapeUtils.unescapeHtml4(text));
		this.flagSetText = false;
		
		if (discardAllEdits) {
			this.textNote.setCaretPosition(this.textNote.getText().length());
			this.undoSupport.discardAllEdits();
		}
		
		if (!canEdit)
			this.view();
	}
	
	public boolean edit() {
		if (!this.htmlNote.isEnabled())
			return false;
		
		((CardLayout) this.getLayout()).last(this);
		this.textNote.requestFocus();
		return true;
	}
	
	public void view() {
		((CardLayout) this.getLayout()).first(this);
		this.htmlNote.setText(Text2HTML.convert(this.getText()));
	}
	
	private void initialize(String text, boolean canEdit) {
		this.setLayout(new CardLayout());
		
		this.undoSupport = new UndoSupport();
		
		JToolBar toolBar = null;
		
		this.htmlNote = new JXEditorPane();
		this.addContextMenu(this.htmlNote);
		this.htmlNote.setEditable(false);
		this.htmlNote.setContentType("text/html");
		this.htmlNote.setFont(UIManager.getFont("Label.font"));
		
		this.htmlNote.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					DesktopUtils.browse(evt.getURL().toExternalForm());
				}
			}
			
		});
		
		toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		
		this.editAction = new AbstractAction("", ImageUtils.getResourceImage(
				"edit.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				HTMLEditorPane.this.edit();
			}
			
		};
		
		toolBar.add(this.editAction);
		toolBar.add(Help.getHelpButton("task_note"));
		
		JPanel htmlPanel = new JPanel(new BorderLayout());
		htmlPanel.add(toolBar, BorderLayout.NORTH);
		htmlPanel.add(
				ComponentFactory.createJScrollPane(this.htmlNote, false),
				BorderLayout.CENTER);
		
		this.textNote = new JTextArea();
		this.addContextMenu(this.textNote);
		this.textNote.setLineWrap(true);
		this.textNote.setWrapStyleWord(true);
		this.textNote.setBorder(BorderFactory.createEmptyBorder());
		this.textNote.getDocument().addUndoableEditListener(this.undoSupport);
		this.undoSupport.initializeMaps(this.textNote);
		
		this.textNote.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (HTMLEditorPane.this.flagSetText)
					return;
				
				HTMLEditorPane.this.textChanged(HTMLEditorPane.this.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (HTMLEditorPane.this.flagSetText)
					return;
				
				HTMLEditorPane.this.textChanged(HTMLEditorPane.this.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (HTMLEditorPane.this.flagSetText)
					return;
				
				HTMLEditorPane.this.textChanged(HTMLEditorPane.this.getText());
			}
			
		});
		
		toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		
		toolBar.add(new AbstractAction("", ImageUtils.getResourceImage(
				"previous.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				HTMLEditorPane.this.view();
			}
			
		});
		
		toolBar.addSeparator();
		
		toolBar.add(this.undoSupport.getUndoAction());
		toolBar.add(this.undoSupport.getRedoAction());
		
		toolBar.addSeparator();
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_b.png",
				"<b>|</b>"));
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_i.png",
				"<i>|</i>"));
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_ul.png",
				"\n<ul>\n<li>|</li>\n</ul>"));
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_ol.png",
				"\n<ol>\n<li>|</li>\n</ol>"));
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_li.png",
				"\n<li>|</li>"));
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"html_a.png",
				"<a href=\"\">|</a>") {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				TUFileDialog dialog = new TUFileDialog(
						true,
						Translations.getString("general.link"));
				dialog.setFile("http://");
				dialog.setVisible(true);
				
				if (dialog.isCancelled()) {
					HTMLEditorPane.this.textNote.requestFocus();
					return;
				}
				
				String url = dialog.getFile();
				
				try {
					File file = new File(url);
					if (file.exists())
						url = file.toURI().toURL().toExternalForm();
				} catch (Throwable t) {
					
				}
				
				this.setContent("<a href=\"" + url + "\">|</a>");
				super.actionPerformed(event);
			}
			
		});
		
		toolBar.add(new HTMLInsertContentAction(
				this.textNote,
				"calendar.png",
				StringValueCalendar.INSTANCE_DATE_TIME.getString(Calendar.getInstance())));
		
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(toolBar, BorderLayout.NORTH);
		textPanel.add(
				ComponentFactory.createJScrollPane(this.textNote, false),
				BorderLayout.CENTER);
		
		this.add(htmlPanel, "" + 0);
		this.add(textPanel, "" + 1);
		
		this.setText(text, canEdit, true);
		this.view();
	}
	
	private void addContextMenu(JComponent component) {
		JPopupMenu menu = new JPopupMenu();
		
		menu.add(new ActionCopy(16, 16));
		menu.add(new ActionCut(16, 16));
		menu.add(new ActionPaste(16, 16));
		
		component.addMouseListener(new PopupTriggerMouseListener(
				menu,
				component));
	}
	
	private static class PopupTriggerMouseListener extends MouseAdapter {
		
		private JPopupMenu popup;
		private JComponent component;
		
		public PopupTriggerMouseListener(JPopupMenu popup, JComponent component) {
			CheckUtils.isNotNull(popup);
			CheckUtils.isNotNull(component);
			
			this.popup = popup;
			this.component = component;
		}
		
		private void showMenuIfPopupTrigger(MouseEvent e) {
			if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
				this.component.requestFocus();
				this.popup.show(this.component, e.getX() + 3, e.getY() + 3);
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			this.showMenuIfPopupTrigger(e);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			this.showMenuIfPopupTrigger(e);
		}
		
	}
	
}
