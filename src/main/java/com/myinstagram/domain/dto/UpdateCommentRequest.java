package com.myinstagram.domain.dto;

import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class UpdateCommentRequest {
    private Long commentId;
    private String commentName;
    private String content;
}
