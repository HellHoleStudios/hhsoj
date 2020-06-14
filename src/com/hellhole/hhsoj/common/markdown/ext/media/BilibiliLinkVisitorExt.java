package com.hellhole.hhsoj.common.markdown.ext.media;

import com.vladsch.flexmark.util.ast.VisitHandler;

public class BilibiliLinkVisitorExt {
    public static <V extends BilibiliLinkVisitor> VisitHandler<?>[] VISIT_HANDLERS(V visitor) {
        return new VisitHandler<?>[] {
                new VisitHandler<>(BilibiliLink.class, visitor::visit)
        };
    }
}
