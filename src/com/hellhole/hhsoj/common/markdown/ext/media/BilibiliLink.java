package com.hellhole.hhsoj.common.markdown.ext.media;

import com.vladsch.flexmark.ast.Image;
import com.hellhole.hhsoj.common.markdown.ext.media.internal.AbstractMediaLink;

public class BilibiliLink extends AbstractMediaLink {

    public BilibiliLink() {
        super();
    }

    public BilibiliLink(Image other) {
        super(other);
    }

    // This class leaves room for specialization, should we need it.
    // Additionally, it makes managing different Node types easier for users.
}
