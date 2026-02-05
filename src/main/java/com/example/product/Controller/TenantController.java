package com.example.product.Controller;

import com.example.product.DTO.ApiResponse;
import com.example.product.Service.TenantService;
import com.example.product.model.Tenant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant")
public class TenantController {
    private final TenantService tenantService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllTenant (){
        return ResponseEntity.ok(new ApiResponse<>(200, null, tenantService.readAll()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateTenant (@RequestBody Tenant tenant, @PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "updated tenant", tenantService.update(id, tenant)));
    }
}
