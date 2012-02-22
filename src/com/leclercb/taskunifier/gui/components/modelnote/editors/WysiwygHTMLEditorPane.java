package com.leclercb.taskunifier.gui.components.modelnote.editors;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.JXEditorPane;

import com.leclercb.commons.api.event.action.ActionSupport;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.actions.ActionCopy;
import com.leclercb.taskunifier.gui.actions.ActionCut;
import com.leclercb.taskunifier.gui.actions.ActionPaste;
import com.leclercb.taskunifier.gui.components.modelnote.HTMLEditorInterface;
import com.leclercb.taskunifier.gui.components.modelnote.converters.HTML2Text;
import com.leclercb.taskunifier.gui.components.modelnote.converters.Text2HTML;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.DesktopUtils;
import com.leclercb.taskunifier.gui.utils.UndoSupport;

public class WysiwygHTMLEditorPane extends JPanel implements HTMLEditorInterface {
	
	private ActionSupport actionSupport;
	
	private UndoSupport undoSupport;
	
	private JXEditorPane htmlNote;
	
	public WysiwygHTMLEditorPane(String text, boolean canEdit, String propertyName) {
		this.actionSupport = new ActionSupport(this);
		this.initialize(text, canEdit, propertyName);
	}
	
	@Override
	public JComponent getComponent() {
		return this;
	}
	
	public String getText() {
		return HTML2Text.convert(this.htmlNote.getText());
	}
	
	public void setText(String text, boolean canEdit, boolean discardAllEdits) {
		this.htmlNote.setText(Text2HTML.convert(text));
		this.htmlNote.setEnabled(canEdit);
		
		if (discardAllEdits) {
			//this.htmlNote.setCaretPosition(this.htmlNote.getCaret().);
			this.undoSupport.discardAllEdits();
			this.htmlNote.requestFocus();
		}
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
		this.htmlNote.setFont(UIManager.getFont("Label.font"));
		this.htmlNote.getDocument().addUndoableEditListener(this.undoSupport);
		this.undoSupport.initializeMaps(this.htmlNote);
		
		this.htmlNote.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				actionSupport.fireActionPerformed(0, ACTION_TEXT_CHANGED);
			}
			
		});
		
		this.htmlNote.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						DesktopUtils.browse(evt.getURL().toExternalForm());
					} catch (Exception exc) {
						
					}
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
		
		JToolBar toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		
		toolBar.addSeparator();
		
		toolBar.add(this.undoSupport.getUndoAction());
		toolBar.add(this.undoSupport.getRedoAction());
		
		toolBar.addSeparator();
		
		for (Action action : this.htmlNote.getActions()) {
			System.out.println(action.getValue(Action.NAME));
		}
		
		toolBar.add(new WysiwygHTMLInsertContentAction(
				"html_b.png",
				Translations.getString("modelnote.action.b"),
				getAction("font-bold")));
		
		toolBar.add(new WysiwygHTMLInsertContentAction(
				"html_i.png",
				Translations.getString("modelnote.action.i"),
				getAction("font-italic")));
		
		toolBar.add(new WysiwygHTMLInsertContentAction(
				"html_ul.png",
				Translations.getString("modelnote.action.ul"),
				getAction("InsertUnorderedList")));
		
		toolBar.add(new WysiwygHTMLInsertContentAction(
				"html_li.png",
				Translations.getString("modelnote.action.li"),
				getAction("InsertUnorderedListItem")));
		
		toolBar.add(new WysiwygHTMLInsertContentAction(
				"html_ol.png",
				Translations.getString("modelnote.action.ol"),
				getAction("InsertOrderedList")));
		
		toolBar.add(new WysiwygHTMLInsertContentAction(
				"html_li.png",
				Translations.getString("modelnote.action.li"),
				getAction("InsertOrderedListItem")));
		
		this.add(toolBar, BorderLayout.NORTH);
		this.add(
				ComponentFactory.createJScrollPane(this.htmlNote, false),
				BorderLayout.CENTER);
		
		if (propertyName != null) {
			toolBar.addSeparator();
			toolBar.add(this.createFontSizeComboBox(this.htmlNote));
		}
		
		this.setText(text, canEdit, true);
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
	public void addActionListener(ActionListener listener) {
		this.actionSupport.addActionListener(listener);
	}
	
	@Override
	public void removeActionListener(ActionListener listener) {
		this.actionSupport.removeActionListener(listener);
	}
	
}