package com.smart.helper;

import com.smart.Exception.FileValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    private final static String uploadDir = "C:/Users/HP/resources";

    public static String uploadImage(MultipartFile file) {
        if(file == null){
            throw new FileValidationException("File cannot be null");
        }
        try {
            validateFile(file);
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filename = UUID.randomUUID() + "_" +file.getOriginalFilename();
            Path path  = Paths.get(uploadDir,filename);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        }catch(Exception e){
            logger.error("File upload failed for file: {}", file.getOriginalFilename(), e);
            throw new FileValidationException("Image upload failed", e);
        }
    }

    public static void deleteImage(String fileName) {
        try{
            if (fileName == null) {
                logger.error("File deletion failed Due to file null!");
                throw new FileValidationException("File name cannot be null");
            }
            Path path = Paths.get(uploadDir,fileName);
            if (!"default.png".equals(fileName)) {
                Files.deleteIfExists(path);
            }
        }catch (Exception e){
            logger.error("File deletion failed for file: {}", fileName, e);
            throw new FileValidationException("Unable to upload file. Please try again later.", e);
        }
    }

    private static void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileValidationException("File is empty");
        }
        List<String> allowedTypes = List.of("image/jpeg", "image/png");
        if (!allowedTypes.contains(file.getContentType())) {
            throw new FileValidationException("Only JPG and PNG allowed");
        }
    }
}
