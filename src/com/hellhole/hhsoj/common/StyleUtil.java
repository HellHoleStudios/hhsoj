package com.hellhole.hhsoj.common;

import com.hellhole.hhsoj.tomcat.util.TomcatHelper;

public class StyleUtil {
	public static final String[] verFull = { 
			"Accepted",
			"Wrong Answer",
			"Time Limit Exceeded",
			"Memory Limit Exceeded",
			"Runtime Error",
			"Restrict Function",
			"Judgement Failed",
			"Point" };
	public static final String[] verShort = { 
			"AC",
			"WA",
			"TLE",
			"MLE",
			"RE",
			"RF",
			"JF",
			"PT" };
	public static final String[] verIcon = { 
			"check",
			"close",
			"clock-o",
			"database",
			"bomb",
			"ban",
			"remove",
			"pie-chart" };
	public static final String[] verColor = { 
			"#00ee00",
			"#0000ee",
			"#ff8800",
			"#aa00aa",
			"#dd0000",
			"#ddcc00",
			"#aaaaaa",
			"#000000" };
	public static final String[] verDescription = {
			"Your output is correct",
			"Your program produced incorrect output",
			"Your program used too much time to run",
			"Your program used too much memory to run",
			"Your program exited abnormally",
			"Your program used illegal system call",
			"There's something wrong with our system",
			"Verdict is given in the form of points" };
	public static final String verFormat = "<span class=\"verdict-span\" style=\"color:%s;font-weight:bold\" title=\"%s\"><i class=\"%s\"></i> %s</span>";

	public static String styledVerdict(String ver) {
		for (int i = 0; i < verFull.length; i++) {
			if (ver.equals(verFull[i])) {
				return String.format(verFormat, verColor[i], verDescription[i], "fa fa-" + verIcon[i], ver);
			}
		}
		return String.format(verFormat, "#cccccc", "", "", ver);
	}

	public static String styledVerdict(String ver, String color) {
		for (int i = 0; i < verFull.length; i++) {
			if (ver.equals(verFull[i])) {
				return String.format(verFormat, color, verDescription[i], "fa fa-" + verIcon[i], ver);
			}
		}
		return String.format(verFormat, color, "", "", ver);
	}

	public static String shortVerdict(String ver) {
		for (int i = 0; i < verFull.length; i++) {
			if (ver.equals(verFull[i])) {
				return verShort[i];
			}
		}
		return ver;
	}

	public static String shortNumber(float x) {
		String str;
		if (Math.abs(x) <= 1e5f) {
			str = String.format("%.1f", x);
			if (str.length() <= 6) {
				return str;
			}
		}
		if (Math.abs(x) <= 1e8f) {
			str = String.format("%.1fk", x / 1000f);
			if (str.length() <= 6) {
				return str;
			}
		}
		if (Math.abs(x) <= 1e11f) {
			str = String.format("%.1fm", x / 1e6f);
			if (str.length() <= 6) {
				return str;
			}
		}
		if (Math.abs(x) <= 1e14f) {
			str = String.format("%.1fb", x / 1e9f);
			if (str.length() <= 6) {
				return str;
			}
		}
		if (Math.abs(x) <= 1e17f) {
			str = String.format("%.1ft", x / 1e12f);
			if (str.length() <= 6) {
				return str;
			}
		}
		if (x < 0) {
			return "-Inf";
		}
		return "Inf";
	}

	public static String plural(long num, String singular, String plural) {
		if (num > 1)
			return plural;
		return singular;
	}

	public static String shortDate(long time) {
		long diff = System.currentTimeMillis()/1000 - time;
		if (diff == 0) {
			return "now";
		}
		String suffix = "ago";
		if (diff < 0) {
			diff = -diff;
			suffix = "later";
		}
		if (diff < 60) {
			return diff + " sec " + suffix;
		}
		if (diff < 3600) {
			long sec = diff % 60;
			if (sec == 0) {
				return diff / 60 + " min " + suffix;
			}
			return diff / 60 + " min " + sec + " sec " + suffix;
		}
		if (diff < 86400) {
			long min = (diff % 3600 + 30) / 60;
			if (min == 0) {
				return diff / 3600 + " h " + suffix;
			}
			return diff / 3600 + " h " + min + " min " + suffix;
		}
		if (diff < 2629744) {
			long day = diff / 86400;
			long h = (diff % 86400 + 1800) / 3600;
			if (h == 0) {
				return day + plural(day, " day ", " days ") + suffix;
			}
			return day + plural(day, " day ", " days ") + h + " h " + suffix;
		}
		if (diff < 31556926) {
			long month = diff / 2629744;
			long day = (diff % 2629744 + 43200) / 86400;
			if (day == 0) {
				return month + plural(month, " month ", " months ") + suffix;
			}
			return month + plural(month, " month ", " months ") + day + plural(day, " day ", " days ") + suffix;
		}
		long year = diff / 31556926;
		long month = (diff % 31556926) / 2629744;
		long day = ((diff - diff % 31556926) % 86400 + 43200) / 86400;
		if (month == 0) {
			if (day == 0) {
				return year + plural(year, " year ", " years ") + suffix;
			}
			return year + plural(year, " year ", " years ") + day + plural(day, " day ", " days ") + suffix;
		}
		if (day == 0) {
			return year + plural(year, " year ", " years ") + month + plural(month, " month ", " months ") + suffix;
		}
		return year + plural(year, " year ", " years ") + month + plural(month, " month ", " months ") + day
				+ plural(day, " day ", " days ") + suffix;
	}

	public static float squaredAvg(float x, float y, float a, float b) {
		return (float) Math.sqrt((x * x * a + y * y * b) / (a + b));
	}

	public static int normalizeColor(float x) {
		return Math.max(Math.min((int) Math.round(x), 255), 0);
	}

	public static String mixColorSquared(float r1, float g1, float b1, float r2, float g2, float b2, float a, float b) {
		int r3 = normalizeColor(squaredAvg(r1, r2, a, b));
		int g3 = normalizeColor(squaredAvg(g1, g2, a, b));
		int b3 = normalizeColor(squaredAvg(b1, b2, a, b));
		return String.format("rgb(%d,%d,%d)", r3, g3, b3);
	}

	public static String colorize(float x) {
		if (x <= 0.3) {
			return mixColorSquared(255, 0, 0, 255, 136, 0, 0.3f - x, x);
		} else if (x <= 0.7) {
			return mixColorSquared(255, 136, 0, 238, 238, 0, 0.7f - x, x - 0.3f);
		}
		return mixColorSquared(238, 238, 0, 0, 238, 0, 1.0f - x, x - 0.7f);
	}

	public static String getCommonLangName(String lang) {
		Language l = TomcatHelper.getLangs().get(lang);
		if (l == null)
			return lang;
		return l.name;
	}

}
