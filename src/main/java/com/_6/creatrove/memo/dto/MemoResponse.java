package com._6.creatrove.memo.dto;

import java.time.LocalDateTime;

public record MemoResponse(Long memoId, String content, String category, Boolean pinned, LocalDateTime createdAt) {}