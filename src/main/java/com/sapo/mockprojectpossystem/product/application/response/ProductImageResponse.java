package com.sapo.mockprojectpossystem.product.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("position")
    private Integer position;

    @JsonProperty("src")
    private String src;

    @JsonProperty("alt")
    private String alt;

    @JsonProperty("filename")
    private String filename;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;
}