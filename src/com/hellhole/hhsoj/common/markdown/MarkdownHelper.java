package com.hellhole.hhsoj.common.markdown;

import java.util.Arrays;

import com.hellhole.hhsoj.common.markdown.ext.media.MediaTagsExtension;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class MarkdownHelper {
	private static Parser parser;
	private static HtmlRenderer renderer;

	private static void init() {
		MutableDataSet options = new MutableDataSet();
		options.set(Parser.EXTENSIONS, Arrays.asList(
				TablesExtension.create(),
				StrikethroughExtension.create(),
				TocExtension.create(),
				AnchorLinkExtension.create(),
				MediaTagsExtension.create()));
		options.set(AnchorLinkExtension.ANCHORLINKS_WRAP_TEXT, false);
		options.set(AnchorLinkExtension.ANCHORLINKS_TEXT_PREFIX,"<img class=\"octicon\" src=\"assets/img/anchor.svg\" />");
		options.set(AnchorLinkExtension.ANCHORLINKS_ANCHOR_CLASS,"anchor");
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
