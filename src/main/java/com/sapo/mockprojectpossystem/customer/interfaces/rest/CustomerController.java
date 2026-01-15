package com.sapo.mockprojectpossystem.customer.interfaces.rest;

import com.sapo.mockprojectpossystem.common.response.MessageResponse;
import com.sapo.mockprojectpossystem.customer.application.implement.CustomerService;
import com.sapo.mockprojectpossystem.customer.application.request.CreateCustomerRequest;
import com.sapo.mockprojectpossystem.customer.application.request.CustomerQueryParams;
import com.sapo.mockprojectpossystem.customer.application.request.UpdateCustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasAnyRole('OWNER', 'CS', 'SALES')")
    @GetMapping
    public ResponseEntity<?> getAllCustomers(@ModelAttribute CustomerQueryParams query) {
        try {
            return ResponseEntity.ok(customerService.getAllCustomers(query));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'CS', 'SALES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(customerService.getCustomerById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'CS', 'SALES')")
    @PostMapping
    public ResponseEntity<?> createCustomer(@ModelAttribute CreateCustomerRequest request) {
        try {
            return ResponseEntity.ok(customerService.createCustomer(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'CS')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @ModelAttribute UpdateCustomerRequest request) {
        try {
            return ResponseEntity.ok(customerService.updateCustomer(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
