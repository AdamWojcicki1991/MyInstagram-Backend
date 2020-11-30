package com.myinstagram.domain.dto;

import lombok.*;

import java.time.LocalDate;

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
    private final LocalDate postDate;
    private final Long userId;
    private final String login;
}
