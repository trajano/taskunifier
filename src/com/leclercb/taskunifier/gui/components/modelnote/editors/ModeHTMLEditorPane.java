package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
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
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.properties.events.SavePropertiesListener;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.commons.values.StringValueCalendar;
import com.leclercb.taskunifier.gui.components.help.Help;
import com.leclercb.taskunifier.gui.components.modelnote.HTMLEditorInterface;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.swing.TUFileDialog;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.DesktopUtils;
import com.leclercb.taskunifier.gui.utils.ImageUtils;
import com.leclercb.taskunifier.gui.utils.ProtocolUtils;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public class ModeHTMLEditorPane extends JPanel implements HTMLEditorInterface {
	
	private ActionSupport actionSupport;
	
	private UndoSupport undoSupport;
	
	private JXEditorPane htmlNote;
	private JTextArea textNote;
	private Action editAction;
	private boolean flagSetText;
	
	public ModeHTMLEditorPane(String text, boolean canEdit, String propertyName) {
		this.actionSupport = new ActionSupport(this);
		this.initialize(text, canEdit, propertyName);
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}
	
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
	
	private void initialize(
			String text,
			boolean canEdit,
			final String propertyName) {
		this.setOpaque(false);
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
		
		toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setOpaque(false);
		toolBar.setFloatable(false);
		
		this.editAction = new AbstractAction("", ImageUtils.getResourceImage(
				"edit.png",
				16,
				16)) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ModeHTMLEditorPane.this.edit();
			}
			
		};
		
		this.editAction.putValue(
				Action.SHORT_DESCRIPTION,
				Translations.getString("modelnote.action.edit_note"));
		
		toolBar.add(this.editAction);
		toolBar.add(Help.getHelpButton("task_note"));
		
		if (propertyName != null) {
			toolBar.addSeparator();
			toolBar.add(this.createFontSizeComboBox(this.htmlNote));
		}
		
		JPanel htmlPanel = new JPanel(new BorderLayout());
		htmlPanel.setOpaque(false);
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
				if (ModeHTMLEditorPane.this.flagSetText)
					return;
				
				actionSupport.fireActionPerformed(0, ACTION_TEXT_CHANGED);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (ModeHTMLEditorPane.this.flagSetText)
					return;
				
				actionSupport.fireActionPerformed(0, ACTION_TEXT_CHANGED);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (ModeHTMLEditorPane.this.flagSetText)
					return;
				
				actionSupport.fireActionPerformed(0, ACTION_TEXT_CHANGED);
			}
			
		});
		
		if (propertyName != null) {
			float textFontSize = Main.getSettings().getFloatProperty(
					propertyName + ".text.font_size",
					(float) this.textNote.getFont().getSize());
			this.textNote.setFont(this.textNote.getFont().deriveFont(
					textFontSize));
		}
		
		toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setOpaque(false);
		toolBar.setFloatable(false);
		
		AbstractAction viewAction = new AbstractAction(
				"",
				ImageUtils.getResourceImage("previous.png", 16, 16)) {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				ModeHTMLEditorPane.this.view();
			}
			
		};
		
		viewAction.putValue(
				Action.SHORT_DESCRIPTION,
				Translations.getString("modelnote.action.view_note"));
		
		toolBar.add(viewAction);
		
		toolBar.addSeparator();
		
		toolBar.add(this.undoSupport.getUndoAction());
		toolBar.add(this.undoSupport.getRedoAction());
		
		toolBar.addSeparator();
		
		toolBar.add(new ModeInsertHTMLTextAction(
				this.textNote,
				"html_b.png",
				Translations.getString("modelnote.action.b"),
				"<b>|</b>"));
		
		toolBar.add(new ModeInsertHTMLTextAction(
				this.textNote,
				"html_i.png",
				Translations.getString("modelnote.action.i"),
				"<i>|</i>"));
		
		toolBar.add(new ModeInsertHTMLTextAction(
				this.textNote,
				"html_ul.png",
				Translations.getString("modelnote.action.ul"),
				"\n<ul>\n<li>|</li>\n</ul>"));
		
		toolBar.add(new ModeInsertHTMLTextAction(
				this.textNote,
				"html_ol.png",
				Translations.getString("modelnote.action.ol"),
				"\n<ol>\n<li>|</li>\n</ol>"));
		
		toolBar.add(new ModeInsertHTMLTextAction(
				this.textNote,
				"html_li.png",
				Translations.getString("modelnote.action.li"),
				"\n<li>|</li>"));
		
		toolBar.add(new ModeInsertHTMLTextAction(
				this.textNote,
				"html_a.png",
				Translations.getString("modelnote.action.a"),
				"<a href=\"\">|</a>") {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				TUFileDialog dialog = new TUFileDialog(
						true,
						Translations.getString("general.link"));
				dialog.setFile("http://");
				dialog.setVisible(true);
				
				if (dialog.isCancelled()) {
					ModeHTMLEditorPane.this.textNote.requestFocus();
					return;
				}
				
				String url = dialog.getFile();
				
				try {
					File file = new File(url);
					if (file.exists())
						url = file.toURI().toURL().toExternalForm();
				} catch (Throwable t) {
					
				}
				
				this.setText("<a href=\"" + url + "\">|</a>");
				super.actionPerformed(event);
			}
			
		});
		
		toolBar.add(new ModeInsertHTMLTextAction(
				this.textNote,
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
			toolBar.addSeparator();
			toolBar.add(this.createFontSizeComboBox(this.textNote));
		}
		
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.setOpaque(false);
		textPanel.add(toolBar, BorderLayout.NORTH);
		textPanel.add(
				ComponentFactory.createJScrollPane(this.textNote, false),
				BorderLayout.CENTER);
		
		this.add(htmlPanel, "" + 0);
		this.add(textPanel, "" + 1);
		
		// Properties
		if (propertyName != null) {
			Main.getSettings().addSavePropertiesListener(
					new SavePropertiesListener() {
						
						@Override
						public void saveProperties() {
							Main.getSettings().setFloatProperty(
									propertyName + ".html.font_size",
									(float) ModeHTMLEditorPane.this.htmlNote.getFont().getSize());
							Main.getSettings().setFloatProperty(
									propertyName + ".text.font_size",
									(float) ModeHTMLEditorPane.this.textNote.getFont().getSize());
						}
						
					});
		}
		
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
	
	@Override
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
}
