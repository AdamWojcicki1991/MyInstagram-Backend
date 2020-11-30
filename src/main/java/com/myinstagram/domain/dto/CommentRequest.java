package com.myinstagram.domain.dto;

import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class CommentRequest {
    private Long postId;
    private String login;
    private String content;
}
