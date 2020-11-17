package com.myinstagram.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
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
    private Long id;

    @NotBlank(message = "Role name is required !")
    @Column(name = "ROLE_NAME")
    private String roleName;

    @OneToMany(
            targetEntity = UserRole.class,
            mappedBy = "role",
            fetch = LAZY
    )
    private Set<UserRole> userRoles;
}
