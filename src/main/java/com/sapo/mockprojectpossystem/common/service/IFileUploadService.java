package com.sapo.mockprojectpossystem.common.service;

import com.sapo.mockprojectpossystem.common.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileUploadService {
    FileUploadResponse uploadImageFile(MultipartFile file) throws IOException;

    void deleteFile(String assetId) throws Exception;
}
