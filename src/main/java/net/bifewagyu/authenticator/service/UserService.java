package net.bifewagyu.authenticator.service;


import net.bifewagyu.authenticator.details.UserDetailsimpl;
import net.bifewagyu.authenticator.dtos.request.LoginRequest;
import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.RecoveryJwtTokenDto;
import net.bifewagyu.authenticator.dtos.response.UserResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.models.RoleModels;
import net.bifewagyu.authenticator.repository.UserRepository;
import net.bifewagyu.authenticator.roles.RolesNames;
import net.bifewagyu.authenticator.security.WebSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    public UserService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, WebSecurity webSecurity, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.webSecurity = webSecurity;
        this.userRepository = userRepository;
    }


    public UserResponse salvarUsuario(UserRequest userRequest) {

      EntityUser user = EntityUser.builder()
              .nome(userRequest.nome())
              .email(userRequest.email())
              .password(webSecurity.passwordEncoder().encode(userRequest.password()))
                      .roles(List.of(RoleModels.builder().names(userRequest.role()).build()))
                              .build();

      userRepository.save(user);

      return new UserResponse(user.getId(), user.getNome(), user.getEmail(), user.getRoles());

    }


    public RecoveryJwtTokenDto authenticateUser(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());


        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsimpl userDetailsimpl =  (UserDetailsimpl) authentication.getPrincipal();

        return new RecoveryJwtTokenDto(jwtTokenService.generetToken(userDetailsimpl));

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




}
