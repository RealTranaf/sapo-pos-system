package com.sapo.mockprojectpossystem.product.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageRequest {
    @NotNull(message = "Position is required.")
    private Integer position;

    @NotBlank(message = "URL is required.")
    private String src;

    private String alt;

    @NotBlank(message = "File name is required.")
    private String filename;

    private Integer size;

    private Integer width;

    private Integer height;
}