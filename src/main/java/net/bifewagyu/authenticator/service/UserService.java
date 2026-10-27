package net.bifewagyu.authenticator.service;


import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.UserResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserResponse salvarUsuario(UserRequest userRequest) {

        EntityUser user = new EntityUser();
        user.setNome(userRequest.nome());
        user.setEmail(userRequest.email());
        user.setPassword(userRequest.password());

        EntityUser salvarUsuario = userRepository.save(user);

        return new UserResponse(salvarUsuario.getId(), salvarUsuario.getNome(), salvarUsuario.getEmail());

    }




}
