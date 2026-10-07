package com._6.creatrove.memo.dto;

import com._6.creatrove.memo.domain.MemoCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public record MemoUpdateRequest(
        @Size(max = 10000, message = "메모 내용이 너무 깁니다.")
        String content,
        MemoCategory category
) {
    @JsonIgnore
    @AssertTrue(message = "수정할 내용이 없습니다.")
    public boolean isNotEmpty() {
        return (content != null && !content.isBlank()) || category != null;
    }
}