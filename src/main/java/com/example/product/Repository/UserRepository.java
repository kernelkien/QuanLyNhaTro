package com.example.product.Repository;

import com.example.product.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsernameAndTenant_Id(String username, Long tenantId);

    Optional<User> findByUsernameAndTenant_Id(String username, Long tenantId);

    boolean existsByUsernameAndTenantIsNull(String username);

    Optional<User> findByUsernameAndTenantIsNull(String username);
}
