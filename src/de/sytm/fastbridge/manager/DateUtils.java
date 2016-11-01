package de.sytm.fastbridge.manager;

public class DateUtils {

	public static String toSecs(long time) {
		String tmp = "" + time;
		if (time <= 1001) {
			tmp = "000" + tmp;
			if (tmp.length() > 4)
				tmp = tmp.substring(tmp.length() - 4);
		}
		return "§6" + tmp.substring(0, tmp.length() - 3) + "§7.§6" + tmp.substring(tmp.length() - 3, tmp.length())
				+ "§7s";
	}
}
