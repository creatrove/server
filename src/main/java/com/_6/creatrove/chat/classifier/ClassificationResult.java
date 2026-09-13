package com._6.creatrove.chat.classifier;

import java.util.List;

public record ClassificationResult(
        MemoDraft memo,
        CalendarDraft calendar,
        LedgerDraft ledger,
        List<String> suggestedCategories // AMBIGUOUS일 때만 값 있음
) {
    public boolean isAmbiguous() {
        return memo == null && calendar == null && ledger == null;
    }

    public int matchedCount() {
        int count = 0;
        if (memo != null) count++;
        if (calendar != null) count++;
        if (ledger != null) count++;
        return count;
    }
}
