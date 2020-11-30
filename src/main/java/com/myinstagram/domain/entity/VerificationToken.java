package com.myinstagram.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "VERIFICATION_TOKENS")
public final class VerificationToken {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(name = "VERIFICATION_TOKEN")
    private String token;

    @OneToOne(fetch = LAZY)
    private User user;

    @Column(name = "EXPIRATION_DATE")
    private Instant expirationDate;
}
