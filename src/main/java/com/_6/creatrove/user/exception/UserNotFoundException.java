package com._6.creatrove.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("존재하지 않는 회원입니다. id=" + userId);
    }
}