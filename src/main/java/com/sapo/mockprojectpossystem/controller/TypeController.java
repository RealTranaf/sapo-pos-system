package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Type;
import com.sapo.mockprojectpossystem.response.TypeResponse;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.service.TypeService;
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
@RequestMapping("/types")
public class TypeController {
    private final TypeService typeService;

    @GetMapping
    public ResponseEntity<?> getAllTypes(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Type> typePage = typeService.getAllType(page, size);
            List<TypeResponse> typeResponses = typePage.stream().map(TypeResponse::new).collect(Collectors.toList());
            response.put("types", typeResponses);
            response.put("currentPage", typePage.getNumber());
            response.put("totalItems", typePage.getTotalElements());
            response.put("totalPages", typePage.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeById(@PathVariable Integer id) {
        try {
            Type type = typeService.getTypeById(id);
            return ResponseEntity.ok(new TypeResponse(type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addType(@RequestParam String name) {
        try {
            typeService.createType(name);
            return ResponseEntity.ok("Type added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

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
