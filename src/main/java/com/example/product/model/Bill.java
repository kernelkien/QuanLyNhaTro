package com.example.product.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "bill", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"room_id", "month", "year"})
})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false, updatable = false)
    private Tenant tenant;

    private Integer month;
    private Integer year;

    private Double roomPrice;
    private Double electricPrice;
    private Double waterPrice;
    private Double otherPrice;
    private Double totalAmount;

    @Column(nullable = false)
    private Boolean paid = false;
}
