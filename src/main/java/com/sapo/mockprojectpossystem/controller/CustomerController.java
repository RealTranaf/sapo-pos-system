package com.sapo.mockprojectpossystem.controller;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Gender;
import com.sapo.mockprojectpossystem.response.CustomerResponse;
import com.sapo.mockprojectpossystem.response.MessageResponse;
import com.sapo.mockprojectpossystem.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Lấy danh sách customer với chức năng sorting và tìm kiếm
    @PreAuthorize("hasAnyRole('OWNER', 'CS', 'SALES')")
    @GetMapping
    public ResponseEntity<?> getAllCustomers(@RequestParam(required = false) String keyword,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate,
                                             @RequestParam(required = false) Double minAmount,
                                             @RequestParam(required = false) Double maxAmount,
                                             @RequestParam(defaultValue = "lastPurchaseDate") String sortBy,
                                             @RequestParam(defaultValue = "desc") String sortDir,
                                             @RequestParam(required = false) Gender gender) {
        try {
            Map<String, Object> response = new HashMap<>();
            Page<Customer> customerPage = customerService.getAllCustomer(keyword, page, size, startDate, endDate, minAmount, maxAmount, sortBy, sortDir, gender);
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

    // Lấy customer theo ID
    @PreAuthorize("hasAnyRole('OWNER', 'CS', 'SALES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Integer id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(new CustomerResponse(customer));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // Thêm customer mới
    @PreAuthorize("hasAnyRole('OWNER', 'CS', 'SALES')")
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

    // Cập nhật customer
    @PreAuthorize("hasAnyRole('OWNER', 'CS')")
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
