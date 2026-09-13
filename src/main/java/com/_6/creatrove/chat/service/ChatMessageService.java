package com._6.creatrove.chat.service;

import com._6.creatrove.calendar.domain.CalendarEvent;
import com._6.creatrove.calendar.domain.EventSource;
import com._6.creatrove.calendar.repository.CalendarEventRepository;
import com._6.creatrove.chat.classifier.*;
import com._6.creatrove.chat.domain.ChatMessage;
import com._6.creatrove.chat.domain.ClassifiedType;
import com._6.creatrove.chat.dto.*;
import com._6.creatrove.chat.exception.ChatMessageNotFoundException;
import com._6.creatrove.chat.repository.ChatMessageRepository;
import com._6.creatrove.ledger.domain.IncomeStatus;
import com._6.creatrove.ledger.domain.LedgerEntry;
import com._6.creatrove.ledger.repository.LedgerEntryRepository;
import com._6.creatrove.memo.domain.Memo;
import com._6.creatrove.memo.repository.MemoRepository;
import com._6.creatrove.user.domain.User;
import com._6.creatrove.user.exception.UserNotFoundException;
import com._6.creatrove.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemoRepository memoRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final MessageClassifier messageClassifier;

    @Transactional
    public ChatMessageResponse sendMessage(Long userId, ChatMessageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        ChatMessage message = chatMessageRepository.save(
                ChatMessage.builder()
                        .user(user)
                        .content(request.content())
                        .imageUrl(request.imageUrl())
                        .linkUrl(request.linkUrl())
                        .build()
        );

        ClassificationResult result = messageClassifier.classify(request.content());

        if (result.isAmbiguous()) {
            message.updateClassification(ClassifiedType.AMBIGUOUS);
            return new ChatMessageResponse(
                    message.getId(), message.getContent(), ClassifiedType.AMBIGUOUS.name(),
                    message.getCreatedAt(), null, null, result.suggestedCategories(), null
            );
        }

        List<TroveItemDto> items = new ArrayList<>();
        DuplicateCandidateDto duplicateCandidate = null;

        if (result.memo() != null) {
            Memo memo = memoRepository.save(
                    Memo.builder()
                            .user(user).sourceMessage(message)
                            .content(result.memo().content())
                            .category(result.memo().category())
                            .build()
            );
            // ★ 수정 1: label 인자 추가
            items.add(new TroveItemDto("MEMO", memo.getId(), memo.getContent(), null, null, null, null,
                    "메모 " + memo.getCategory().displayName()));
        }

        if (result.calendar() != null) {
            CalendarEvent event = calendarEventRepository.save(
                    CalendarEvent.builder()
                            .user(user).sourceMessage(message)
                            .title(result.calendar().title())
                            .eventDate(result.calendar().date())
                            .eventTime(result.calendar().time())
                            .source(EventSource.CAPTURE)
                            .build()
            );
            // ★ 수정 2: label 인자 추가 (M/d 형식)
            String dateLabel = event.getEventDate().getMonthValue() + "/" + event.getEventDate().getDayOfMonth();
            items.add(new TroveItemDto("CALENDAR", event.getId(), event.getTitle(),
                    event.getEventDate().toString(), null, null, null,
                    "캘린더 " + dateLabel));
        }

        if (result.ledger() != null) {
            LedgerDraft draft = result.ledger();

            Optional<LedgerEntry> existing = ledgerEntryRepository
                    .findByUserAndAmountAndStatus(user, draft.amount(), IncomeStatus.SCHEDULED);

            if (existing.isPresent()) {
                LedgerEntry e = existing.get();
                duplicateCandidate = new DuplicateCandidateDto(e.getId(), e.getItem(), e.getAmount(), e.getStatus().name());
                // 장부 항목은 아직 만들지 않고 사용자 확인 대기
            } else {
                LedgerEntry entry = ledgerEntryRepository.save(
                        LedgerEntry.builder()
                                .user(user).sourceMessage(message)
                                .item(draft.item()).amount(draft.amount())
                                .status(IncomeStatus.SCHEDULED)
                                .entryDate(draft.date())
                                .build()
                );
                // ★ 수정 3: label 인자 추가
                items.add(new TroveItemDto("LEDGER", entry.getId(), null, null,
                        entry.getItem(), entry.getAmount(), entry.getStatus().name(),
                        formatLedgerLabel(entry.getAmount(), entry.getStatus())));
            }
        }

        ClassifiedType finalType = resolveFinalType(items, duplicateCandidate);
        message.updateClassification(finalType);

        return new ChatMessageResponse(
                message.getId(), message.getContent(), finalType.name(), message.getCreatedAt(),
                items.size() == 1 ? items.get(0) : null,
                items.size() > 1 ? items : null,
                null,
                duplicateCandidate
        );
    }

    @Transactional
    public ChatMessageResponse resolveDuplicate(Long userId, Long messageId, DuplicateResolutionRequest request) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .filter(m -> m.getUser().getUserId().equals(userId))
                .orElseThrow(() -> new ChatMessageNotFoundException(messageId));

        ClassificationResult result = messageClassifier.classify(message.getContent());
        LedgerDraft draft = result.ledger();
        if (draft == null) {
            throw new IllegalStateException("장부 중복 확인 대상이 아닌 메시지입니다.");
        }

        LedgerEntry entry;
        if (request.resolution() == DuplicateResolution.EXISTING) {
            entry = ledgerEntryRepository.findByUserAndAmountAndStatus(message.getUser(), draft.amount(), IncomeStatus.SCHEDULED)
                    .orElseThrow(() -> new IllegalStateException("확인할 기존 항목을 찾을 수 없습니다."));
            entry.updateStatus(IncomeStatus.COMPLETED);
        } else {
            entry = ledgerEntryRepository.save(
                    LedgerEntry.builder()
                            .user(message.getUser()).sourceMessage(message)
                            .item(draft.item()).amount(draft.amount())
                            .status(IncomeStatus.COMPLETED)
                            .entryDate(draft.date())
                            .build()
            );
        }

        message.updateClassification(ClassifiedType.LEDGER);

        // ★ 수정 4: label 인자 추가
        TroveItemDto item = new TroveItemDto("LEDGER", entry.getId(), null, null,
                entry.getItem(), entry.getAmount(), entry.getStatus().name(),
                formatLedgerLabel(entry.getAmount(), entry.getStatus()));

        return new ChatMessageResponse(
                message.getId(), message.getContent(), ClassifiedType.LEDGER.name(),
                message.getCreatedAt(), item, null, null, null
        );
    }

    private ClassifiedType resolveFinalType(List<TroveItemDto> items, DuplicateCandidateDto duplicateCandidate) {
        if (duplicateCandidate != null && items.isEmpty()) {
            return ClassifiedType.DUPLICATE;
        }
        if (duplicateCandidate != null || items.size() > 1) {
            return ClassifiedType.MULTI;
        }
        return ClassifiedType.valueOf(items.get(0).type());
    }

    private String formatLedgerLabel(Long amount, IncomeStatus status) {
        return "장부 +%,d %s".formatted(amount, status.displayName());
    }
}