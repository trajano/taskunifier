package com.leclercb.taskunifier.gui.components.searcheredit.searcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import com.leclercb.commons.api.utils.EqualsUtils;
import com.leclercb.taskunifier.gui.MainFrame;
import com.leclercb.taskunifier.gui.images.Images;
import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.FileUtils;
import com.leclercb.taskunifier.gui.utils.SpringUtils;

public class TaskSearcherPanel extends JPanel implements PropertyChangeListener {
	
	private TaskSearcher searcher;
	
	private JButton searcherIcon;
	private JTextField searcherTitle;
	
	public TaskSearcherPanel(TaskSearcher searcher) {
		this.searcher = searcher;
		
		this.searcher.addPropertyChangeListener(this);
		
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		// Icon
		JPanel iconPanel = new JPanel(new BorderLayout());
		
		this.searcherIcon = new JButton();
		iconPanel.add(this.searcherIcon, BorderLayout.CENTER);
		this.searcherIcon.setIcon(this.searcher.getIcon() == null ? Images.getResourceImage(
				"remove.png",
				24,
				24) : Images.getImage(this.searcher.getIcon(), 24, 24));
		this.searcherIcon.setText(this.searcher.getIcon() == null ? Translations.getString("searcheredit.searcher.no_icon") : this.searcher.getIcon());
		this.searcherIcon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return Translations.getString("general.images");
					}
					
					@Override
					public boolean accept(File f) {
						if (f.isDirectory())
							return true;
						
						String extention = FileUtils.getExtention(f.getName());
						
						String[] imageExtentions = new String[] {
								"jpeg",
								"jpg",
								"gif",
								"tiff",
								"tif",
								"png" };
						
						for (int i = 0; i < imageExtentions.length; i++)
							if (imageExtentions[i].equals(extention))
								return true;
						
						return false;
					}
					
				});
				
				int result = fileChooser.showOpenDialog(MainFrame.getInstance().getFrame());
				
				if (result == JFileChooser.APPROVE_OPTION)
					TaskSearcherPanel.this.searcher.setIcon(fileChooser.getSelectedFile().getAbsolutePath());
			}
			
		});
		
		final JButton searcherRemoveIcon = new JButton(Images.getResourceImage(
				"remove.png",
				16,
				16));
		iconPanel.add(searcherRemoveIcon, BorderLayout.EAST);
		searcherRemoveIcon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TaskSearcherPanel.this.searcher.setIcon(null);
			}
			
		});
		
		panel.add(new JLabel(
				Translations.getString("searcheredit.searcher.icon") + ":"));
		panel.add(iconPanel);
		
		// Title
		this.searcherTitle = new JTextField(this.searcher.getTitle());
		this.searcherTitle.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				TaskSearcherPanel.this.searcher.setTitle(TaskSearcherPanel.this.searcherTitle.getText());
			}
			
		});
		
		panel.add(new JLabel(
				Translations.getString("searcheredit.searcher.title") + ":"));
		panel.add(this.searcherTitle);
		
		// Lay out the panel
		SpringUtils.makeCompactGrid(panel, 2, 2, // rows, cols
				6,
				6, // initX, initY
				6,
				6); // xPad, yPad
		
		this.add(panel, BorderLayout.NORTH);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(TaskSearcher.PROP_TITLE)) {
			if (!EqualsUtils.equals(
					this.searcherTitle.getText(),
					evt.getNewValue()))
				this.searcherTitle.setText((String) evt.getNewValue());
		}
		
		if (evt.getPropertyName().equals(TaskSearcher.PROP_ICON)) {
			if (!EqualsUtils.equals(
					this.searcherTitle.getText(),
					evt.getNewValue())) {
				this.searcherIcon.setIcon((String) evt.getNewValue() == null ? Images.getResourceImage(
						"remove.png",
						24,
						24) : Images.getImage(
						(String) evt.getNewValue(),
						24,
						24));
				this.searcherIcon.setText((String) evt.getNewValue() == null ? Translations.getString("searcheredit.searcher.no_icon") : (String) evt.getNewValue());
			}
		}
	}
	
}
