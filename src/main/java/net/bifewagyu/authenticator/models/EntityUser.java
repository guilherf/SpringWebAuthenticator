package net.bifewagyu.authenticator.models;



import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "users")
@Entity(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EntityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String nome;

    @Column(unique = true)
    String email;

    String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "users_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    public List<RoleModels> roles;

}
