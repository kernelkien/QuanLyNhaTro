package com.example.product.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/createAdmin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register-tenant").hasRole("ADMIN")
                                // ADMIN
                                .requestMatchers(HttpMethod.GET, "/bill/room/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/bill/tenant/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/bill/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/bill/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/bill/**").hasRole("ADMIN")

                                .requestMatchers("/room/**").hasRole("ADMIN")
                                .requestMatchers("/tenant/**").hasRole("ADMIN")

                                // TENANT
                                .requestMatchers(HttpMethod.GET, "/bill/myBill").hasRole("TENANT")

                                .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
