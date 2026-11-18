package net.bifewagyu.authenticator.repository;

import net.bifewagyu.authenticator.models.EntityAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<EntityAdmin, Long> {


    Optional<EntityAdmin> findByEmail(String email);
    Optional<EntityAdmin> findById(Long id);



}
