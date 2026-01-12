package com.sapo.mockprojectpossystem.customer.infrastructure;

import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class JpaCustomerRepositoryAdapter implements CustomerRepository {
    private final JpaCustomerRepository jpaRepo;

    @Override
    public Optional<Customer> findById(Integer id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Optional<Customer> findByPhoneNum(String phoneNum) {
        return jpaRepo.findByPhoneNum(phoneNum);
    }

    @Override
    public List<Customer> findAll() {
        return jpaRepo.findAll();
    }

    @Override
    public Page<Customer> findAll(Specification<Customer> spec, Pageable pageable) {
        return jpaRepo.findAll(spec, pageable);
    }

    @Override
    public Customer save(Customer customer) {
        return jpaRepo.save(customer);
    }

    @Override
    public List<Customer> saveAll(List<Customer> customers) {
        return jpaRepo.saveAll(customers);
    }

    @Override
    public long count() {
        return jpaRepo.count();
    }
}
