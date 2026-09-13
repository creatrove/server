package com._6.creatrove.chat.service;

import com._6.creatrove.calendar.domain.CalendarEvent;
import com._6.creatrove.calendar.domain.EventSource;
import com._6.creatrove.calendar.repository.CalendarEventRepository;
import com._6.creatrove.chat.classifier.*;
import com._6.creatrove.chat.domain.ChatMessage;
import com._6.creatrove.chat.domain.ClassifiedType;
import com._6.creatrove.chat.dto.ChatMessageRequest;
import com._6.creatrove.chat.dto.ChatMessageResponse;
import com._6.creatrove.chat.dto.TroveItemDto;
import com._6.creatrove.chat.exception.DuplicateLedgerEntryException;
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
                    message.getCreatedAt(), null, null, result.suggestedCategories()
            );
        }

        List<TroveItemDto> items = new ArrayList<>();

        if (result.memo() != null) {
            Memo memo = memoRepository.save(
                    Memo.builder()
                            .user(user)
                            .sourceMessage(message)
                            .content(result.memo().content())
                            .category(result.memo().category())
                            .build()
            );
            items.add(new TroveItemDto("MEMO", memo.getId(), memo.getContent(), null, null, null, null));
        }

        if (result.calendar() != null) {
            CalendarEvent event = calendarEventRepository.save(
                    CalendarEvent.builder()
                            .user(user)
                            .sourceMessage(message)
                            .title(result.calendar().title())
                            .eventDate(result.calendar().date())
                            .eventTime(result.calendar().time())
                            .eventEndTime(null)
                            .source(EventSource.CAPTURE)
                            .build()
            );
            items.add(new TroveItemDto("CALENDAR", event.getId(), event.getTitle(),
                    event.getEventDate().toString(), null, null, null));
        }

        if (result.ledger() != null) {
            LedgerDraft draft = result.ledger();

            ledgerEntryRepository.findByUserAndAmountAndStatus(user, draft.amount(), IncomeStatus.SCHEDULED)
                    .ifPresent(existing -> {
                        throw new DuplicateLedgerEntryException(existing.getId(), draft.amount());
                    });

            LedgerEntry entry = ledgerEntryRepository.save(
                    LedgerEntry.builder()
                            .user(user)
                            .sourceMessage(message)
                            .item(draft.item())
                            .amount(draft.amount())
                            .status(IncomeStatus.SCHEDULED)
                            .entryDate(draft.date())
                            .build()
            );
            items.add(new TroveItemDto("LEDGER", entry.getId(), null, null,
                    entry.getItem(), entry.getAmount(), entry.getStatus().name()));
        }

        ClassifiedType finalType = items.size() > 1 ? ClassifiedType.MULTI
                : ClassifiedType.valueOf(items.get(0).type());
        message.updateClassification(finalType);

        return new ChatMessageResponse(
                message.getId(), message.getContent(), finalType.name(), message.getCreatedAt(),
                items.size() == 1 ? items.get(0) : null,
                items.size() > 1 ? items : null,
                null
        );
    }
}