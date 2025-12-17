package com.sapo.mockprojectpossystem.service.interfaces;

import com.sapo.mockprojectpossystem.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileUploadService {
    FileUploadResponse uploadImageFile(MultipartFile file) throws IOException;

    void deleteFile(String assetId) throws Exception;
}
