package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Purchase;
import com.sapo.mockprojectpossystem.request.PurchaseRequest;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.response.PurchaseResponse;
import com.sapo.mockprojectpossystem.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchasess")
public class PurchaseController {
    private final PurchaseService purchasesService;

    @GetMapping
    public ResponseEntity<?> getAllPurchase(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Purchase> purchasesPage = purchasesService.getAllPurchase(page, size);
            List<PurchaseResponse> purchasesResponses = purchasesPage.stream().map(PurchaseResponse::new).collect(Collectors.toList());
            response.put("purchasess", purchasesResponses);
            response.put("currentPage", purchasesPage.getNumber());
            response.put("totalItems", purchasesPage.getTotalElements());
            response.put("totalPages", purchasesPage.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseById(@PathVariable Integer id) {
        try {
            Purchase purchases = purchasesService.getPurchaseById(id);
            return ResponseEntity.ok(new PurchaseResponse(purchases));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addPurchase(@RequestBody PurchaseRequest request) {
        try {
            purchasesService.createPurchase(request);
            return ResponseEntity.ok(new MessageResponse("Purchase added successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
