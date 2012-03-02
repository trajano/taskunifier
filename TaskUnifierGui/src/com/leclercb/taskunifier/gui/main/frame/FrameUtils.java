package com.leclercb.taskunifier.gui.main.frame;

public final class FrameUtils {
	
	private FrameUtils() {
		
	}
	
	public static FrameView getFrameView(int frameId) {
		if (frameId == MainFrame.getInstance().getFrameId())
			return MainFrame.getInstance();
		
		for (SubFrame subFrame : SubFrame.getSubFrames()) {
			if (frameId == subFrame.getFrameId())
				return subFrame;
		}
		
		return null;
	}
	
}
