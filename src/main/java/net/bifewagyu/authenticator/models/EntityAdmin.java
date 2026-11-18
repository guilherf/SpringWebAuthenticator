package net.bifewagyu.authenticator.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "models")
@Table(name = "model")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EntityAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String nome;

    @Column(unique = true)
    String email;

    String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "admin_roles",
            joinColumns = @JoinColumn(name = "admin_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleModels> role = new HashSet<>();






}
