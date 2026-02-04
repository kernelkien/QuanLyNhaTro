package com.example.product.Repository;

import com.example.product.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByRoomIdAndTenantIdAndMonthAndYear(
            Long roomId,
            Long tenantId,
            int month,
            int year
    );

    List<Bill> findByRoomId(Long roomId);

    List<Bill> findByRoomIdAndMonthAndYear(Long roomId, int month, int year);

    List<Bill> findByTenantId(Long tenantId);

    List<Bill> findByTenantIdAndMonthAndYear(Long tenantId, int month, int year);
}
