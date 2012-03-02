package lu.tudor.santec.bizcal.views.weeklist;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import lu.tudor.santec.bizcal.views.WeekListViewPanel;
import bizcal.common.CalendarViewConfig;
import bizcal.common.Event;
import bizcal.swing.CalendarView;
import bizcal.util.DateUtil;

public class WeekListView extends CalendarView implements MouseListener {
	
	private JPanel panel;
	private WeekListViewPanel parent;
	private Date date;
	private List showEvents;
	
	private WeekList[] weekLists;
	
	public WeekListView(CalendarViewConfig config, WeekListViewPanel parent)
			throws Exception {
		super(config);
		
		this.showEvents = new ArrayList();
		
		this.parent = parent;
		
		this.panel = new JPanel();
		this.panel.setOpaque(false);
		this.panel.setLayout(new BorderLayout());
		
		JPanel listPanel = new ScrollablePanel();
		listPanel.setLayout(new GridLayout(1, 7));
		
		this.weekLists = new WeekList[7];
		
		ListSelectionListener selectionListener = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				JList list = (JList) evt.getSource();
				if (list.getSelectedIndex() == -1)
					return;
				
				for (WeekList wl : WeekListView.this.weekLists) {
					if (!list.equals(wl.getList()))
						wl.getList().clearSelection();
				}
				
				try {
					WeekListView.this.deselect();
					WeekListView.this.select(
							null,
							(Event) list.getSelectedValue(),
							true);
				} catch (Exception e) {
					
				}
			}
			
		};
		
		for (int i = 0; i < 7; i++) {
			this.weekLists[i] = new WeekList(config);
			this.weekLists[i].getList().addMouseListener(this);
			this.weekLists[i].getList().addListSelectionListener(
					selectionListener);
			listPanel.add(this.weekLists[i]);
		}
		
		JScrollPane jsp = new JScrollPane(listPanel);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		this.panel.add(jsp, BorderLayout.CENTER);
		
		this.setDate(new Date());
	}
	
	private static class ScrollablePanel extends JPanel implements Scrollable {
		
		public ScrollablePanel() {
			super();
		}
		
		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return this.getPreferredSize();
		}
		
		@Override
		public int getScrollableBlockIncrement(
				Rectangle visibleRect,
				int orientation,
				int direction) {
			return Math.max(visibleRect.height * 9 / 10, 1);
		}
		
		@Override
		public boolean getScrollableTracksViewportHeight() {
			if (this.getParent() instanceof JViewport) {
				JViewport viewport = (JViewport) this.getParent();
				return this.getPreferredSize().height < viewport.getHeight();
			}
			
			return false;
		}
		
		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}
		
		@Override
		public int getScrollableUnitIncrement(
				Rectangle visibleRect,
				int orientation,
				int direction) {
			return Math.max(visibleRect.height / 10, 1);
		}
		
	}
	
	@Override
	public JComponent getComponent() {
		return this.panel;
	}
	
	@Override
	protected Date getDate(int xPos, int yPos) throws Exception {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void refresh0() throws Exception {
		if (this.broker != null) {
			List<Event> events = this.broker.getEvents(null);
			
			Calendar end = Calendar.getInstance();
			end.setTime(this.date);
			end.add(Calendar.DAY_OF_MONTH, 6);
			
			this.parent.setTitle(this.date, end.getTime());
			
			this.showEvents.clear();
			
			List<Event> dayEvents = new ArrayList<Event>();
			for (int i = 0; i < 7; i++) {
				dayEvents.clear();
				
				Calendar day = Calendar.getInstance();
				day.setTime(this.date);
				day.add(Calendar.DAY_OF_MONTH, i);
				
				for (Event event : events) {
					if (DateUtil.isSameDay(event.getStart(), day.getTime())) {
						dayEvents.add(event);
						this.showEvents.add(event);
					}
				}
				
				this.weekLists[i].setEvents(dayEvents, day.getTime());
			}
		}
	}
	
	public void setDate(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		if (c.getTime().equals(this.date))
			return;
		
		this.date = c.getTime();
		
		try {
			this.refresh0();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JList list = (JList) e.getSource();
			int index = list.locationToIndex(e.getPoint());
			
			if (index == -1)
				return;
			
			Object item = list.getModel().getElementAt(index);
			list.ensureIndexIsVisible(index);
			
			this.listener.eventDoubleClick(null, (Event) item, e);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public List getEvents() {
		return this.showEvents;
	}
	
}
