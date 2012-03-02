package lu.tudor.santec.bizcal.views.weeklist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import bizcal.common.CalendarViewConfig;
import bizcal.common.Event;

public class WeekListRenderer implements ListCellRenderer {
	
	private CalendarViewConfig config;
	
	private JPanel panel;
	private JLabel dates;
	private JTextArea title;
	
	public WeekListRenderer(CalendarViewConfig config) {
		this.config = config;
		
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		
		this.dates = new JLabel();
		this.title = new JTextArea();
		
		this.dates.setOpaque(false);
		this.title.setOpaque(false);
		
		this.title.setEditable(false);
		this.title.setLineWrap(true);
		this.title.setWrapStyleWord(true);
		this.title.setBorder(BorderFactory.createEmptyBorder());
		
		if (config.isShowTime())
			this.panel.add(this.dates, BorderLayout.NORTH);
		else
			this.panel.add(this.dates, BorderLayout.WEST);
		
		this.panel.add(this.title, BorderLayout.CENTER);
		
		this.panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
	}
	
	@Override
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
		Event event = (Event) value;
		
		this.dates.setIcon(event.getIcon());
		
		if (this.config.isShowTime()) {
			this.dates.setText(this.config.getTimeFormat().format(
					event.getStart())
					+ " - "
					+ this.config.getTimeFormat().format(event.getEnd()));
		}
		
		this.title.setText(event.getDescription());
		
		Color background = UIManager.getColor("List.background");
		
		if (event.getColor() != null)
			background = event.getColor();
		
		if (isSelected)
			background = UIManager.getColor("List.selectionBackground");
		
		this.panel.setBackground(background);
		
		int width = list.getWidth();
		if (width > 0)
			this.title.setSize(width, Short.MAX_VALUE);
		
		return this.panel;
	}
	
}
