package com.example.product.Service;

import com.example.product.DTO.BillResponse;
import com.example.product.Repository.BillRepository;
import com.example.product.Repository.RoomRepository;
import com.example.product.Repository.TenantRepository;
import com.example.product.model.Bill;
import com.example.product.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillService {
    private final BillRepository billRepository;
    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;

    // createBillRequest
    public BillResponse createBill (Long roomId, int month, int year, Double electricPrice, Double waterPrice, Double otherPrice){
        if (month < 1 || month > 12) {
            throw new RuntimeException("invalid month");
        }

        if (year < 2000) {
            throw new RuntimeException("invalid year");
        }

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RuntimeException("room not found"));

        if (room.getTenant() == null) {
            throw new RuntimeException("room has no tenant");
        }

        billRepository.findByRoomIdAndTenantIdAndMonthAndYear(
                        roomId, room.getTenant().getId(), month, year
                )
                .ifPresent(b -> {
                    throw new RuntimeException("room already has bill for this month");
                });

        double e = electricPrice != null ? electricPrice : 0;
        double w = waterPrice != null ? waterPrice : 0;
        double o = otherPrice != null ? otherPrice : 0;

        double total = room.getRoomPrice() + e + w + o;

        Bill bill = new Bill();
        bill.setRoom(room);
        bill.setTenant(room.getTenant());
        bill.setMonth(month);
        bill.setYear(year);
        bill.setRoomPrice(room.getRoomPrice());
        bill.setElectricPrice(e);
        bill.setWaterPrice(w);
        bill.setOtherPrice(o);
        bill.setTotalAmount(total);
        bill.setPaid(false);

        billRepository.save(bill);

        return new BillResponse(
                bill.getId(),
                room.getId(),
                room.getName(),
                bill.getTenant().getId(),
                bill.getTenant().getName(),
                month,
                year,
                e,
                w,
                o,
                total,
                false
        );
    }

    public List<BillResponse> getBillsByRoom(Long roomId, Integer month, Integer year) {
        roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("room not found"));

        List<Bill> bills;

        if (month != null && year != null)
            bills = billRepository.findByRoomIdAndMonthAndYear(roomId, month, year);
        else if (month != null)
            bills = billRepository.findByRoomIdAndMonth(roomId, month);
        else if (year != null)
            bills = billRepository.findByRoomIdAndYear(roomId, year);
        else
            bills = billRepository.findByRoomId(roomId);

        return bills.stream()
                .map(bill -> new BillResponse(
                        bill.getId(),
                        bill.getRoom().getId(),
                        bill.getRoom().getName(),
                        bill.getTenant().getId(),
                        bill.getTenant().getName(),
                        bill.getMonth(),
                        bill.getYear(),
                        bill.getElectricPrice(),
                        bill.getWaterPrice(),
                        bill.getOtherPrice(),
                        bill.getTotalAmount(),
                        bill.getPaid()
                ))
                .toList();
    }

    public List<BillResponse> getBillByTenant (Long tenantId, Integer month, Integer year){
        tenantRepository.findById(tenantId).orElseThrow(() -> new RuntimeException("tenant not found"));

        List<Bill> bills;

        // add month or year search
        if (month != null && year != null)
            bills = billRepository.findByTenantIdAndMonthAndYear(tenantId, month, year);
        else if (month != null)
            bills = billRepository.findByTenantIdAndMonth(tenantId, month);
        else if (year != null)
            bills = billRepository.findByTenantIdAndYear(tenantId, year);
        else
            bills = billRepository.findByTenantId(tenantId);

        return bills.stream()
                .map(bill -> new BillResponse(
                        bill.getId(),
                        bill.getRoom().getId(),
                        bill.getRoom().getName(),
                        bill.getTenant().getId(),
                        bill.getTenant().getName(),
                        bill.getMonth(),
                        bill.getYear(),
                        bill.getElectricPrice(),
                        bill.getWaterPrice(),
                        bill.getOtherPrice(),
                        bill.getTotalAmount(),
                        bill.getPaid()
                ))
                .toList();
    }

    @Transactional
    public BillResponse payBill(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getPaid() == true) {
            throw new RuntimeException("Bill already paid");
        }

        bill.setPaid(true);
        billRepository.save(bill);

        return new BillResponse(
                bill.getId(),
                bill.getRoom().getId(),
                bill.getRoom().getName(),
                bill.getTenant().getId(),
                bill.getTenant().getName(),
                bill.getMonth(),
                bill.getYear(),
                bill.getElectricPrice(),
                bill.getWaterPrice(),
                bill.getOtherPrice(),
                bill.getTotalAmount(),
                bill.getPaid()
        );
    }
}
