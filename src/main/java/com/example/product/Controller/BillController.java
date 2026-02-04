package com.example.product.Controller;

import com.example.product.DTO.ApiResponse;
import com.example.product.DTO.CreateBillRequest;
import com.example.product.Service.BillService;
import com.example.product.model.User;
import com.example.product.util.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bill")
public class BillController {
    private final BillService billService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createBill (@RequestBody CreateBillRequest createBillRequest) {
        return ResponseEntity.ok(new ApiResponse<>(200, "bill created", billService.createBill(
                createBillRequest.getRoomId(),
                createBillRequest.getMonth(),
                createBillRequest.getYear(),
                createBillRequest.getElectricPrice(),
                createBillRequest.getWaterPrice(),
                createBillRequest.getOtherPrice()
        )));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<ApiResponse<?>> getBillsByRoom (@PathVariable long roomId,
                                                          @RequestBody(required = false) int month,
                                                          @RequestBody(required = false) int year){
        return ResponseEntity.ok(new ApiResponse<>
                (200, "get bill by room", billService.getBillsByRoom(roomId, month, year)));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<ApiResponse<?>> getBillsByTenant (@PathVariable long tenantId,
                                                            @RequestBody(required = false) int month,
                                                            @RequestBody(required = false) int year){
        return ResponseEntity.ok(new ApiResponse<>
                (200, "get bill by tenant", billService.getBillByTenant(tenantId, month, year)));
    }

    @GetMapping("/myBill")
    public ResponseEntity<ApiResponse<?>> getMyBill (Authentication authentication,
                                                     @RequestBody(required = false) int month,
                                                     @RequestBody(required = false) int year){
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        if (user.getTenantId() == null){
            throw new RuntimeException("Tenant not found");
        }

        return ResponseEntity.ok(new ApiResponse<>
                (200, "my bills", billService.getBillByTenant(user.getTenantId(), month, year)));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<?>> payBill (@PathVariable long id){
        return ResponseEntity.ok(new ApiResponse<>(200, "bill is paid", billService.payBill(id)));
    }

}
