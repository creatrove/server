package com._6.creatrove.memo.dto;

import java.util.List;

public record MemoListResponse(int totalCount, List<MemoGroupResponse> groups) {}