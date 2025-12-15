package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Purchase;
import com.sapo.mockprojectpossystem.request.PurchaseRequest;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.response.PurchaseResponse;
import com.sapo.mockprojectpossystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    // Lấy danh sách đơn hàng, có sorting và tìm kiếm
    @PreAuthorize("hasAnyRole('OWNER', 'SALES', 'CS')")
    @GetMapping
    public ResponseEntity<?> getAllPurchases(@RequestParam(required = false) String keyword,
                                             @RequestParam(required = false) Integer customerId,
                                             @RequestParam(required = false) Integer userId,
                                             @RequestParam(required = false) Double minTotal,
                                             @RequestParam(required = false) Double maxTotal,
                                             @RequestParam(required = false) Double minDiscount,
                                             @RequestParam(required = false) Double maxDiscount,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate,
                                             @RequestParam(required = false) Integer productId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "createdAt") String sortBy,
                                             @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            Page<Purchase> purchasePage = purchaseService.getAllPurchase(keyword, customerId, userId,
                    minTotal, maxTotal, minDiscount, maxDiscount,
                    startDate, endDate, productId, page, size, sortBy, sortDir
            );

            List<PurchaseResponse> purchases =
                    purchasePage.stream().map(PurchaseResponse::new).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("purchases", purchases);
            response.put("currentPage", purchasePage.getNumber());
            response.put("totalItems", purchasePage.getTotalElements());
            response.put("totalPages", purchasePage.getTotalPages());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy đơn hàng theo ID
    @PreAuthorize("hasAnyRole('OWNER', 'SALES', 'CS')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Integer id) {
        try {
            Purchase purchases = purchaseService.getPurchaseById(id);
            return ResponseEntity.ok(new PurchaseResponse(purchases));
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
