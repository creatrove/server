package com._6.creatrove.ledger.repository;

import com._6.creatrove.ledger.domain.IncomeStatus;
import com._6.creatrove.ledger.domain.LedgerEntry;
import com._6.creatrove.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    Optional<LedgerEntry> findByUserAndAmountAndStatus(User user, Long amount, IncomeStatus status);
}