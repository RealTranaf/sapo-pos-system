package com.sapo.mockprojectpossystem.common.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadResponse implements Serializable {
    String originalName;
    String assetId;
    Integer size;
    String publicId;
    String url;
    String type;
}
