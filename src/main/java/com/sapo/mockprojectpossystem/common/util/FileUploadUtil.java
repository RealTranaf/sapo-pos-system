package com.sapo.mockprojectpossystem.common.util;

import com.sapo.mockprojectpossystem.common.exception.InvalidException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class FileUploadUtil implements IFileUploadUtil {
    @Value("${upload.maxsize.mb}")
    private long maxSizeMB;

    @Override
    public void checkIsImage(MultipartFile file) {
        String IMAGE_REGEX = "(jpg|jpeg|png|gif|webp)$";
        if (!Pattern.matches(IMAGE_REGEX, getExtension(file).toLowerCase()))
            throw new InvalidException("Invalid file type");
    }

    @Override
    public void checkSize(MultipartFile file) {
        if (file.getSize() > maxSizeMB * 1024 * 1024)
            throw new InvalidException("Your attached file must less than " + maxSizeMB + "Mb");
    }

    @Override
    public String getExtension(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }

    @Override
    public String generateUniqueFileName(MultipartFile file) {
        String sortUuid = UUID.randomUUID().toString().substring(0, 8);
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = FilenameUtils.getBaseName(file.getOriginalFilename());
        return fileName + "_" + dateString + "_" + sortUuid;
    }
}
