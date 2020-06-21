package com.hellhole.hhsoj.common.markdown.ext.media.internal;

import java.net.MalformedURLException;
import java.net.URL;

import com.hellhole.hhsoj.common.Sanitizer;

final class Utilities {

	private Utilities() {
	}

	public static String resolveYoutubeSrc(String src) {
		URL url=parseURL(src);
		if(url==null)return null;
		String host = url.getHost();
		if (!"/watch".equals(url.getPath())&&"youtu.be".equals(host)) {
			String v = url.getPath().substring(1);
			String t = null;
			if (url.getQuery() != null) {
				String[] query = url.getQuery().split("&");
				for (String s : query) {
					int equalSign = s.indexOf("=");
					if (equalSign == -1)
						continue;
					String key = s.substring(0, equalSign);
					if ("t".equals(key)) {
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
		} else if ("/watch".equals(url.getPath())
				&& ("www.youtube.com".equals(host) || "youtube.com".equals(host) || "youtu.be".equals(host))) {
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
				if ("v".equals(key)) {
					v = s.substring(equalSign + 1);
				} else if ("t".equals(key)) {
					t = s.substring(equalSign + 1);
				}
			}
			if(!Sanitizer.verifyYoutubeId(v))return null;
			if(t==null) {
				return "https://www.youtube.com/embed/" + v + "?rel=0";
			}
			else {
				return "https://www.youtube.com/embed/" + v + "?start=" + t + "&rel=0";
			}
		}
		return null;
	}

	public static String resolveBilibiliSrc(String src) {
		URL url=parseURL(src);
		if(url==null)return null;
		String host = url.getHost();
		String file = url.getPath().substring(1);
		if(file.startsWith("video/")) {
			file=file.substring(6);
		}
		if (file.endsWith("/")) {
			file = file.substring(0, file.length() - 1);
		}
		if ("www.bilibili.com".equals(host) || "bilibili.com".equals(host) || "b23.tv".equals(host)) {
			String p = "1";
			if (url.getQuery() != null) {
				String[] query = url.getQuery().split("&");
				for (String s : query) {
					int equalSign = s.indexOf("=");
					if (equalSign == -1)
						continue;
					if ("p".equals(s.substring(0, equalSign))) {
						p = s.substring(equalSign + 1);
					}
				}
			}
			if (file.startsWith("av")) {
				file = file.substring(2);
				if(!Sanitizer.verifyBilibiliAId(file))return null;
				return "https://player.bilibili.com/player.html?aid=" + file + "&page=" + p;
			} else if (file.startsWith("BV")) {
				if(!Sanitizer.verifyBilibiliBvId(file))return null;
				return "https://player.bilibili.com/player.html?bvid=" + file + "&page=" + p;
			}
		}
		return null;
	}

	public static String resolveAudioType(String source) {
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

	public static String resolveVideoType(String source) {
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

	public static URL parseURL(String urlStr) {
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
		return url;
	}
	
	public static String getFilename(String path) {
		int lastSlash = path.lastIndexOf("/");
		return path.substring(lastSlash + 1, path.length());
	}
}
