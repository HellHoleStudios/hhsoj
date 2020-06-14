package com.hellhole.hhsoj.common.markdown.ext.media.internal;

import com.vladsch.flexmark.ast.Image;
import com.hellhole.hhsoj.common.markdown.ext.media.AudioLink;
import com.hellhole.hhsoj.common.markdown.ext.media.BilibiliLink;
import com.hellhole.hhsoj.common.markdown.ext.media.YoutubeLink;
import com.hellhole.hhsoj.common.markdown.ext.media.VideoLink;
import com.vladsch.flexmark.parser.block.NodePostProcessor;
import com.vladsch.flexmark.parser.block.NodePostProcessorFactory;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeTracker;
import com.vladsch.flexmark.util.data.DataHolder;

import org.jetbrains.annotations.NotNull;

public class MediaTagsNodePostProcessor extends NodePostProcessor {
	public MediaTagsNodePostProcessor(DataHolder options) {
	}

	@Override
	public void process(@NotNull NodeTracker state, @NotNull Node node) {
		if (node instanceof Image) {
			Image image=(Image)node;
			AbstractMediaLink mediaLink = null;
			String urlStr=image.getUrl().unescape();
			
			if(Utilities.resolveYoutubeSrc(urlStr)!=null) {
				mediaLink = new YoutubeLink(image);
			}
			else if(Utilities.resolveBilibiliSrc(urlStr)!=null) {
				mediaLink = new BilibiliLink(image);
			}
			else if (Utilities.resolveAudioType(Utilities.getFilename(urlStr))!=null) {
				mediaLink = new AudioLink(image);
			}
			else if(Utilities.resolveVideoType(Utilities.getFilename(urlStr))!=null) {
				mediaLink = new VideoLink(image);
			}
			else {
				return;
			}
			node.insertAfter(mediaLink);
			node.unlink();
			state.nodeRemoved(node);
			state.nodeAddedWithChildren(mediaLink);
		}
	}

	public static class Factory extends NodePostProcessorFactory {
		public Factory(DataHolder options) {
			super(false);

			addNodes(Image.class);
		}

		@NotNull
		@Override
		public NodePostProcessor apply(@NotNull Document document) {
			return new MediaTagsNodePostProcessor(document);
		}
	}
}
