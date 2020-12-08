package com.myinstagram.domain.dto;

import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long userId;
    private String userName;
    private String email;
    private String description;
}
