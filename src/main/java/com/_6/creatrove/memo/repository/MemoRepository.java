package com._6.creatrove.memo.repository;

import com._6.creatrove.memo.domain.Memo;
import com._6.creatrove.memo.domain.MemoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByUser_UserIdOrderByPinnedDescCreatedAtDesc(Long userId);

    List<Memo> findByUser_UserIdAndCategoryOrderByPinnedDescCreatedAtDesc(Long userId, MemoCategory category);

    List<Memo> findTop10ByUser_UserIdAndViewedAtIsNotNullOrderByViewedAtDesc(Long userId);

    List<Memo> findByUser_UserIdAndContentContainingIgnoreCase(Long userId, String keyword);
}