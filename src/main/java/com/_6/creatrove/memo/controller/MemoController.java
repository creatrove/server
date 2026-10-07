package com._6.creatrove.memo.controller;

import com._6.creatrove.memo.domain.MemoCategory;
import com._6.creatrove.memo.dto.*;
import com._6.creatrove.memo.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/memos/{memoId}")
    public MemoUpdateResponse update(@AuthenticationPrincipal Long userId,
                                     @PathVariable Long memoId,
                                     @Valid @RequestBody MemoUpdateRequest request) {
        return memoService.updateMemo(userId, memoId, request);
    }

    @PatchMapping("/memos/{memoId}/pin")
    public MemoUpdateResponse togglePin(@AuthenticationPrincipal Long userId,
                                        @PathVariable Long memoId) {
        return memoService.togglePin(userId, memoId);
    }

    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal Long userId,
                                       @PathVariable Long memoId) {
        memoService.deleteMemo(userId, memoId);
        return ResponseEntity.noContent().build();
    }
}