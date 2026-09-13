package com._6.creatrove.memo.dto;

import java.util.List;

public record MemoGroupResponse(String category, List<MemoResponse> memos) {}