package com._6.creatrove.chat.exception;

public class ChatMessageNotFoundException extends RuntimeException {
    public ChatMessageNotFoundException(Long messageId) {
        super("존재하지 않는 메시지입니다. id=" + messageId);
    }
}