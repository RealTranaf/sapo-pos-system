package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Gender;
import com.sapo.mockprojectpossystem.model.Purchase;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CustomerSpecification {
    public static Specification<Customer> containKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(root.get("phoneNum"), pattern)
            );
        };
    }
    // Query tìm kiếm từ keyword theo tên hoặc SDT của customer

    public static Specification<Customer> purchaseDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null || end == null) {
                return null;
            }

            query.distinct(true);

            Join<Customer, Purchase> purchases = root.join("purchases", JoinType.LEFT);
            return cb.between(purchases.get("createdAt"), start, end);
        };
    }
    // Query tìm kiếm customer đã mua hàng trong một khoảng thời gian nhất định

    public static Specification<Customer> genderEquals(Gender gender) {
        return (root, query, cb) -> {
            if (gender == null) {
                return null;
            }
            return cb.equal(root.get("gender"), gender);
        };
    }
    // Query tìm kiếm customer dựa theo giới tính
}
