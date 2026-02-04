package com.example.product.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginRequest {
    private String username;
    private String password;
    private Long tenantId;
}

