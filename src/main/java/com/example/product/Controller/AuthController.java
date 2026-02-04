package com.example.product.Controller;

import com.example.product.DTO.ApiResponse;
import com.example.product.DTO.AuthResponse;
import com.example.product.DTO.LoginRequest;
import com.example.product.DTO.RegisterTenantRequest;
import com.example.product.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/createAdmin")
    public ResponseEntity<ApiResponse<?>> registerAdmin (){
        authService.createAdmin();
        return ResponseEntity.ok(new ApiResponse<>(201, "register admin successfully", null));
    }

    @PostMapping("/register-tenant")
    public ResponseEntity<ApiResponse<?>> registerTenant (@RequestBody RegisterTenantRequest registerTenantRequest){
        return ResponseEntity.ok(new ApiResponse<>(201, "register tenant successfully", authService.registerTenant(registerTenantRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
}
