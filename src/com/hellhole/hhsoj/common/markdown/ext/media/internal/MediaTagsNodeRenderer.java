package com.hellhole.hhsoj.common.markdown.ext.media.internal;

import com.hellhole.hhsoj.common.markdown.ext.media.AudioLink;
import com.hellhole.hhsoj.common.markdown.ext.media.BilibiliLink;
import com.hellhole.hhsoj.common.markdown.ext.media.YoutubeLink;
import com.hellhole.hhsoj.common.markdown.ext.media.VideoLink;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.util.data.DataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class MediaTagsNodeRenderer implements NodeRenderer {
	public MediaTagsNodeRenderer(DataHolder options) {
	}

	@Override
	public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {

		HashSet<NodeRenderingHandler<?>> set = new HashSet<>();
		set.add(new NodeRenderingHandler<>(YoutubeLink.class, this::renderYoutubeLink));
		set.add(new NodeRenderingHandler<>(BilibiliLink.class, this::renderBilibiliLink));
		set.add(new NodeRenderingHandler<>(AudioLink.class, this::renderAudioLink));
		set.add(new NodeRenderingHandler<>(VideoLink.class, this::renderVideoLink));
		return set;
	}

	private void renderYoutubeLink(YoutubeLink node, NodeRendererContext context, HtmlWriter html) {
		if (context.isDoNotRenderLinks()) {
			context.renderChildren(node);
		} else {
			String iframe = "<iframe title=\"" + node.getTitle() + "\" src=\""
					+ Utilities.resolveYoutubeSrc(node.getUrl().unescape())
					+ "\" height=\"360\" width=\"480\" border=\"0\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen=\"true\"></iframe>";
			html.raw(iframe);
		}
	}

	private void renderBilibiliLink(BilibiliLink node, NodeRendererContext context, HtmlWriter html) {
		if (context.isDoNotRenderLinks()) {
			context.renderChildren(node);
		} else {
			String iframe = "<iframe title=\"" + node.getTitle() + "\" src=\""
					+ Utilities.resolveBilibiliSrc(node.getUrl().unescape())
					+ "\" scrolling=\"no\" height=\"360\" width=\"480\" border=\"0\" frameborder=\"no\" framespacing=\"0\" allowfullscreen=\"true\"></iframe>";
			html.raw(iframe);
		}
	}

	private void renderAudioLink(AudioLink node, NodeRendererContext context, HtmlWriter html) {
		if (context.isDoNotRenderLinks()) {
			context.renderChildren(node);
		} else {
			ResolvedLink resolvedLink = context.resolveLink(LinkType.LINK, node.getUrl().unescape(), false);
			String[] sources = resolvedLink.getUrl().split("\\|");
			html.attr("title", node.getText()).attr("controls", "").withAttr().tag("audio");
			for (String source : sources) {
				String encoded = context.getHtmlOptions().percentEncodeUrls ? context.encodeUrl(source) : source;
				String type = Utilities.resolveAudioType(source);
				html.attr("src", encoded);
				if (type != null)
					html.attr("type", type);
				html.withAttr().tag("source", true);
			}
			html.text("Your browser does not support the audio element.");
			html.tag("/audio");
		}
	}

	private void renderVideoLink(VideoLink node, NodeRendererContext context, HtmlWriter html) {
		if (context.isDoNotRenderLinks()) {
			context.renderChildren(node);
		} else {
			ResolvedLink resolvedLink = context.resolveLink(LinkType.LINK, node.getUrl().unescape(), false);
			String[] sources = resolvedLink.getUrl().split("\\|");
			html.attr("title", node.getText()).attr("controls", "").withAttr().tag("video");
			for (String source : sources) {
				String encoded = context.getHtmlOptions().percentEncodeUrls ? context.encodeUrl(source) : source;
				String type = Utilities.resolveVideoType(source);
				html.attr("src", encoded);
				if (type != null)
					html.attr("type", type);
				html.withAttr().tag("source", true);
			}
			html.text("Your browser does not support the video element.");
			html.tag("/video");
		}
	}

	public static class Factory implements NodeRendererFactory {
		@NotNull
		@Override
		public NodeRenderer apply(@NotNull DataHolder options) {
			return new MediaTagsNodeRenderer(options);
		}
	}
}
