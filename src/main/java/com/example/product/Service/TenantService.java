package com.example.product.Service;

import com.example.product.DTO.TenantResponse;
import com.example.product.Repository.RoomRepository;
import com.example.product.Repository.TenantRepository;
import com.example.product.model.Room;
import com.example.product.model.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final RoomRepository roomRepository;

    public List<TenantResponse> readAll() {
        return tenantRepository.findAll().stream()
                .map(tenant -> {
                    Room room = roomRepository.findByTenantId(tenant.getId()).orElse(null);

                    return new TenantResponse(
                            tenant.getId(),
                            tenant.getName(),
                            tenant.getPhone(),
                            room != null ? room.getId() : null,
                            room != null ? room.getName() : null
                    );
                })
                .toList();
    }

    public TenantResponse update(Long id, Tenant newTenant) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        Room room = roomRepository.findByTenantId(tenant.getId()).orElse(null);

        tenant.setName(newTenant.getName());
        tenant.setPhone(newTenant.getPhone());

        tenantRepository.save(tenant);

        return new TenantResponse(
                tenant.getId(),
                tenant.getName(),
                tenant.getPhone(),
                room != null ? room.getId() : null,
                room != null ? room.getName() : null
        );
    }

    public void delete(Long id) {
        tenantRepository.deleteById(id);
    }

}
