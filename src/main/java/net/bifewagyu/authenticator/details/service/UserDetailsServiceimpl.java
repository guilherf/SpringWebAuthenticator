package net.bifewagyu.authenticator.details.service;

import net.bifewagyu.authenticator.details.UserDetailsimpl;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceimpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceimpl( UserRepository userRepository) {
        this.userRepository = userRepository;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EntityUser user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNames()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getNome(),
                user.getPassword(),
                authorities
        );
    }
}
