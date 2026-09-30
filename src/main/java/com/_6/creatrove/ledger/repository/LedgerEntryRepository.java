package com._6.creatrove.ledger.repository;

import com._6.creatrove.ledger.domain.IncomeStatus;
import com._6.creatrove.ledger.domain.LedgerEntry;
import com._6.creatrove.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    // 채팅 캡처 시 중복 감지용
    Optional<LedgerEntry> findByUserAndAmountAndStatus(User user, Long amount, IncomeStatus status);

    // 월별 조회
    List<LedgerEntry> findByUser_UserIdAndEntryDateBetweenOrderByEntryDateAsc(
            Long userId, LocalDate start, LocalDate end);

    // 일자별 조회
    List<LedgerEntry> findByUser_UserIdAndEntryDateOrderByCreatedAtAsc(Long userId, LocalDate date);

    // 트로브 "최근 확인" 목록용
    List<LedgerEntry> findTop10ByUser_UserIdAndViewedAtIsNotNullOrderByViewedAtDesc(Long userId);

    // 트로브 통합 검색용
    List<LedgerEntry> findByUser_UserIdAndItemContainingIgnoreCase(Long userId, String keyword);
}