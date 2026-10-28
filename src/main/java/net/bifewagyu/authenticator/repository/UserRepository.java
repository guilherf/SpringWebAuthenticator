package net.bifewagyu.authenticator.repository;


import net.bifewagyu.authenticator.models.EntityUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<EntityUser,Long> {


 Optional<EntityUser> findById(Long id);
}
