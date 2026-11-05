package net.bifewagyu.authenticator.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @ManyToOne(fetch = FetchType.EAGER,  cascade = CascadeType.ALL)
    @JoinTable(name = "admin_roles",
    joinColumns = @JoinColumn(name = "admin_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<RoleModels> role;





}
