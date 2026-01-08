package com.sapo.mockprojectpossystem.customer.interfaces;

import com.sapo.mockprojectpossystem.customer.application.CustomerService;
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
            CustomerResponse customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'CS', 'SALES')")
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerRequest request) {
        try {
            CustomerResponse customer = customerService.createCustomer(request.name, request.phoneNum, request.gender, request.note);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('OWNER', 'CS')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody UpdateCustomerRequest request) {
        try {
            CustomerResponse customer = customerService.updateCustomer(id, request.name, request.phoneNum, request.gender, request.note);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
