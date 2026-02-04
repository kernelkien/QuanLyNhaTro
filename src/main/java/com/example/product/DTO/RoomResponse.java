package com.example.product.DTO;

import lombok.*;

@Getter
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomName;
    private Long tenantId;
    private String tenantName;
    private Double roomPrice;
    private String status;
}
