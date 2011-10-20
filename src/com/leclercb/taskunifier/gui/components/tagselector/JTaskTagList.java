package com.leclercb.taskunifier.gui.components.tagselector;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.gui.swing.TUCheckBoxList;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;
import com.leclercb.taskunifier.gui.utils.Images;

public class JTaskTagList extends JPanel {
	
	private JTextField text;
	private JButton button;
	private JPopupMenu popup;
	private TUCheckBoxList list;
	private TaskTagListModel model;
	
	public JTaskTagList() {
		this.initialize();
	}
	
	public TagList getTags() {
		return TagList.fromString(this.text.getText());
	}
	
	public void setTags(String tags) {
		this.text.setText(tags);
	}
	
	public void setTags(TagList tags) {
		if (tags == null)
			this.text.setText("");
		else
			this.text.setText(tags.toString());
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.text.setEnabled(enabled);
		this.button.setEnabled(enabled);
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.popup = new JPopupMenu();
		
		this.text = new JTextField();
		
		this.button = new JButton(Images.getResourceImage("edit.png", 16, 16));
		this.button.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				String text = JTaskTagList.this.text.getText();
				String[] tags = text.split(",");
				for (int i = 0; i < tags.length; i++) {
					tags[i] = tags[i].trim();
				}
				
				JTaskTagList.this.model.updateCheckBoxStates(tags);
				
				JTaskTagList.this.popup.show(
						JTaskTagList.this.button,
						e.getX(),
						e.getY());
			}
			
		});
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.list = new TUCheckBoxList();
		this.model = new TaskTagListModel();
		this.list.setModel(this.model);
		
		this.model.addListChangeListener(new ListChangeListener() {
			
			@Override
			public void listChange(ListChangeEvent event) {
				Tag tag = (Tag) event.getValue();
				TagList tags = TagList.fromString(JTaskTagList.this.text.getText());
				
				if (event.getChangeType() == ListChangeEvent.VALUE_ADDED) {
					tags.addTag(tag);
				} else if (event.getChangeType() == ListChangeEvent.VALUE_REMOVED) {
					tags.removeTag(tag);
				}
				
				JTaskTagList.this.text.setText(tags.toString());
			}
			
		});
		
		panel.add(ComponentFactory.createJScrollPane(this.list, false));
		
		this.popup.add(panel);
		
		this.add(this.text, BorderLayout.CENTER);
		this.add(this.button, BorderLayout.EAST);
	}
	
}
