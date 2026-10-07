package com._6.creatrove.trove.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TroveItemDto(
        TroveItemType type,
        Long id,
        String title,       // 메모 내용 요약 / 캘린더 제목
        LocalDate date,      // 캘린더 startDate / 장부 entryDate / 메모는 createdAt 날짜
        String item,         // 장부 항목명 (장부일 때만)
        Long amount,         // 장부 금액 (장부일 때만)
        String status,       // 장부 완료/예정 상태 (장부일 때만)
        String label,        // "캘린더 7/15", "장부 +500,000 예정", "메모 아이디어"
        LocalDateTime viewedAt
) {
}