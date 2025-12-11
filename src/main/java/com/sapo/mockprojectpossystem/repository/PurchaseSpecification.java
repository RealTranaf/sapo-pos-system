package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.Customer;
import com.sapo.mockprojectpossystem.model.Purchase;
import com.sapo.mockprojectpossystem.model.PurchaseItem;
import com.sapo.mockprojectpossystem.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PurchaseSpecification {

    public static Specification<Purchase> containKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String pattern = "%" + keyword.toLowerCase() + "%";

            Join<Purchase, Customer> customerJoin = root.join("customer", JoinType.LEFT);
            Join<Purchase, User> userJoin = root.join("user", JoinType.LEFT);

            return cb.or(
                    cb.like(cb.lower(root.get("note")), pattern),
                    cb.like(cb.lower(customerJoin.get("name")), pattern),
                    cb.like(cb.lower(userJoin.get("username")), pattern)
            );
        };
    }
    // Query tìm kiếm đơn hàng từ keyword theo note, tên khách hàng hoặc tên nhân viên

    public static Specification<Purchase> customerEquals(Integer customerId) {
        return (root, query, cb) -> {
            if (customerId == null) return null;
            return cb.equal(root.get("customer").get("id"), customerId);
        };
    }
    // Query tìm kiếm đơn hàng theo id khách hàng

    public static Specification<Purchase> userEquals(Integer userId) {
        return (root, query, cb) -> {
            if (userId == null) return null;
            return cb.equal(root.get("user").get("id"), userId);
        };
    }
    // Query tìm kiếm đơn hàng theo id nhân viên

    public static Specification<Purchase> totalAmountBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null)
                return cb.between(root.get("totalAmount"), min, max);

            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("totalAmount"), min);

            return cb.lessThanOrEqualTo(root.get("totalAmount"), max);
        };
    }
    // Query tìm kiếm đơn hàng có tiền hàng trong khoảng

    public static Specification<Purchase> discountAmountBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null)
                return cb.between(root.get("discountAmount"), min, max);

            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("discountAmount"), min);

            return cb.lessThanOrEqualTo(root.get("discountAmount"), max);
        };
    }
    // Query tìm kiếm đơn hàng có tiền khuyến mãi trong khoảng


    public static Specification<Purchase> createdDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null || end == null) return null;
            return cb.between(root.get("createdAt"), start, end);
        };
    }
    // Query tìm kiếm đơn hàng xảy ra trong khoản thời gian nhất định

    public static Specification<Purchase> containsProduct(Integer productId) {
        return (root, query, cb) -> {
            if (productId == null) return null;

            Join<Purchase, PurchaseItem> items = root.join("purchaseItems", JoinType.LEFT);

            return cb.equal(items.get("product").get("id"), productId);
        };
    }
    // Query tìm kiếm đơn hàng có chứa product nhất định
}
