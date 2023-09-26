package com.picnic.security;

// imports

import com.picnic.security.JwtConverter;
import com.picnic.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    // new... add the parameter: `AuthenticationConfiguration authConfig`
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {

        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .requestMatchers("/authenticate").permitAll()
                .requestMatchers("/signup").permitAll()
                .requestMatchers("/api/stories", "/api/stories/*").permitAll()
                .requestMatchers("/api/stories/delete", "/api/stories/delete/*").permitAll()
                .requestMatchers("/api/comments", "/api/comments/*").permitAll()
                .requestMatchers("/api/votes", "/api/votes/*").permitAll()
                .requestMatchers("/**").denyAll()
                .and()
                // New...
                .addFilterAfter(new JwtRequestFilter(converter), BasicAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
