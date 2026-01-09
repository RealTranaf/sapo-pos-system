package com.sapo.mockprojectpossystem.product.interfaces.rest;

import com.sapo.mockprojectpossystem.product.domain.model.Type;
import com.sapo.mockprojectpossystem.product.interfaces.response.TypeLongResponse;
import com.sapo.mockprojectpossystem.common.response.MessageResponse;
import com.sapo.mockprojectpossystem.product.application.implement.TypeService;
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
@RequestMapping("/types")
public class TypeController {
    private final TypeService typeService;

    // Lấy danh sách Type
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @GetMapping
    public ResponseEntity<?> getAllTypes(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Type> typePage = typeService.getAllType(page, size);
            List<TypeLongResponse> typeResponses = typePage.stream().map(TypeLongResponse::new).collect(Collectors.toList());
            response.put("types", typeResponses);
            response.put("currentPage", typePage.getNumber());
            response.put("totalItems", typePage.getTotalElements());
            response.put("totalPages", typePage.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Lấy type theo ID
    @PreAuthorize("hasAnyRole('OWNER', 'WH', 'SALES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeById(@PathVariable Integer id) {
        try {
            Type type = typeService.getTypeById(id);
            return ResponseEntity.ok(new TypeLongResponse(type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Thêm type mới
    @PreAuthorize("hasAnyRole('OWNER', 'WH')")
    @PostMapping
    public ResponseEntity<?> addType(@RequestParam String name) {
        try {
            typeService.createType(name);
            return ResponseEntity.ok("Type added successfully!");
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
            typeService.updateType(id, name);
            return ResponseEntity.ok("Type updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
