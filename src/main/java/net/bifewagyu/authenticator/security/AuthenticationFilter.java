package net.bifewagyu.authenticator.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.bifewagyu.authenticator.details.UserDetailsimpl;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.repository.UserRepository;
import net.bifewagyu.authenticator.service.JwtTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public AuthenticationFilter(JwtTokenService jwtTokenService, UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkIfEndpointNotOPublic(request)) {
            String token = recoveryToken(request);
            if (token != null) {
            String subject = jwtTokenService.getSubjectFromToken(token);
            EntityUser user = userRepository.findByEmail(subject).get();
            UserDetailsimpl userDetails = new UserDetailsimpl(user);


            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new RuntimeException("Token está ausente!");
        }

        }

        filterChain.doFilter(request, response);
    }


   private boolean checkIfEndpointNotOPublic(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return !Arrays.asList(WebSecurity.ENDPOINTS_SEM_AUTENTIFICACAO).contains(uri);

   }

   public String recoveryToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
   }



}
