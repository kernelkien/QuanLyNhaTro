package com.example.product.Service;

import com.example.product.DTO.RoomResponse;
import com.example.product.Repository.RoomRepository;
import com.example.product.Repository.TenantRepository;
import com.example.product.model.Room;
import com.example.product.model.RoomStatus;
import com.example.product.model.Tenant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;

    public RoomResponse createRoom(Room newRoom) {
        if (newRoom.getRoomPrice() == null || newRoom.getRoomPrice() < 0) {
            throw new RuntimeException("invalid room price");
        }

        Room room = new Room();
        room.setName(newRoom.getName());
        room.setRoomPrice(newRoom.getRoomPrice());
        room.setStatus(RoomStatus.TRONG);
        roomRepository.save(room);

        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getTenant() != null ? room.getTenant().getId() : null,
                room.getTenant() != null ? room.getTenant().getName() : null,
                room.getRoomPrice(),
                room.getStatus().name()
        );
    }

    public List<RoomResponse> read(){
        return roomRepository.findAll().stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getName(),
                        room.getTenant() != null ? room.getTenant().getId() : null,
                        room.getTenant() != null ? room.getTenant().getName() : null,
                        room.getRoomPrice(),
                        room.getStatus().name()
                ))
                .toList();
    }


    public RoomResponse updateRoom(Long roomId, Room newRoom) {
        if (newRoom.getRoomPrice() == null || newRoom.getRoomPrice() < 0) {
            throw new RuntimeException("invalid room price");
        }

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RuntimeException("room not found"));

        room.setName(newRoom.getName());
        room.setRoomPrice(newRoom.getRoomPrice());

        roomRepository.save(room);

        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getTenant() != null ? room.getTenant().getId() : null,
                room.getTenant() != null ? room.getTenant().getName() : null,
                room.getRoomPrice(),
                room.getStatus().name()
        );
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("room not found"));

        if (room.getStatus() == RoomStatus.DANG_THUE) {
            throw new RuntimeException("Cannot delete room while it is being rented");
        }

        roomRepository.deleteById(roomId);
    }

    public RoomResponse assignTenant(Long roomId, Long tenantId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getStatus() == RoomStatus.DANG_THUE) {
            throw new RuntimeException("Room already rented");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        boolean tenantAlreadyRenting =
                roomRepository.existsByTenantIdAndStatus(tenantId, RoomStatus.DANG_THUE);

        if (tenantAlreadyRenting) {
            throw new RuntimeException("Tenant is already renting another room");
        }

        room.setTenant(tenant);
        room.setStatus(RoomStatus.DANG_THUE);

        roomRepository.save(room);

        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getTenant().getId(),
                room.getTenant().getName(),
                room.getRoomPrice(),
                room.getStatus().name()
        );
    }

    public RoomResponse removeTenant(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setTenant(null);
        room.setStatus(RoomStatus.TRONG);

        roomRepository.save(room);

        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getTenant() != null ? room.getTenant().getId() : null,
                room.getTenant() != null ? room.getTenant().getName() : null,
                room.getRoomPrice(),
                room.getStatus().name()
        );
    }
}
