package com._6.creatrove.memo.dto;

import com._6.creatrove.memo.domain.Memo;

public record MemoUpdateResponse(
        Long memoId,
        String content,
        String category,
        String categoryDisplayName,
        Boolean pinned
) {
    public static MemoUpdateResponse from(Memo memo) {
        return new MemoUpdateResponse(
                memo.getId(),
                memo.getContent(),
                memo.getCategory().name(),
                memo.getCategory().displayName(),
                memo.getPinned()
        );
    }
}