package com.hellhole.hhsoj.common.markdown;

import java.util.Arrays;

import com.hellhole.hhsoj.common.markdown.ext.media.MediaTagsExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MarkdownHelper {
	private final static String anchor = "<svg class=\"octicon\" viewBox=\"0 0 16 16\" version=\"1.1\" width=\"16\" height=\"16\" aria-hidden=\"true\"><path fill-rule=\"evenodd\" d=\"M7.775 3.275a.75.75 0 001.06 1.06l1.25-1.25a2 2 0 112.83 2.83l-2.5 2.5a2 2 0 01-2.83 0 .75.75 0 00-1.06 1.06 3.5 3.5 0 004.95 0l2.5-2.5a3.5 3.5 0 00-4.95-4.95l-1.25 1.25zm-4.69 9.64a2 2 0 010-2.83l2.5-2.5a2 2 0 012.83 0 .75.75 0 001.06-1.06 3.5 3.5 0 00-4.95 0l-2.5 2.5a3.5 3.5 0 004.95 4.95l1.25-1.25a.75.75 0 00-1.06-1.06l-1.25 1.25a2 2 0 01-2.83 0z\"></path></svg></a>";

	private static Parser parser;
	private static HtmlRenderer renderer;

	private static void init() {
		MutableDataSet options = new MutableDataSet();
		options.set(Parser.EXTENSIONS, Arrays.asList(
				TablesExtension.create(),
				StrikethroughExtension.create(),
				MediaTagsExtension.create()));
		parser = Parser.builder(options).build();
		renderer = HtmlRenderer.builder(options).build();
	}

	public static String convert(String source) {
		if (parser == null || renderer == null)
			init();
		Node document = parser.parse(source);
		String html = renderer.render(document);
		return html;
	}
}
