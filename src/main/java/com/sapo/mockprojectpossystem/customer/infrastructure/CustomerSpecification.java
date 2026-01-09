package com.sapo.mockprojectpossystem.customer.infrastructure;

import com.sapo.mockprojectpossystem.customer.domain.model.Customer;
import com.sapo.mockprojectpossystem.customer.domain.enums.Gender;
import com.sapo.mockprojectpossystem.purchase.domain.model.Purchase;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

@RequiredArgsConstructor
public class CustomerSpecification {
    public static Specification<Customer> containKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(root.get("name"), pattern),
                    cb.like(root.get("phoneNum"), pattern),
                    cb.like(root.get("note"), pattern)
            );
        };
    }
    // Query tìm kiếm từ keyword theo tên hoặc SDT của customer

//    public static Specification<Customer> purchaseDateBetween(Instant start, Instant end) {
//        return (root, query, cb) -> {
//            if (start == null || end == null) return null;
//            return cb.between(root.get("lastPurchaseAt"), start, end);
//        };
//    }
    public static Specification<Customer> purchaseDateBetween(Instant start, Instant end) {
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

    public static Specification<Customer> purchaseAmountBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null)
                return cb.between(root.get("totalPurchaseAmount"), min, max);
            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("totalPurchaseAmount"), min);
            return cb.lessThanOrEqualTo(root.get("totalPurchaseAmount"), max);
        };
    }
    // Query tìm kiếm customer theo khoảng tổng đơn hàng đã mua
}