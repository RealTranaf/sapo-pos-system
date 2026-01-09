package com.sapo.mockprojectpossystem.customer.application;

import com.sapo.mockprojectpossystem.auth.interfaces.request.UpdateUserRequest;
import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.enums.Gender;
import com.sapo.mockprojectpossystem.customer.domain.model.PhoneNumber;
import com.sapo.mockprojectpossystem.customer.domain.repository.CustomerRepository;
import com.sapo.mockprojectpossystem.customer.infrastructure.CustomerSpecification;
import com.sapo.mockprojectpossystem.customer.interfaces.request.CreateCustomerRequest;
import com.sapo.mockprojectpossystem.customer.interfaces.request.CustomerQueryParams;
import com.sapo.mockprojectpossystem.customer.interfaces.request.UpdateCustomerRequest;
import com.sapo.mockprojectpossystem.customer.interfaces.response.CustomerResponse;
import com.sapo.mockprojectpossystem.common.response.PageResponse;
import com.sapo.mockprojectpossystem.common.util.DateTimeUtils;
import com.sapo.mockprojectpossystem.common.util.StringUtils;
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

import static com.sapo.mockprojectpossystem.customer.validation.CustomerValidation.validateCustomer;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    // Tạo customer từ name, phoneNum, gender và note
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        String name = request.getName();
        String phoneNum = request.getPhoneNum();
        Gender gender = request.getGender();
        String note = request.getNote();

        validateCustomer(name, phoneNum);

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

        String keyword = query.getKeyword();
        String startDate = query.getStartDate();
        String endDate = query.getEndDate();
        Integer page = query.getPage();
        Integer size = query.getSize();
        String sortBy = query.getSortBy();
        String sortDir = query.getSortDir();
        Gender gender = query.getGender();
        Double minAmount = query.getMinAmount();
        Double maxAmount = query.getMaxAmount();

        String sortField = StringUtils.hasText(sortBy) ? sortBy : "createdAt";

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Instant startInstant = null;
        Instant endInstant = null;

        if (StringUtils.hasText(startDate) && StringUtils.hasText(endDate)) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            startInstant = DateTimeUtils.startOfDayUtc(start);
            endInstant = DateTimeUtils.endOfDayUtc(end);
        }

        Specification<Customer> spec = Specification
                .where(CustomerSpecification.containKeyword(keyword))
                .and(CustomerSpecification.purchaseDateBetween(startInstant, endInstant))
                .and(CustomerSpecification.genderEquals(gender == null ? null : gender))
                .and(CustomerSpecification.purchaseAmountBetween(minAmount, maxAmount));

        Page<CustomerResponse> pageResponse = customerRepository.findAll(spec, pageable).map(CustomerResponse::new);

        return new PageResponse<CustomerResponse>("customers", pageResponse);
    }

    // Cập nhật customer
    public CustomerResponse updateCustomer(Integer id, UpdateCustomerRequest request) {

        String name = request.getName();
        String phoneNum = request.getPhoneNum();
        Gender gender = request.getGender();
        String note = request.getNote();

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer does not exist"));

        validateCustomer(name, phoneNum);

        customerRepository.findByPhoneNum(phoneNum)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(c -> {
                    throw new RuntimeException("Phone number already exists");
                });

        customer.update(name, new PhoneNumber(phoneNum), gender, note);
        Customer updated = customerRepository.save(customer);
        return new CustomerResponse(updated);
    }
}
