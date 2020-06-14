package com.hellhole.hhsoj.common.markdown.ext.media.internal;

import java.net.MalformedURLException;
import java.net.URL;

final class Utilities {

	private Utilities() {
	}

	static String resolveYoutubeSrc(String urlStr) {
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			if (e.getMessage().startsWith("no protocol")) {
				if (urlStr.startsWith("//")) {
					urlStr = "http:" + urlStr;
				} else {
					urlStr = "http://" + urlStr;
				}

				try {
					url = new URL(urlStr);
				} catch (MalformedURLException e2) {
					return null;
				}
			} else {
				return null;
			}
		}

		String host = url.getHost();
		if (!url.getPath().equals("/watch")&&host.equals("youtu.be")) {
			String v = url.getPath().substring(1);
			String t = null;
			if (url.getQuery() != null) {
				String[] query = url.getQuery().split("&");
				for (String s : query) {
					int equalSign = s.indexOf("=");
					if (equalSign == -1)
						continue;
					String key = s.substring(0, equalSign);
					if (key.equals("t")) {
						t = s.substring(equalSign + 1);
					}
				}
			}
			
			if(t==null) {
				return "https://www.youtube.com/embed/" + v + "?rel=0";
			}
			else {
				return "https://www.youtube.com/embed/" + v + "start=" + t + "?rel=0";
			}
		} else if (url.getPath().equals("/watch")
				&& (host.equals("www.youtube.com") || host.equals("youtube.com") || host.equals("youtu.be"))) {
			if (url.getQuery() == null)
				return null;
			String[] query = url.getQuery().split("&");
			String v = "";
			String t = null;
			for (String s : query) {
				int equalSign = s.indexOf("=");
				if (equalSign == -1)
					continue;
				String key = s.substring(0, equalSign);
				if (key.equals("v")) {
					v = s.substring(equalSign + 1);
				} else if (key.equals("t")) {
					t = s.substring(equalSign + 1);
				}
			}
			if(t==null) {
				return "https://www.youtube.com/embed/" + v + "?rel=0";
			}
			else {
				return "https://www.youtube.com/embed/" + v + "start=" + t + "?rel=0";
			}
		}
		return null;
	}

	static String resolveBilibiliSrc(String urlStr) {
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			if (e.getMessage().startsWith("no protocol")) {
				if (urlStr.startsWith("//")) {
					urlStr = "http:" + urlStr;
				} else {
					urlStr = "http://" + urlStr;
				}

				try {
					url = new URL(urlStr);
				} catch (MalformedURLException e2) {
					return null;
				}
			} else {
				return null;
			}
		}

		String host = url.getHost();
		String file = url.getPath();
		if(file.startsWith("/video/")) {
			file=file.substring(7);
		}
		if (file.endsWith("/")) {
			file = file.substring(0, file.length() - 1);
		}
		if (host.equals("www.bilibili.com") || host.equals("bilibili.com") || host.equals("b23.tv")) {
			String p = "1";
			if (url.getQuery() != null) {
				String[] query = url.getQuery().split("&");
				for (String s : query) {
					int equalSign = s.indexOf("=");
					if (equalSign == -1)
						continue;
					if (s.substring(0, equalSign).equals("p")) {
						p = s.substring(equalSign + 1);
					}
				}
			}
			if (file.startsWith("av")) {
				file = file.substring(2);
				return "https://player.bilibili.com/player.html?aid=" + file + "&page=" + p;
			} else if (file.startsWith("BV")) {
				return "https://player.bilibili.com/player.html?bvid=" + file + "&page=" + p;
			}
		}
		return null;
	}

	static String resolveAudioType(String source) {
		int period = source.lastIndexOf(".");
		if (period == -1)
			return null;

		String extension = source.substring(period + 1, source.length());
		switch (extension) {
		case "opus":
			return "audio/ogg; codecs=opus";
		case "weba":
			return "audio/webm";
		case "webm":
			return "audio/webm; codecs=opus";
		case "ogg":
			return "audio/ogg";
		case "mp3":
			return "audio/mpeg";
		case "wav":
			return "audio/wav";
		case "flac":
			return "audio/flac";
		default:
			return null;
		}
	}

	static String resolveVideoType(String source) {
		int period = source.lastIndexOf(".");
		if (period == -1)
			return null;

		String extension = source.substring(period + 1, source.length());
		switch (extension) {
		case "mp4":
			return "video/mp4";
		case "webm":
			return "video/webm";
		case "ogv":
			return "video/ogg";
		case "3gp":
			return "video/3gp";
		default:
			return null;
		}
	}

	static String getFilename(String path) {
		int lastSlash = path.lastIndexOf("/");
		return path.substring(lastSlash + 1, path.length());
	}

}
