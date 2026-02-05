package com.example.product.Controller;

import com.example.product.DTO.ApiResponse;
import com.example.product.DTO.RoomResponse;
import com.example.product.Service.RoomService;
import com.example.product.model.Room;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;


    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRoom (@Valid @RequestBody Room room){
        return ResponseEntity.ok(new ApiResponse<>(200, "successfully created room", roomService.createRoom(room)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllRoom (){
        return ResponseEntity.ok(new ApiResponse<>(200, null, roomService.read()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateRoom (@RequestBody Room room, @PathVariable Long id){
        return ResponseEntity.ok(new ApiResponse<>(200, "updated room", roomService.updateRoom(id, room)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRoom(@PathVariable Long id){
        roomService.deleteRoom(id);
        return  ResponseEntity.ok(new ApiResponse<>(200, "room deleted", null));
    }

    @PostMapping("/{roomId}/assign-tenant/{tenantId}")
    public ResponseEntity<ApiResponse<?>> assignTenant (@PathVariable Long roomId, @PathVariable Long tenantId){
        return ResponseEntity.ok(new ApiResponse<>(200, "tenant assigned to room", roomService.assignTenant(roomId, tenantId)));
    }

    @PostMapping("/{roomId}/remove-tenant")
    public ResponseEntity<ApiResponse<?>> removeTenant (@PathVariable Long roomId){
        return ResponseEntity.ok(new ApiResponse<>(200, "tenant removed", roomService.removeTenant(roomId)));
    }

}
