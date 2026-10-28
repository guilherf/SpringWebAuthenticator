package net.bifewagyu.authenticator.service;


import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.UserResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public List<UserResponse> mostrarUsuario() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(),u.getNome(), u.getEmail())).toList();

    }


    public UserResponse removerUsuario(Long id) {
        EntityUser deleteUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario n Encontrado"));
        userRepository.delete(deleteUser);

        return new UserResponse(deleteUser.getId(), deleteUser.getNome(), deleteUser.getEmail());

    }


    public boolean matchPassword(UserRequest userRequest, String password) {
        return passwordEncoder.matches(userRequest.password(), password);
    }




}
