package com.hellhole.hhsoj.common.markdown.ext.media;

import com.vladsch.flexmark.ast.Image;
import com.hellhole.hhsoj.common.markdown.ext.media.internal.AbstractMediaLink;

public class YoutubeLink extends AbstractMediaLink {

    public YoutubeLink() {
        super();
    }

    public YoutubeLink(Image other) {
        super(other);
    }

    // This class leaves room for specialization, should we need it.
    // Additionally, it makes managing different Node types easier for users.
}
