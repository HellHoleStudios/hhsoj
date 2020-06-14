package com.hellhole.hhsoj.common.markdown.ext.media;

import com.vladsch.flexmark.util.ast.VisitHandler;

public class YoutubeLinkVisitorExt {
    public static <V extends YoutubeLinkVisitor> VisitHandler<?>[] VISIT_HANDLERS(V visitor) {
        return new VisitHandler<?>[] {
                new VisitHandler<>(YoutubeLink.class, visitor::visit)
        };
    }
}
