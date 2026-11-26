package vn.iotstar.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStorageUtil {

    private final Path uploadRoot;

    public FileStorageUtil(@Value("${app.upload.dir:${user.home}/bt5-uploads}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadRoot);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục lưu ảnh: " + this.uploadRoot, e);
        }
    }

    public String storeImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalName = file.getOriginalFilename();
        String ext = "png";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
        }
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + "." + ext;
        Path target = this.uploadRoot.resolve(fileName).normalize();
        if (!target.startsWith(this.uploadRoot)) {
            throw new IOException("Đường dẫn tệp không hợp lệ!");
        }
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public void deleteLocalFile(String fileName) {
        if (fileName == null || fileName.isBlank() || fileName.startsWith("http://") || fileName.startsWith("https://")) {
            return;
        }
        try {
            Path target = this.uploadRoot.resolve(Paths.get(fileName).getFileName().toString()).normalize();
            if (target.startsWith(this.uploadRoot)) {
                Files.deleteIfExists(target);
            }
        } catch (IOException ignored) {
            // Ignore cleanup failure
        }
    }

    public Path resolveFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }
        String safeName = Paths.get(fileName).getFileName().toString();
        Path resolved = this.uploadRoot.resolve(safeName).normalize();
        return resolved.startsWith(this.uploadRoot) ? resolved : null;
    }
}
