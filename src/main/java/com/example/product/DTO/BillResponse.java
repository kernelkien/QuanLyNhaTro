package com.example.product.DTO;

import lombok.*;

@Getter
@AllArgsConstructor
public class BillResponse {
    private Long id;

    private Long roomId;
    private String roomName;

    private Long tenantId;
    private String tenantName;

    private int month;
    private int year;

    private Double electricPrice;
    private Double waterPrice;
    private Double otherPrice;

    private Double totalPrice;

    private boolean paid;
}
