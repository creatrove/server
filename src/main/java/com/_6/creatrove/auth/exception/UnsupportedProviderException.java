package com._6.creatrove.auth.exception;

public class UnsupportedProviderException extends RuntimeException {
    public UnsupportedProviderException(String registrationId) {
        super("지원하지 않는 provider 입니다: " + registrationId);
    }
}