package com.sapo.mockprojectpossystem.service.implementations;

import com.sapo.mockprojectpossystem.dto.response.FileUploadResponse;
import com.sapo.mockprojectpossystem.service.interfaces.ICloudinaryService;
import com.sapo.mockprojectpossystem.service.interfaces.IFileUploadService;
import com.sapo.mockprojectpossystem.util.interfaces.IFileUploadUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadService implements IFileUploadService {
    ICloudinaryService cloudinaryService;
    IFileUploadUtil fileUploadUtil;

    @Override
    public FileUploadResponse uploadImageFile(MultipartFile file) throws IOException {
        fileUploadUtil.checkSize(file);
        fileUploadUtil.checkIsImage(file);
        return cloudinaryService.uploadImage(file);
    }

    @Override
    public void deleteFile(String assetId) throws Exception {
        cloudinaryService.delete(assetId);
    }
}
