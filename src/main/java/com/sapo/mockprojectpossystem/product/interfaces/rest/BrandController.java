package com.sapo.mockprojectpossystem.product.interfaces.rest;
import com.sapo.mockprojectpossystem.common.response.MessageResponse;
import com.sapo.mockprojectpossystem.product.interfaces.request.BrandQueryParams;
import com.sapo.mockprojectpossystem.product.application.implement.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    // Lấy hết danh sách các brand
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @GetMapping
    public ResponseEntity<?> getAllBrands(@ModelAttribute BrandQueryParams query) {
        try {
            return ResponseEntity.ok(brandService.getAllBrand(query));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy brand theo ID
    @PreAuthorize("hasAnyRole('OWNER', 'WH', 'SALES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(brandService.getBrandById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Thêm brand mới (cần param name)
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @PostMapping
    public ResponseEntity<?> addBrand(@RequestParam String name) {
        try {
            return ResponseEntity.ok(brandService.createBrand(name));
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
            return ResponseEntity.ok(brandService.updateBrand(id, name));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
