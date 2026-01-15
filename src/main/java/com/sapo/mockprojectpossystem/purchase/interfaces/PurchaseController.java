package com.sapo.mockprojectpossystem.purchase.interfaces;

import com.sapo.mockprojectpossystem.common.response.MessageResponse;
import com.sapo.mockprojectpossystem.purchase.application.implement.PurchaseService;
import com.sapo.mockprojectpossystem.purchase.application.request.PurchaseQueryParams;
import com.sapo.mockprojectpossystem.purchase.application.request.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    // Lấy danh sách đơn hàng, có sorting và tìm kiếm
    @PreAuthorize("hasAnyRole('OWNER', 'SALES', 'CS')")
    @GetMapping
    public ResponseEntity<?> getAllPurchases(PurchaseQueryParams query) {
        try {
            return ResponseEntity.ok(purchaseService.getAllPurchase(query));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy đơn hàng theo ID
    @PreAuthorize("hasAnyRole('OWNER', 'SALES', 'CS')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(purchaseService.getPurchaseById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Thêm đơn hàng mới
    @PreAuthorize("hasAnyRole('OWNER', 'SALES')")
    @PostMapping
    public ResponseEntity<?> addPurchase(@RequestBody PurchaseRequest request) {
        try {
            purchaseService.createPurchase(request);
            return ResponseEntity.ok(new MessageResponse("Purchase added successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
