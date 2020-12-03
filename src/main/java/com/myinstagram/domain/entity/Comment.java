package com.myinstagram.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

import static javax.persistence.GenerationType.SEQUENCE;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Entity
@Table(name = "COMMENTS")
public final class Comment {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @NotBlank(message = "Comment name is required !")
    @Column(name = "COMMENT_NAME")
    private String commentName;

    @Lob
    @Nullable
    @Column(name = "CONTENT")
    private String content;

    @CreationTimestamp
    @NotNull(message = "Comment creation date can not be Null !")
    @Column(name = "COMMENT_DATE")
    private Instant commentDate;

    @Column(name = "UPDATE_DATE")
    private Instant updateDate;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
