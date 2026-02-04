package com.example.product.DTO;

import lombok.*;

@Getter
@AllArgsConstructor
public class TenantResponse {
    private Long id;
    private String name;
    private String phone;
    private Long roomId;
    private String roomName;
}
