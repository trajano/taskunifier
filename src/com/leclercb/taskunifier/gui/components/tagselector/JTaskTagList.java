package com.leclercb.taskunifier.gui.components.tagselector;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.leclercb.commons.api.event.listchange.ListChangeEvent;
import com.leclercb.commons.api.event.listchange.ListChangeListener;
import com.leclercb.taskunifier.api.models.Tag;
import com.leclercb.taskunifier.api.models.TagList;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.JCheckBoxList;
import com.leclercb.taskunifier.gui.utils.ComponentFactory;

public class JTaskTagList extends JPanel {
	
	private JTextField text;
	private JButton button;
	private JDialog popup;
	private JCheckBoxList list;
	private TaskTagListModel model;
	
	public JTaskTagList() {
		this.initialize();
	}
	
	public TagList getTags() {
		return TagList.fromString(this.text.getText());
	}
	
	public void setTags(String tags) {
		this.popup.setVisible(false);
		this.text.setText(tags);
	}
	
	public void setTags(TagList tags) {
		this.popup.setVisible(false);
		
		if (tags == null)
			this.text.setText("");
		else
			this.text.setText(tags.toString());
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		
		this.text = new JTextField();
		
		this.initializePopupWindow();
		
		this.button = new JButton("X");
		this.button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				JDialog popup = JTaskTagList.this.popup;
				if (!popup.isVisible()) {
					String text = JTaskTagList.this.text.getText();
					String[] tags = text.split(",");
					for (int i = 0; i < tags.length; i++) {
						tags[i] = tags[i].trim();
					}
					
					JTaskTagList.this.model.updateCheckBoxStates(tags);
					
					popup.setLocationRelativeTo(JTaskTagList.this.button);
					popup.setVisible(true);
					popup.requestFocus();
				}
			}
			
		});
		
		this.list = new JCheckBoxList();
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
		
		this.popup.add(
				ComponentFactory.createJScrollPane(this.list, false),
				BorderLayout.CENTER);
		
		this.add(this.text, BorderLayout.CENTER);
		this.add(this.button, BorderLayout.EAST);
	}
	
	private void initializePopupWindow() {
		this.popup = new JDialog(MainFrame.getInstance().getFrame());
		this.popup.setAlwaysOnTop(true);
		this.popup.setModal(true);
		this.popup.setUndecorated(true);
		this.popup.setSize(150, 200);
		this.popup.setResizable(false);
		this.popup.setLayout(new BorderLayout());
		this.popup.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		this.popup.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowGainedFocus(WindowEvent e) {

			}
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				JTaskTagList.this.popup.setVisible(false);
			}
			
		});
	}
	
}
