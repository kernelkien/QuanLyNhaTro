package com.example.product.DTO;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
public class CreateBillRequest {
    private Long roomId;
    private int month;
    private int year;
    private Double electricPrice;
    private Double waterPrice;
    private Double otherPrice;
}

