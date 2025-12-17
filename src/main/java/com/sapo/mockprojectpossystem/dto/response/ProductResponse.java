package com.sapo.mockprojectpossystem.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("summary")
    private String summary;

    @JsonProperty("content")
    private String content;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("brand")
    private BrandResponse brand;

    @JsonProperty("images")
    private List<ProductImageResponse> images;

    @JsonProperty("variants")
    private List<ProductVariantResponse> variants;

    @JsonProperty("options")
    private List<ProductOptionResponse> options;

    @JsonProperty("types")
    private List<TypeResponse> types;
}