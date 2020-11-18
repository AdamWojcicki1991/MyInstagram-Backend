package com.myinstagram.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Entity
@Table(name = "ROLES")
public final class Role {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "Role name is required !")
    @Column(name = "ROLE_NAME")
    private String roleName;

    @ManyToMany
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")}
    )
    private Set<User> users;
}
