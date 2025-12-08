package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Gender;
import com.sapo.mockprojectpossystem.response.CustomerResponse;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(@RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate,
                                             @RequestParam(defaultValue = "lastPurchaseDate") String sortBy,
                                             @RequestParam(defaultValue = "desc") String sortDir,
                                             @RequestParam(required = false) Gender gender) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Customer> customerPage = customerService.getAllCustomer(keyword, page, size, startDate, endDate, sortBy, sortDir, gender);
            List<CustomerResponse> customerResponses = customerPage.stream().map(CustomerResponse::new).collect(Collectors.toList());
            response.put("customers", customerResponses);
            response.put("currentPage", customerPage.getNumber());
            response.put("totalItems", customerPage.getTotalElements());
            response.put("totalPages", customerPage.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(new CustomerResponse(customer));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestParam String name,
                                         @RequestParam String phoneNum,
                                         @RequestParam(required = false) Gender gender,
                                         @RequestParam(required = false) String note) {
        try {
            customerService.createCustomer(name, phoneNum, gender, note);
            return ResponseEntity.ok("Customer added successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id,
                                            @RequestParam String name,
                                            @RequestParam String phoneNum,
                                            @RequestParam(required = false) Gender gender,
                                            @RequestParam(required = false) String note) {
        try {
            customerService.updateCustomer(id, name, phoneNum, gender, note);
            return ResponseEntity.ok("Customer updated successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }


}
