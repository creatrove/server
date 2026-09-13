package com._6.creatrove.chat.classifier;

import com._6.creatrove.memo.domain.MemoCategory;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RuleBasedMessageClassifier implements MessageClassifier {

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(\\d[\\d,]*)\\s*(원|만원)");
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{1,2})/(\\d{1,2})");
    private static final List<String> DEADLINE_KEYWORDS = List.of("마감", "촬영", "미팅", "회의");
    private static final long MAX_AMOUNT = 1_000_000_000_000L; // 1조원, 비정상 값 방어

    @Override
    public ClassificationResult classify(String content) {
        LedgerDraft ledger = extractLedger(content);
        CalendarDraft calendar = extractCalendar(content);
        MemoDraft memo = (ledger == null && calendar == null) ? extractMemo(content) : null;

        boolean nothingMatched = (ledger == null && calendar == null && memo == null);
        if (nothingMatched) {
            return new ClassificationResult(null, null, null,
                    List.of("MEMO", "CALENDAR", "LEDGER"));
        }

        return new ClassificationResult(memo, calendar, ledger, null);
    }

    private LedgerDraft extractLedger(String content) {
        Matcher matcher = AMOUNT_PATTERN.matcher(content);
        if (!matcher.find()) {
            return null;
        }

        long amount;
        try {
            amount = Long.parseLong(matcher.group(1).replace(",", ""));
        } catch (NumberFormatException e) {
            return null; // 숫자로 파싱 불가능하면 장부로 분류하지 않음
        }

        if ("만원".equals(matcher.group(2))) {
            amount *= 10_000;
        }

        if (amount <= 0 || amount > MAX_AMOUNT) {
            return null; // 비정상적인 금액은 장부로 분류하지 않음
        }

        return new LedgerDraft(content, amount, LocalDate.now());
    }

    private CalendarDraft extractCalendar(String content) {
        Matcher matcher = DATE_PATTERN.matcher(content);
        boolean hasDeadlineKeyword = DEADLINE_KEYWORDS.stream().anyMatch(content::contains);
        boolean hasDatePattern = matcher.find();

        if (!hasDatePattern && !hasDeadlineKeyword) {
            return null;
        }

        LocalDate date = LocalDate.now();
        if (hasDatePattern) {
            try {
                int month = Integer.parseInt(matcher.group(1));
                int day = Integer.parseInt(matcher.group(2));
                date = LocalDate.of(LocalDate.now().getYear(), month, day);
            } catch (NumberFormatException | DateTimeException e) {
                // 13/45처럼 존재하지 않는 날짜면 오늘 날짜로 대체 (500 방지)
                date = LocalDate.now();
            }
        }

        return new CalendarDraft(content, date, null);
    }

    private MemoDraft extractMemo(String content) {
        if (content.length() < 5) {
            return null;
        }
        return new MemoDraft(content, MemoCategory.IDEA);
    }
}