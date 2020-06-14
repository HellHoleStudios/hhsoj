package com.hellhole.hhsoj.common.markdown.ext.media.internal;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.InlineLinkNode;
import com.vladsch.flexmark.util.sequence.BasedSequence;

public abstract class AbstractMediaLink extends InlineLinkNode {

    public AbstractMediaLink() {
    	
    }

    public AbstractMediaLink(Image other) {
        super(other.baseSubSequence(other.getStartOffset(), other.getEndOffset()),
                other.baseSubSequence(other.getStartOffset(), other.getTextOpeningMarker().getEndOffset()),
                other.getText(),
                other.getTextClosingMarker(),
                other.getLinkOpeningMarker(),
                other.getUrl(),
                other.getTitleOpeningMarker(),
                other.getTitle(),
                other.getTitleClosingMarker(),
                other.getLinkClosingMarker()
        );
    }

    @Override
    public void setTextChars(BasedSequence textChars) {
        int textCharsLength = textChars.length();
        textOpeningMarker = textChars.subSequence(0, 1);
        text = textChars.subSequence(2, textCharsLength - 1).trim();
        textClosingMarker = textChars.subSequence(textCharsLength - 1, textCharsLength);
    }
}
