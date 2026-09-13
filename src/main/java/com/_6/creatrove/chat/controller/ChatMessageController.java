package com._6.creatrove.chat.controller;

import com._6.creatrove.chat.dto.ChatMessageRequest;
import com._6.creatrove.chat.dto.ChatMessageResponse;
import com._6.creatrove.chat.dto.DuplicateResolutionRequest;
import com._6.creatrove.chat.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/chats/messages")
    public ResponseEntity<ChatMessageResponse> send(@AuthenticationPrincipal Long userId,
                                                    @Valid @RequestBody ChatMessageRequest request) {
        ChatMessageResponse response = chatMessageService.sendMessage(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/chats/messages/{messageId}/duplicate-resolution")
    public ChatMessageResponse resolveDuplicate(@AuthenticationPrincipal Long userId,
                                                @PathVariable Long messageId,
                                                @RequestBody DuplicateResolutionRequest request) {
        return chatMessageService.resolveDuplicate(userId, messageId, request);
    }
}