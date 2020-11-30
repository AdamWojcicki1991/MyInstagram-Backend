package com.myinstagram.domain.dto;

import lombok.*;

import java.time.LocalDate;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor
public final class CommentDto {
    private final Long id;
    private final String commentName;
    private final String content;
    private final LocalDate commentDate;
    private final Long postId;
    private final String postName;
    private final Long userId;
}
