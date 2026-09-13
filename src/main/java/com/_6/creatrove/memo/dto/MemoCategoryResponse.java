package com._6.creatrove.memo.dto;

import java.util.List;

public record MemoCategoryResponse(String category, int totalCount, List<MemoResponse> memos) {}