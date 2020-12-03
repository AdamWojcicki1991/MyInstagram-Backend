package com.myinstagram.domain.dto;

import lombok.*;

import java.time.Instant;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
public final class CommentDto {
    private final Long id;
    private final String commentName;
    private final String content;
    private final Instant commentDate;
    private final Instant updateDate;
    private final Long postId;
    private final String postName;
    private final Long userId;
}
