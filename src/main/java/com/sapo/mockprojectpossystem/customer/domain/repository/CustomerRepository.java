package com.sapo.mockprojectpossystem.customer.domain.repository;

import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> findById(Integer id);
    Optional<Customer> findByPhoneNum(String phoneNum);
    List<Customer> findAll();
    Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);
    Customer save(Customer customer);
//    List<Customer> saveAll(List<Customer> customers);
    long count();
}
