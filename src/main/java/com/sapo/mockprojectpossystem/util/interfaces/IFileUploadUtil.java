package com.sapo.mockprojectpossystem.util.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadUtil {
    void checkIsImage(MultipartFile file);

    void checkSize(MultipartFile file);

    String getExtension(MultipartFile file);

    String generateUniqueFileName(MultipartFile file);
}
