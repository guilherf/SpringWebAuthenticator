package net.bifewagyu.authenticator.service;


import net.bifewagyu.authenticator.dtos.request.AdminRequest;
import net.bifewagyu.authenticator.dtos.request.LoginRequest;
import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.AdminResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.models.RoleModels;
import net.bifewagyu.authenticator.repository.RoleRepository;
import net.bifewagyu.authenticator.repository.UserRepository;
import net.bifewagyu.authenticator.security.WebSecurity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final JwtTokenService jwtTokenService;

    private final WebSecurity webSecurity;

    public AdminService(UserRepository userRepository, RoleRepository roleRepository, JwtTokenService jwtTokenService, WebSecurity webSecurity) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenService = jwtTokenService;
        this.webSecurity = webSecurity;
    }


    public AdminResponse promoverParaAdmin (Long userId) {

        EntityUser user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Usuario não encontrado"));


        RoleModels role = roleRepository.findByNames("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("o cargo ROLE_ADMIN não foi encontrado"));


        user.getRoles().add(role);


        return new AdminResponse(userId, user.getNome(), user.getEmail(), user.getRoles());
    }

















}
