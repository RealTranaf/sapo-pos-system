package com.sapo.mockprojectpossystem.product.interfaces.rest;

import com.sapo.mockprojectpossystem.product.application.request.TypeQueryParams;
import com.sapo.mockprojectpossystem.common.response.MessageResponse;
import com.sapo.mockprojectpossystem.product.application.implement.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/types")
public class TypeController {
    private final TypeService typeService;

    // Lấy danh sách Type
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @GetMapping
    public ResponseEntity<?> getAllTypes(@ModelAttribute TypeQueryParams query) {
        try {
            return ResponseEntity.ok(typeService.getAllType(query));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy type theo ID
    @PreAuthorize("hasAnyRole('OWNER', 'WH', 'SALES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(typeService.getTypeById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Thêm type mới
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @PostMapping
    public ResponseEntity<?> addType(@RequestParam String name) {
        try {
            return ResponseEntity.ok(typeService.createType(name));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Cập nhật type
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateType(@PathVariable Integer id,
                                         @RequestParam String name) {
        try {
            return ResponseEntity.ok(typeService.updateType(id, name));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
