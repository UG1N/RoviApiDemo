package com.demo.rovi.roviapidemo.model.dao.auth;

public class OAuthGenerationException extends Exception {

    public OAuthGenerationException() {
        super();
    }

    public OAuthGenerationException(String detailMessage) {
        super(detailMessage);
    }

    public OAuthGenerationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OAuthGenerationException(Throwable throwable) {
        super(throwable);
    }
}
