package net.bifewagyu.authenticator.service;


import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.UserResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponse salvarUsuario(UserRequest userRequest) {

        EntityUser user = new EntityUser();
        user.setNome(userRequest.nome());
        user.setEmail(userRequest.email());
        String password = passwordEncoder.encode(userRequest.password());
        user.setPassword(password);

        EntityUser salvarUsuario = userRepository.save(user);

        return new UserResponse(salvarUsuario.getId(), salvarUsuario.getNome(), salvarUsuario.getEmail());

    }


    public UserResponse removerUsuario(EntityUser user) {
        userRepository.delete(user);


        return new UserResponse(user.getId(), user.getNome(), user.getEmail() + "REMOVED");
    }




}
