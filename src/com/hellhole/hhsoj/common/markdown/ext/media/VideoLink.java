package com.hellhole.hhsoj.common.markdown.ext.media;

import com.vladsch.flexmark.ast.Image;
import com.hellhole.hhsoj.common.markdown.ext.media.internal.AbstractMediaLink;

public class VideoLink extends AbstractMediaLink {

    final public static String PREFIX = "!V";
    final private static String TYPE = "Video";

    public VideoLink() {
        super(PREFIX, TYPE);
    }

    public VideoLink(Image other) {
        super(PREFIX, TYPE, other);
    }

    // This class leaves room for specialization, should we need it.
    // Additionally, it makes managing different Node types easier for users.
}
