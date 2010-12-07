package com.leclercb.taskunifier.gui.components.searcheredit.searcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.leclercb.taskunifier.gui.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.translations.Translations;

public class TaskSearcherPanel extends JPanel {

	private TaskSearcher searcher;

	public TaskSearcherPanel(TaskSearcher searcher) {
		this.searcher = searcher;

		this.initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		final JTextField title = new JTextField(this.searcher.getTitle());
		title.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				searcher.setTitle(title.getText());
			}

		});

		panel.add(new JLabel(Translations.getString("searcheredit.searcher.title")), BorderLayout.WEST);
		panel.add(title, BorderLayout.CENTER);

		this.add(panel, BorderLayout.CENTER);
	}

}
