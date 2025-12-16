//package com.sapo.mockprojectpossystem.response;
//
//import com.sapo.mockprojectpossystem.model.ProductStatus;
//import com.sapo.mockprojectpossystem.model.Type;
//import lombok.Data;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//public class ProductResponse {
//    private Integer id;
//    private String name;
//    private String sku;
//    private String barcode;
//    private ProductStatus status;
//    private String description;
//    private double basePrice;
//    private double sellPrice;
//    private int quantity;
//    private Integer brandId;
//    private List<Integer> typeIds;
//
//    public ProductResponse(Product product) {
//        this.id = product.getId();
//        this.name = product.getName();
//        this.sku = product.getSku();
//        this.barcode = product.getBarcode();
//        this.status = product.getStatus();
//        this.description = product.getDescription();
//        this.basePrice = product.getBasePrice();
//        this.sellPrice = product.getSellPrice();
//        this.quantity = product.getQuantity();
//        this.brandId = product.getBrand().getId();
//        this.typeIds = product.getTypes()
//                .stream()
//                .map(Type::getId)
//                .collect(Collectors.toList());
//    }
//}
