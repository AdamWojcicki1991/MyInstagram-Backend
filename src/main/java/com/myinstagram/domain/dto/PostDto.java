package com.myinstagram.domain.dto;

import lombok.*;

import java.time.Instant;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
public final class PostDto {
    private final Long id;
    private final String postName;
    private final String caption;
    private final String url;
    private final Long imageSerialNumber;
    private final Long likesCount;
    private final Instant postDate;
    private final Instant updateDate;
    private final Long userId;
    private final String login;
}
