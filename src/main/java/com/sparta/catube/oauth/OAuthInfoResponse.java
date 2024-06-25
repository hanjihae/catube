package com.sparta.catube.oauth;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();
    OAuthProvider getOAuthProvider();
}
