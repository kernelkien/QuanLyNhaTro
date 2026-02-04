package com.example.product.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RegisterTenantRequest {
    private String username;
    private String password;
    private String name;
    private String phone;
}

