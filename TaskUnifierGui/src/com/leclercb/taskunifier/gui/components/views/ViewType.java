package com.leclercb.taskunifier.gui.components.views;

public enum ViewType {
	
	TASKS(TaskView.class),
	NOTES(NoteView.class),
	CALENDAR(CalendarView.class);
	
	private Class<?> cls;
	
	private ViewType(Class<?> cls) {
		this.cls = cls;
	}
	
	public boolean match(View o) {
		return this.cls.isInstance(o);
	}
	
}
