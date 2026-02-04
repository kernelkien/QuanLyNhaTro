package com.example.product.Service;

import com.example.product.DTO.AuthResponse;
import com.example.product.DTO.LoginRequest;
import com.example.product.DTO.RegisterTenantRequest;
import com.example.product.Repository.TenantRepository;
import com.example.product.Repository.UserRepository;
import com.example.product.model.Role;
import com.example.product.model.Tenant;
import com.example.product.model.User;
import com.example.product.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void createAdmin() {
        if (!userRepository.existsByUsernameAndTenantIsNull("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setTenant(null);

            userRepository.save(admin);
        }
    }

    public AuthResponse login(LoginRequest request){

        User user = request.getTenantId() == null
                ? userRepository.findByUsernameAndTenantIsNull(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                : userRepository.findByUsernameAndTenant_Id(
                request.getUsername(),
                request.getTenantId()
        ).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse registerTenant(RegisterTenantRequest request) {

        Tenant tenant = Tenant.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .build();

        tenantRepository.save(tenant);

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.TENANT)
                .tenant(tenant)
                .build();

        userRepository.save(user);

        return new AuthResponse(jwtUtil.generateToken(user));
    }
}
