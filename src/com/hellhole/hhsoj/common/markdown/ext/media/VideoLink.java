package com.hellhole.hhsoj.common.markdown.ext.media;

import com.vladsch.flexmark.ast.Image;
import com.hellhole.hhsoj.common.markdown.ext.media.internal.AbstractMediaLink;

public class VideoLink extends AbstractMediaLink {

    public VideoLink() {
        super();
    }

    public VideoLink(Image other) {
        super(other);
    }

    // This class leaves room for specialization, should we need it.
    // Additionally, it makes managing different Node types easier for users.
}
