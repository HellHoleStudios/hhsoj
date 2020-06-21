package com.hellhole.hhsoj.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class Sanitizer {
	private static final PolicyFactory markdown = new HtmlPolicyBuilder()
			.allowStandardUrlProtocols()
			.allowElements("hr", "dd", "dl", "dt", "kbd", "abbr", "pre", "audio", "video", "source")
			.allowAttributes("align", "alink", "alt", "bgcolor", "border", "cellpadding", "cellspacing", "class",
					"color", "cols", "colspan", "coords", "dir", "face", "height", "hspace", "ismap", "lang",
					"marginheight", "marginwidth", "multiple", "nohref", "noresize", "noshade", "nowrap", "ref", "rel",
					"rev", "rows", "rowspan", "scrolling", "shape", "span", "summary", "tabindex", "title", "usemap",
					"valign", "value", "vlink", "vspace", "width")
			.globally()
			.allowElements("iframe").allowAttributes("src")
			.matching((String elementName, String attributeName, String value) -> {
				if (validVideoSource(value))
					return value;
				return null;
			}).onElements("iframe")
			.allowAttributes("allow", "frameborder", "framespacing", "allowfullscreen").onElements("iframe")
			.toFactory()
			.and(Sanitizers.BLOCKS).and(Sanitizers.FORMATTING).and(Sanitizers.IMAGES)
			.and(Sanitizers.LINKS).and(Sanitizers.STYLES).and(Sanitizers.TABLES);

	public static String sanitizeMarkdown(String html) {
		return markdown.sanitize(html);
	}

	public static boolean validVideoSource(String src) {
		String urlStr = src;
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
					return false;
				}
			} else {
				return false;
			}
		}
		String host = url.getHost();
		if (host.equals("player.bilibili.com")) {
			if (!"/player.html".equals(url.getPath())) {
				return false;
			}
			String[] query = url.getQuery().split("&");
			for (String s : query) {
				if (s.startsWith("page=")) {

				} else if (s.startsWith("aid=")) {
					if (!verifyBilibiliAId(s.substring(4))) {
						return false;
					}
				} else if (s.startsWith("bvid=")) {
					if (!verifyBilibiliBvId(s.substring(5))) {
						return false;
					}
				} else {
					return false;
				}
			}
		} else if (host.equals("www.youtube.com")) {
			String path = url.getPath();
			if (!path.startsWith("/embed")) {
				return false;
			}
			path = path.substring(6);
			if (path.startsWith("/")) {
				path = path.substring(1);
			}
			if (!verifyYoutubeId(path)) {
				return false;
			}
			String[] query = url.getQuery().split("&");
			for (String s : query) {
				if (!(s.startsWith("rel=") || s.startsWith("start="))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public static boolean verifyYoutubeId(String id) {
		if (id == null) {
			return false;
		}
		if (id.length() != 11) {
			return false;
		}
		for (int i = 0; i < id.length(); i++) {
			if (!Character.isLetterOrDigit(id.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean verifyBilibiliAId(String id) {
		if (id == null) {
			return false;
		}
		for (int i = 0; i < id.length(); i++) {
			if (!Character.isDigit(id.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean verifyBilibiliBvId(String id) {
		if (id == null) {
			return false;
		}
		if (!id.startsWith("BV")) {
			return false;
		}
		String newId = id.substring(2);
		if (newId.length() != 10) {
			return false;
		}
		for (int i = 0; i < newId.length(); i++) {
			if (!Character.isLetterOrDigit(newId.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String encodeEntity(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
				.replace("'", "&#x27").replace("/", "&#x2f");
	}
}
