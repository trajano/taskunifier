package lu.tudor.santec.bizcal.views.weeklist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import bizcal.common.CalendarViewConfig;
import bizcal.common.Event;

public class WeekList extends JPanel {
	
	private CalendarViewConfig config;
	
	private JLabel label;
	private JList list;
	
	public WeekList(CalendarViewConfig config) {
		this.config = config;
		
		this.label = new JLabel();
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.label.setBorder(BorderFactory.createMatteBorder(
				0,
				0,
				1,
				0,
				Color.LIGHT_GRAY));
		
		this.list = new JList() {
			
			@Override
			public String getToolTipText(MouseEvent evt) {
				int index = this.locationToIndex(evt.getPoint());
				
				if (index == -1)
					return null;
				
				Event item = (Event) this.getModel().getElementAt(index);
				
				if (item == null)
					return null;
				
				return item.getToolTip();
			}
			
		};
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.list.setCellRenderer(new WeekListRenderer(config));
		this.list.setModel(new DefaultListModel());
		
		this.setLayout(new BorderLayout());
		this.add(this.label, BorderLayout.NORTH);
		this.add(this.list, BorderLayout.CENTER);
		
		this.setBorder(BorderFactory.createMatteBorder(
				0,
				0,
				0,
				1,
				Color.LIGHT_GRAY));
	}
	
	public JList getList() {
		return this.list;
	}
	
	public void setEvents(List events, Date date) {
		this.label.setText(this.config.getDayFormat().format(date));
		
		DefaultListModel model = (DefaultListModel) this.list.getModel();
		model.removeAllElements();
		
		for (Object object : events) {
			model.addElement(object);
		}
	}
	
}
