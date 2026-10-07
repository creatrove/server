package com._6.creatrove.memo.service;

import com._6.creatrove.memo.domain.Memo;
import com._6.creatrove.memo.domain.MemoCategory;
import com._6.creatrove.memo.dto.*;
import com._6.creatrove.memo.exception.MemoNotFoundException;
import com._6.creatrove.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {

    private static final List<MemoCategory> CATEGORY_ORDER =
            List.of(MemoCategory.SCRIPT, MemoCategory.IDEA, MemoCategory.REFERENCE);

    private final MemoRepository memoRepository;

    public MemoListResponse getMemos(Long userId) {
        List<Memo> memos = memoRepository.findByUser_UserIdOrderByPinnedDescCreatedAtDesc(userId);

        Map<MemoCategory, List<MemoResponse>> grouped = memos.stream()
                .collect(Collectors.groupingBy(
                        Memo::getCategory,
                        LinkedHashMap::new,
                        Collectors.mapping(this::toResponse, Collectors.toList())
                ));

        List<MemoGroupResponse> groups = CATEGORY_ORDER.stream()
                .filter(grouped::containsKey)
                .map(category -> new MemoGroupResponse(category.name(), grouped.get(category)))
                .toList();

        return new MemoListResponse(memos.size(), groups);
    }

    public MemoCategoryResponse getMemosByCategory(Long userId, MemoCategory category) {
        List<Memo> memos = memoRepository
                .findByUser_UserIdAndCategoryOrderByPinnedDescCreatedAtDesc(userId, category);

        List<MemoResponse> responses = memos.stream().map(this::toResponse).toList();
        return new MemoCategoryResponse(category.name(), memos.size(), responses);
    }

    @Transactional
    public MemoResponse getMemo(Long userId, Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .filter(m -> m.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new MemoNotFoundException(memoId));

        memo.markViewed();

        return toResponse(memo);
    }

    @Transactional
    public MemoUpdateResponse updateMemo(Long userId, Long memoId, MemoUpdateRequest request) {
        Memo memo = memoRepository.findByIdAndUser_UserId(memoId, userId)
                .orElseThrow(() -> new MemoNotFoundException(memoId));
        if (request.content() != null && !request.content().isBlank()) {
            memo.updateContent(request.content());
        }
        if (request.category() != null) {
            memo.updateCategory(request.category());
        }
        return MemoUpdateResponse.from(memo);
    }

    @Transactional
    public MemoUpdateResponse togglePin(Long userId, Long memoId) {
        Memo memo = memoRepository.findByIdAndUser_UserId(memoId, userId)
                .orElseThrow(() -> new MemoNotFoundException(memoId));
        memo.togglePin();
        return MemoUpdateResponse.from(memo);
    }

    @Transactional
    public void deleteMemo(Long userId, Long memoId) {
        Memo memo = memoRepository.findByIdAndUser_UserId(memoId, userId)
                .orElseThrow(() -> new MemoNotFoundException(memoId));
        memoRepository.delete(memo);
    }

    private MemoResponse toResponse(Memo memo) {
        return new MemoResponse(memo.getId(), memo.getContent(), memo.getCategory().name(),
                memo.getPinned(), memo.getCreatedAt());
    }
}