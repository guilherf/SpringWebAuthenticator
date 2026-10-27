package net.bifewagyu.authenticator.repository;


import net.bifewagyu.authenticator.models.EntityUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<EntityUser,Long> {




}
