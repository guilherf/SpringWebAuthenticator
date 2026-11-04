package net.bifewagyu.authenticator.service;


import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.UserResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.models.RoleModels;
import net.bifewagyu.authenticator.repository.UserRepository;
import net.bifewagyu.authenticator.roles.RolesNames;
import net.bifewagyu.authenticator.security.WebSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;


    private final WebSecurity webSecurity;


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, WebSecurity webSecurity, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.webSecurity = webSecurity;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponse salvarUsuario(UserRequest userRequest) {

      EntityUser user = EntityUser.builder()
              .nome(userRequest.nome())
              .email(userRequest.email())
              .password(passwordEncoder.encode(userRequest.password()))
                      .roles(List.of(RoleModels.builder().names(userRequest.role()).build()))
                              .build();

      userRepository.save(user);

      return new UserResponse(user.getId(), user.getNome(), user.getEmail(), user.getRoles());

    }

    public List<UserResponse> mostrarUsuario() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(),u.getNome(), u.getEmail(), u.getRoles())).toList();

    }


    public UserResponse removerUsuario(Long id) {
        EntityUser deleteUser = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario n Encontrado"));
        userRepository.delete(deleteUser);

        return new UserResponse(deleteUser.getId(), deleteUser.getNome(), deleteUser.getEmail(), deleteUser.getRoles());

    }


    public boolean matchPassword(UserRequest userRequest, String password) {
        return passwordEncoder.matches(userRequest.password(), password);
    }




}
