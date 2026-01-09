package com.sapo.mockprojectpossystem.common.service;

import com.sapo.mockprojectpossystem.common.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ICloudinaryService {
    FileUploadResponse upload(MultipartFile file, Map<String, String> opts) throws IOException;

    FileUploadResponse uploadImage(MultipartFile file) throws IOException;

    void delete(String assetId) throws Exception;
}
