package com.myinstagram.domain.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class Mail {
    private final String mailTo;
    private final String subject;
    private final String text;
}
