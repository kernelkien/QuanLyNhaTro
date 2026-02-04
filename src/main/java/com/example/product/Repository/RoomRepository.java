package com.example.product.Repository;

import com.example.product.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByIdAndTenantId(Long id, Long tenantId);

    Optional<Room> findByTenantId(Long tenantId);
}

