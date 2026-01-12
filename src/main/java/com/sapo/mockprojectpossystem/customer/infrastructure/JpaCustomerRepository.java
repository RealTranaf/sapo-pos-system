package com.sapo.mockprojectpossystem.customer.infrastructure;

import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.repository.CustomerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface JpaCustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByPhoneNum(String phoneNum);
}
