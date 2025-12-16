package com.sapo.mockprojectpossystem.service.implementations;

import com.cloudinary.Cloudinary;
import com.sapo.mockprojectpossystem.dto.response.FileUploadResponse;
import com.sapo.mockprojectpossystem.service.interfaces.ICloudinaryService;
import com.sapo.mockprojectpossystem.util.interfaces.IFileUploadUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudinaryService implements ICloudinaryService {
    final Cloudinary cloudinary;

    final IFileUploadUtil fileUploadUtil;

    @Value("${cloudinary.image.upload.preset}")
    String productImagePreset;

    @Override
    public FileUploadResponse upload(MultipartFile file, Map<String, String> opts) throws IOException {
        String originalName = file.getOriginalFilename();
        String format = fileUploadUtil.getExtension(file);

        String uniqueName = fileUploadUtil.generateUniqueFileName(file);

        Map<String, String> mainOpts = new HashMap<>();
        mainOpts.put("public_id", uniqueName);
        mainOpts.put("format", format);
        mainOpts.putAll(opts);

        var result = cloudinary.uploader().upload(file.getBytes(), mainOpts);

        String url = result.get("url").toString();
        String publicId = result.get("public_id").toString();
        String assetId = result.get("asset_id").toString();
        int size = Integer.parseInt(result.get("bytes").toString());


        return FileUploadResponse.builder()
            .assetId(assetId)
            .size(size)
            .originalName(originalName)
            .url(url)
            .publicId(publicId)
            .type(format)
            .build();
    }

    @Override
    public FileUploadResponse uploadImage(MultipartFile file) throws IOException {
        Map<String, String> mainOpts = new HashMap<>();
        mainOpts.put("upload_preset", productImagePreset);
        return upload(file, mainOpts);
    }

    @Override
    public void delete(String assetId) throws Exception {
        cloudinary.api().deleteResourcesByAssetIds(List.of(assetId), Map.of());
    }
}
