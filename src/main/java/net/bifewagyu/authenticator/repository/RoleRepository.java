package net.bifewagyu.authenticator.repository;

import net.bifewagyu.authenticator.models.RoleModels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModels, Long> {


    Optional<RoleModels> findByNames(String name);


}
