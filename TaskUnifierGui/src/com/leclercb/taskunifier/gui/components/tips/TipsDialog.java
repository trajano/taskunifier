package com.leclercb.taskunifier.gui.components.tips;

import java.util.Random;

import org.jdesktop.swingx.JXTipOfTheDay;
import org.jdesktop.swingx.tips.TipLoader;

import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frame.MainFrame;
import com.leclercb.taskunifier.gui.translations.Tips;

public class TipsDialog extends JXTipOfTheDay {
	
	private static TipsDialog INSTANCE;
	
	public static TipsDialog getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TipsDialog();
		
		return INSTANCE;
	}
	
	public TipsDialog() {
		super(TipLoader.load(Tips.getProperties()));
	}
	
	public void showTipsDialog(boolean startup) {
		try {
			Random r = new Random();
			int i = r.nextInt(this.getModel().getTipCount());
			this.setCurrentTip(i);
		} catch (Throwable t) {
			
		}
		
		this.showDialog(
				MainFrame.getInstance().getFrame(),
				new ShowOnStartupChoice() {
					
					@Override
					public void setShowingOnStartup(boolean showOnStartup) {
						Main.getSettings().setBooleanProperty(
								"tips.show_on_startup",
								showOnStartup);
					}
					
					@Override
					public boolean isShowingOnStartup() {
						return Main.getSettings().getBooleanProperty(
								"tips.show_on_startup");
					}
					
				},
				!startup);
	}
	
}
