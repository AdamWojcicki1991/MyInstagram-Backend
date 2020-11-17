package com.myinstagram.domain.entity;

import com.myinstagram.domain.util.UserStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Entity
@Table(name = "USERS")
public final class User {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @NotBlank(message = "User name is required !")
    @Column(name = "USER_NAME")
    private String userName;

    @NotBlank(message = "Login is required !")
    @Column(name = "LOGIN", unique = true)
    private String login;

    @NotBlank(message = "Password is required !")
    @Column(name = "PASSWORD")
    private String password;

    @Email
    @NotEmpty(message = "Email is required !")
    @Column(name = "EMAIL")
    private String email;

    @Nullable
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @CreationTimestamp
    @NotNull(message = "User create date cannot be Null !")
    @Column(name = "CREATE_DATE")
    private LocalDate createDate;

    @NotNull(message = "User status cannot be Null !")
    @Enumerated(STRING)
    @Column(name = "USER_STATUS")
    private UserStatus userStatus;

    @NotNull(message = "User validation status cannot be Null !")
    @Column(name = "Enabled")
    private boolean enabled;

    @OneToMany(
            targetEntity = Post.class,
            mappedBy = "user",
            fetch = LAZY
    )
    private List<Post> posts;

    @OneToMany(
            targetEntity = UserRole.class,
            mappedBy = "user",
            fetch = EAGER
    )
    private Set<UserRole> userRoles;
}
