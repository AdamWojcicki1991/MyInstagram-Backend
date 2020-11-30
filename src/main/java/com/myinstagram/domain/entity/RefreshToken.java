package com.myinstagram.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.SEQUENCE;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "REFRESH_TOKENS")
public final class RefreshToken {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(name = "REFRESH_TOKEN")
    private String token;

    @Column(name = "CREATE_DATE")
    private Instant createdDate;
}
