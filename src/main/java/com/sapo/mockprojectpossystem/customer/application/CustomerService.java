package com.sapo.mockprojectpossystem.customer.application;

import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.model.Gender;
import com.sapo.mockprojectpossystem.customer.domain.model.PhoneNumber;
import com.sapo.mockprojectpossystem.customer.domain.repository.CustomerRepository;
import com.sapo.mockprojectpossystem.customer.infrastructure.CustomerSpecification;
import com.sapo.mockprojectpossystem.customer.interfaces.CustomerQueryParams;
import com.sapo.mockprojectpossystem.customer.interfaces.CustomerResponse;
import com.sapo.mockprojectpossystem.customer.interfaces.PageResponse;
import com.sapo.mockprojectpossystem.customer.util.DateTimeUtils;
import com.sapo.mockprojectpossystem.customer.util.StringUtils;
import com.sapo.mockprojectpossystem.customer.validation.CustomerValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Tạo customer từ name, phoneNum, gender và note
    public CustomerResponse createCustomer(String name, String phoneNum, Gender gender, String note) {
        CustomerValidation.validateCreate(name, phoneNum);

        customerRepository.findByPhoneNum(phoneNum)
                .ifPresent(c -> {
                    throw new RuntimeException("Phone number already exists");
                });
        Customer customer = Customer.create(name, new PhoneNumber(phoneNum), gender, note);
        Customer saved = customerRepository.save(customer);
        return new CustomerResponse(saved);
    }

    // Lấy customer theo id
    public CustomerResponse getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer does not exist"));
        return new CustomerResponse(customer);
    }

    // Lấy danh sách customer, có tìm kiếm và sorting
    // Keyword: tìm kiếm customer có name hoặc phoneNum hoặc note giống với keyword
    // startDate, endDate: tìm kiếm customer đã mua hàng trong khoản thời gian cần tìm
    // minAmount, maxAmount: tìm kiếm customer theo khoảng tổng đơn hàng đã mua
    // gender: lấy danh sách customer có gender cần tìm
    // sortBy, sortDir: sorting theo các thuộc tính của customer (kiểm tra class Customer để lấy các thuộc tính)
    public PageResponse<CustomerResponse> getAllCustomers(CustomerQueryParams query) {
        String sortField = StringUtils.hasText(query.getSortBy()) ? query.getSortBy() : "createdAt";

        Sort.Direction direction = query.getSortBy().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), Sort.by(direction, sortField));

        Instant startInstant = null;
        Instant endInstant = null;

        if (StringUtils.hasText(query.getStartDate()) && StringUtils.hasText(query.getEndDate())) {
            LocalDate start = LocalDate.parse(query.getStartDate());
            LocalDate end = LocalDate.parse(query.getEndDate());

            startInstant = DateTimeUtils.startOfDayUtc(start);
            endInstant = DateTimeUtils.endOfDayUtc(end);
        }

        Specification<Customer> spec = Specification
                .where(CustomerSpecification.containKeyword(query.getKeyword()))
                .and(CustomerSpecification.purchaseDateBetween(startInstant, endInstant))
                .and(CustomerSpecification.genderEquals(query.getGender() == null ? null : query.getGender()))
                .and(CustomerSpecification.purchaseAmountBetween(query.getMinAmount(), query.getMaxAmount()));

        Page<CustomerResponse> page =
                customerRepository.findAll(spec, pageable)
                        .map(CustomerResponse::new);

        return new PageResponse<CustomerResponse>("customers", page);
    }

    // Cập nhật customer
    public CustomerResponse updateCustomer(Integer id, String name, String phoneNum, Gender gender, String note) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer does not exist"));

        CustomerValidation.validateUpdate(name, phoneNum);

        customerRepository.findByPhoneNum(phoneNum)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(c -> {
                    throw new RuntimeException("Phone number already exists");
                });

        CustomerValidation.validateUpdate(name, phoneNum);
        customer.update(name, new PhoneNumber(phoneNum), gender, note);
        Customer updated = customerRepository.save(customer);
        return new CustomerResponse(updated);
    }
}
