package org.example.utils;

import ch.qos.logback.core.rolling.helper.FileStoreUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class FileUtils {

    private static final String uploadPath = "src/main/resources/multimedia";

    private static String getFileExtension(MultipartFile file) {
        // Get the original file name
        String originalFileName = file.getOriginalFilename();

        // Extract the file extension
        if (originalFileName != null && !originalFileName.isEmpty()) {
            int lastDotIndex = originalFileName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                return originalFileName.substring(lastDotIndex + 1);
            }
        }

        // If the file name does not contain an extension, return null or an empty string
        return null;
    }

    public static  String getFileCode(MultipartFile multipartFile) throws RuntimeException {
        String extension = FileUtils.getFileExtension(multipartFile);
        if (getFileExtension(multipartFile) == null) {
            throw new RuntimeException("Invalid file extension");
        }
        return String.format("%s.%s", UUID.randomUUID(), extension);
    }

    public static byte[] getFile(String fileCode) throws IOException {
        Path filePath = Paths.get(uploadPath, fileCode);
        return Files.readAllBytes(filePath);
    }

    public static void copyFile(String sourceFileCode, String desFileCode) throws IOException{
        Path source = Paths.get(uploadPath, sourceFileCode);
        Path target = Paths.get(uploadPath, desFileCode);
        Files.copy(source, target);
    }

    public static void storeFile(String fileCode, MultipartFile file) throws IOException {
        Path filePath = Paths.get(uploadPath, fileCode);
        Files.write(filePath, file.getBytes());
    }

    public static void storeFile(String oldFileCode, String newFileCode, MultipartFile file) throws IOException {
        Path oldFilePath = Paths.get(uploadPath, oldFileCode);
        Path newFilePath = Paths.get(uploadPath, newFileCode);
        Files.delete(oldFilePath);
        Files.write(newFilePath, file.getBytes());
    }

    public static void deleteFile(String fileCode) throws IOException {
        Path filePath = Paths.get(uploadPath, fileCode);
        Files.delete(filePath);
    }
}
