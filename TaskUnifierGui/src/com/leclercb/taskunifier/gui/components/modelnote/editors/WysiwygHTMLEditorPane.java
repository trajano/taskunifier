package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Calendar;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTML;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.commons.api.event.propertychange.PropertyChangeSupport;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.components.modelnote.HTMLEditorInterface;
import com.leclercb.taskunifier.gui.components.modelnote.converters.HTML2Text;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TULinkDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.DesktopUtils;
import com.leclercb.taskunifier.gui.utils.ProtocolUtils;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public class WysiwygHTMLEditorPane extends JPanel implements HTMLEditorInterface {
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private UndoSupport undoSupport;
	
	private JToolBar toolBar;
	private JXEditorPane htmlNote;
	private boolean flagSetText;
	
	public WysiwygHTMLEditorPane(
			String text,
			boolean canEdit,
			String propertyName) {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.initialize(text, canEdit, propertyName);
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}
	
	@Override
	public String getText() {
		return HTML2Text.convert(this.htmlNote.getText());
	}
	
	@Override
	public void setText(String text) {
		this.setText(text, true, false);
	}
	
	@Override
	public void setText(String text, boolean canEdit, boolean discardAllEdits) {
		if (WysiwygHTMLEditorPane.this.flagSetText)
			return;
		
		this.toolBar.setVisible(canEdit);
		this.htmlNote.setEnabled(canEdit);
		
		this.flagSetText = true;
		this.htmlNote.setText(Text2HTML.convert(text));
		this.flagSetText = false;
		
		if (discardAllEdits) {
			this.undoSupport.discardAllEdits();
			this.htmlNote.requestFocus();
		}
		
		if (!canEdit)
			this.view();
	}
	
	@Override
	public boolean edit() {
		if (!this.htmlNote.isEnabled())
			return false;
		
		this.htmlNote.requestFocus();
		return true;
	}
	
	@Override
	public void view() {
		
	}
	
	private void initialize(String text, boolean canEdit, String propertyName) {
		this.setLayout(new BorderLayout());
		
		this.undoSupport = new UndoSupport();
		
		this.htmlNote = new JXEditorPane();
		this.addContextMenu(this.htmlNote);
		this.htmlNote.setEditable(true);
		this.htmlNote.setContentType("text/html");
		this.htmlNote.setEditorKit(new WysiwygHTMLEditorKit());
		this.htmlNote.setFont(UIManager.getFont("Label.font"));
		this.htmlNote.getDocument().addUndoableEditListener(this.undoSupport);
		this.undoSupport.initializeMaps(this.htmlNote);
		
		this.htmlNote.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				WysiwygHTMLEditorPane.this.propertyChangeSupport.firePropertyChange(
						PROP_TEXT,
						null,
						WysiwygHTMLEditorPane.this.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				WysiwygHTMLEditorPane.this.propertyChangeSupport.firePropertyChange(
						PROP_TEXT,
						null,
						WysiwygHTMLEditorPane.this.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				WysiwygHTMLEditorPane.this.propertyChangeSupport.firePropertyChange(
						PROP_TEXT,
						null,
						WysiwygHTMLEditorPane.this.getText());
			}
			
		});
		
		this.htmlNote.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String url = ProtocolUtils.urlToString(evt.getURL());
					DesktopUtils.browse(url);
				}
			}
			
		});
		
		if (propertyName != null) {
			float htmlFontSize = Main.getSettings().getFloatProperty(
					propertyName + ".html.font_size",
					(float) this.htmlNote.getFont().getSize());
			this.htmlNote.setFont(this.htmlNote.getFont().deriveFont(
					htmlFontSize));
		}
		
		this.toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		this.toolBar.setFloatable(false);
		
		this.toolBar.add(this.undoSupport.getUndoAction());
		this.toolBar.add(this.undoSupport.getRedoAction());
		
		this.toolBar.addSeparator();
		
		this.toolBar.add(new WysiwygInsertHTMLAction(
				this.htmlNote,
				"html_b.png",
				Translations.getString("modelnote.action.b"),
				this.getAction("font-bold")));
		
		this.toolBar.add(new WysiwygInsertHTMLAction(
				this.htmlNote,
				"html_i.png",
				Translations.getString("modelnote.action.i"),
				this.getAction("font-italic")));
		
		this.toolBar.add(new WysiwygInsertHTMLAction(
				this.htmlNote,
				"html_ul.png",
				Translations.getString("modelnote.action.ul"),
				this.getAction("InsertUnorderedList")));
		
		this.toolBar.add(new WysiwygInsertHTMLAction(
				this.htmlNote,
				"html_li.png",
				Translations.getString("modelnote.action.li"),
				this.getAction("InsertUnorderedListItem")));
		
		this.toolBar.add(new WysiwygInsertHTMLAction(
				this.htmlNote,
				"html_ol.png",
				Translations.getString("modelnote.action.ol"),
				this.getAction("InsertOrderedList")));
		
		this.toolBar.add(new WysiwygInsertHTMLAction(
				this.htmlNote,
				"html_li.png",
				Translations.getString("modelnote.action.li"),
				this.getAction("InsertOrderedListItem")));
		
		this.toolBar.add(new WysiwygInsertHTMLTextAction(
				this.htmlNote,
				"html_a.png",
				Translations.getString("modelnote.action.a"),
				"",
				HTML.Tag.A) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				TULinkDialog dialog = new TULinkDialog(
						true,
						Translations.getString("general.link"));
				dialog.setFile("http://");
				dialog.setVisible(true);
				
				if (dialog.isCancelled()) {
					WysiwygHTMLEditorPane.this.htmlNote.requestFocus();
					return;
				}
				
				String url = dialog.getFile();
				String label = dialog.getLabel();
				
				try {
					File file = new File(url);
					if (file.exists())
						url = file.toURI().toURL().toExternalForm();
				} catch (Throwable t) {
					
				}
				
				if (label == null || label.length() == 0) {
					label = dialog.getFile();
				}
				
				this.setHtml("<a href=\""
						+ StringEscapeUtils.escapeHtml4(url)
						+ "\">"
						+ StringEscapeUtils.escapeHtml4(label)
						+ "</a>");
				super.actionPerformed(event);
			}
			
		});
		
		this.toolBar.add(new WysiwygInsertTextAction(
				this.htmlNote,
				"calendar.png",
				Translations.getString("modelnote.action.date"),
				StringValueCalendar.INSTANCE_DATE_TIME.getString(Calendar.getInstance())) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				this.setText(StringValueCalendar.INSTANCE_DATE_TIME.getString(Calendar.getInstance()));
				super.actionPerformed(event);
			}
			
		});
		
		if (propertyName != null) {
			this.toolBar.addSeparator();
			this.toolBar.add(this.createFontSizeComboBox(this.htmlNote));
		}
		
		this.add(this.toolBar, BorderLayout.NORTH);
		this.add(
				ComponentFactory.createJScrollPane(this.htmlNote, false),
				BorderLayout.CENTER);
		
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
	
	private JComponent createFontSizeComboBox(final JTextComponent component) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		
		final JComboBox cb = new JComboBox(new Integer[] {
				8,
				9,
				10,
				11,
				12,
				13,
				14,
				15,
				16,
				17,
				18,
				19,
				20 });
		cb.setSelectedItem(component.getFont().getSize());
		
		cb.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent evt) {
				Integer fontSize = (Integer) cb.getSelectedItem();
				component.setFont(component.getFont().deriveFont(
						(float) fontSize));
			}
			
		});
		
		cb.setPrototypeDisplayValue("0000");
		cb.setToolTipText(Translations.getString("modelnote.action.font_size"));
		
		panel.add(cb, BorderLayout.WEST);
		
		return panel;
	}
	
	public Action getAction(String name) {
		for (Action action : this.htmlNote.getActions()) {
			if (EqualsUtils.equals(name, action.getValue(Action.NAME)))
				return action;
		}
		
		return null;
	}
	
	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}
	
	@Override
	public void addPropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(
				propertyName,
				listener);
	}
	
	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(
			String propertyName,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				propertyName,
				listener);
	}
	
}
