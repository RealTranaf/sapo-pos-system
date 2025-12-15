package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Brand;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.response.BrandResponse;
import com.sapo.mockprojectpossystem.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    // Lấy hết danh sách các brand
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @GetMapping
    public ResponseEntity<?> getAllBrands(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Brand> brandPage = brandService.getAllBrand(page, size);
            List<BrandResponse> brandResponses = brandPage.stream().map(BrandResponse::new).collect(Collectors.toList());
            response.put("brands", brandResponses);
            response.put("currentPage", brandPage.getNumber());
            response.put("totalItems", brandPage.getTotalElements());
            response.put("totalPages", brandPage.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy brand theo ID
    @PreAuthorize("hasAnyRole('OWNER', 'WH', 'SALES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Integer id) {
        try {
            Brand brand = brandService.getBrandById(id);
            return ResponseEntity.ok(new BrandResponse(brand));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Thêm brand mới (cần param name)
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @PostMapping
    public ResponseEntity<?> addBrand(@RequestParam String name) {
        try {
            brandService.createBrand(name);
            return ResponseEntity.ok("Brand added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Cập nhật brand (cần param name)
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable Integer id,
                                         @RequestParam String name) {
        try {
            brandService.updateBrand(id, name);
            return ResponseEntity.ok("Brand updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
