package com.leclercb.taskunifier.gui.utils;

public final class OsUtils {

	private OsUtils() {

	}

	private static final boolean osIsMacOsX;
	private static final boolean osIsWindows;
	private static final boolean osIsWindowsXP;
	private static final boolean osIsWindows2003;
	private static final boolean osIsWindowsVista;
	private static final boolean osIsLinux;

	static {
		String os = System.getProperty("os.name").toLowerCase();

		if (os == null)
			os = "";

		osIsMacOsX = os.equals("mac os x");
		osIsWindows = os.indexOf("windows") != -1;
		osIsWindowsXP = os.equals("windows xp");
		osIsWindows2003 = os.equals("windows 2003");
		osIsWindowsVista = os.equals("windows vista");
		osIsLinux = os.indexOf("linux") != -1;
	}

	public static boolean isMacOSX() {
		return osIsMacOsX;
	}

	public static boolean isWindows() {
		return osIsWindows;
	}

	public static boolean isWindowsXP() {
		return osIsWindowsXP;
	}

	public static boolean isWindows2003() {
		return osIsWindows2003;
	}

	public static boolean isWindowsVista() {
		return osIsWindowsVista;
	}

	public static boolean isLinux() {
		return osIsLinux;
	}

}
