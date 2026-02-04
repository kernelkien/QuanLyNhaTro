package com.example.product.util;

import com.example.product.Repository.UserRepository;
import com.example.product.model.Role;
import com.example.product.model.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/createAdmin") || path.startsWith("/auth/login");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = jwtUtil.extractClaims(token);

        String username = claims.getSubject();

        Role role = Role.valueOf(claims.get("role", String.class));

        User user = role == Role.ADMIN
                ? userRepository.findByUsernameAndTenantIsNull(username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                : userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long tenantId = claims.get("tenant_id", Long.class);

        UserPrincipal principal = new UserPrincipal(
                user.getId(),
                tenantId,
                username,
                null,
                user.getRole()
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}