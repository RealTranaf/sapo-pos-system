package com.sapo.mockprojectpossystem.repository;

import com.sapo.mockprojectpossystem.model.Product;
import com.sapo.mockprojectpossystem.model.ProductStatus;
import com.sapo.mockprojectpossystem.model.Type;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {
    public static Specification<Product> containKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("sku")), pattern),
                    cb.like(cb.lower(root.get("barcode")), pattern)
            );
        };
    }
    // Query tìm kiếm từ keyword theo name, sku hoặc barcode của product

    public static Specification<Product> brandEquals(Integer brandId) {
        return (root, query, cb) -> {
            if (brandId == null) return null;
            return cb.equal(root.get("brand").get("id"), brandId);
        };
    }
    // Query tìm kiếm theo brandId

    public static Specification<Product> hasTypes(List<Integer> typeIds) {
        return (root, query, cb) -> {
            if (typeIds == null || typeIds.isEmpty()) return null;

            query.distinct(true);
            Join<Product, Type> typeJoin = root.join("types", JoinType.LEFT);

            return typeJoin.get("id").in(typeIds);
        };
    }
    // Query tìm kiếm theo danh sách typeId

    public static Specification<Product> statusEquals(ProductStatus status) {
        return (root, query, cb) -> {
            if (status == null) return null;
            return cb.equal(root.get("status"), status);
        };
    }
    // Query tìm kiếm theo status của product

    public static Specification<Product> basePriceBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null)
                return cb.between(root.get("basePrice"), min, max);

            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("basePrice"), min);

            return cb.lessThanOrEqualTo(root.get("basePrice"), max);
        };
    }
    // Query tìm product có basePrice trong khoảng min và max

    public static Specification<Product> sellPriceBetween(Double min, Double max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null)
                return cb.between(root.get("sellPrice"), min, max);

            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("sellPrice"), min);

            return cb.lessThanOrEqualTo(root.get("sellPrice"), max);
        };
    }
    // Query tìm product có sellPrice trong khoảng min và max

    public static Specification<Product> quantityBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;

            if (min != null && max != null)
                return cb.between(root.get("quantity"), min, max);

            if (min != null)
                return cb.greaterThanOrEqualTo(root.get("quantity"), min);

            return cb.lessThanOrEqualTo(root.get("quantity"), max);
        };
    }
    // Query tìm product có quantity trong khoảng cần tìm

    public static Specification<Product> inStock(Boolean inStock) {
        return (root, query, cb) -> {
            if (inStock == null) return null;

            if (inStock)
                return cb.greaterThan(root.get("quantity"), 0);

            return cb.equal(root.get("quantity"), 0);
        };
    }
    // Query tìm product có hay đã hết hàng
}
