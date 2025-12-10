package com.sapo.mockprojectpossystem.service;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Gender;
import com.sapo.mockprojectpossystem.repository.CustomerRepository;
import com.sapo.mockprojectpossystem.repository.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    private static final String PHONE_REGEX = "^0[2|3|5|7|8|9][0-9]{8,9}$";

    public void createCustomer(String name, String phoneNum, Gender gender, String note) {
        validateCustomer(name, phoneNum);

        Optional<Customer> optional = customerRepository.findByPhoneNum(phoneNum);
        if (optional.isPresent()) {
            throw new RuntimeException("Phone number already exists");
        }

        Customer customer = new Customer(name, phoneNum, gender, note);
        customerRepository.save(customer);
    }

    public Customer getCustomerById(Integer id) {
        Optional<Customer> optional = customerRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Customer doesn't exist");
        }
    }

    public Page<Customer> getAllCustomer(String keyword, int page, int size, String startDate, String endDate,
                                         String sortBy, String sortDir, Gender gender) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if (startDate != null && !startDate.isBlank() && endDate != null && !endDate.isBlank()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            startDateTime = start.atStartOfDay();
            endDateTime = end.plusDays(1).atStartOfDay().minusSeconds(1);
        }

        Specification<Customer> spec = Specification
                .where(CustomerSpecification.containKeyword(keyword))
                .and(CustomerSpecification.purchaseDateBetween(startDateTime, endDateTime))
                .and(CustomerSpecification.genderEquals(gender == null ? null : gender));

        return customerRepository.findAll(spec, pageable);
    }

    public void updateCustomer(Integer id, String name, String phoneNum, Gender gender, String note) {
        validateCustomer(name, phoneNum);

        Optional<Customer> optional = customerRepository.findById(id);
        if (optional.isPresent()) {
            Customer customer = optional.get();
            customer.setName(name.trim());
            customer.setPhoneNum(phoneNum.trim());
            customer.setGender(gender);
            customer.setNote(note);
            customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer doesn't exist");
        }
    }

    private void validateCustomer(String name, String phoneNum) {
        if (name == null || name.isBlank()){
            throw new RuntimeException("Name is required");
        }
        if (name.trim().length() < 2 || name.trim().length() > 50) {
            throw new RuntimeException("Name must be 2–50 characters");
        }

        if (phoneNum == null || !phoneNum.matches(PHONE_REGEX)) {
            throw new RuntimeException("Phone number is invalid");
        }
    }
}
