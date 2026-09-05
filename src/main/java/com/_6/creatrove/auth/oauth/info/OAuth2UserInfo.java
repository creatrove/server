package com._6.creatrove.auth.oauth.info;

public interface OAuth2UserInfo {
    String getEmail();
    String getName();
    String getProviderId();
}