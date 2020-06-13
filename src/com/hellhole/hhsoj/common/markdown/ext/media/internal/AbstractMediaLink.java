package com.hellhole.hhsoj.common.markdown.ext.media.internal;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.InlineLinkNode;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public abstract class AbstractMediaLink extends InlineLinkNode {

    final private String PREFIX;
    public AbstractMediaLink(String prefix, String type) {
        this.PREFIX = prefix;
    }

    public AbstractMediaLink(String prefix, String type, Image other) {
        super(other.baseSubSequence(other.getStartOffset() - prefix.length(), other.getEndOffset()),
                other.baseSubSequence(other.getStartOffset() - prefix.length(), other.getTextOpeningMarker().getEndOffset()),
                other.getText(),
                other.getTextClosingMarker(),
                other.getLinkOpeningMarker(),
                other.getUrl(),
                other.getTitleOpeningMarker(),
                other.getTitle(),
                other.getTitleClosingMarker(),
                other.getLinkClosingMarker()
        );

        this.PREFIX = prefix;
    }

    final public String getPrefix() {
        return PREFIX;
    }

    @Override
    public void setTextChars(BasedSequence textChars) {
        int textCharsLength = textChars.length();
        textOpeningMarker = textChars.subSequence(0, PREFIX.length() + 1); // grab n characters, n - 1 for the PREFIX and 1 for the opener
        text = textChars.subSequence(PREFIX.length() + 2, textCharsLength - 1).trim();
        textClosingMarker = textChars.subSequence(textCharsLength - 1, textCharsLength);
    }
}
