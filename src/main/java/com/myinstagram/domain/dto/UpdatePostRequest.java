package com.myinstagram.domain.dto;

import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class UpdatePostRequest {
    private Long postId;
    private String postName;
    private String caption;
}
