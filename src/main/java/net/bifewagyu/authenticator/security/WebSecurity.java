package net.bifewagyu.authenticator.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final AuthenticationFilter authenticationFilter;

    public WebSecurity(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }


    public static final String [] ENDPOINTS_SEM_AUTENTIFICACAO = {
        "/h2-console/**",
            "/user/login",
            "/user/register"
    };


    public static final String [] ENDPOINTS_COM_AUTENTIFICACAO = {


            "/admin/**"

    };


    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        HttpSecurity httpSecurity = http.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers(ENDPOINTS_SEM_AUTENTIFICACAO).permitAll()
                        .requestMatchers(ENDPOINTS_COM_AUTENTIFICACAO).hasRole("ADMIN").anyRequest().denyAll()
                ).headers(httpSecurityHeadersConfigurer ->
                httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
