package com._6.creatrove.memo.controller;

import com._6.creatrove.memo.domain.MemoCategory;
import com._6.creatrove.memo.dto.MemoCategoryResponse;
import com._6.creatrove.memo.dto.MemoListResponse;
import com._6.creatrove.memo.dto.MemoResponse;
import com._6.creatrove.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping("/memos")
    public MemoListResponse getMemos(@AuthenticationPrincipal Long userId) {
        return memoService.getMemos(userId);
    }

    @GetMapping("/memos/category/{category}")
    public MemoCategoryResponse getMemosByCategory(@AuthenticationPrincipal Long userId,
                                                   @PathVariable MemoCategory category) {
        return memoService.getMemosByCategory(userId, category);
    }

    @GetMapping("/memos/{memoId}")
    public MemoResponse getMemo(@AuthenticationPrincipal Long userId, @PathVariable Long memoId) {
        return memoService.getMemo(userId, memoId);
    }
}