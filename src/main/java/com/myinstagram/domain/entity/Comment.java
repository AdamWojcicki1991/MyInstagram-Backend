package com.myinstagram.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static javax.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
    private LocalDate commentDate;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
