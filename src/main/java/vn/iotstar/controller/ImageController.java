package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.iotstar.util.FileStorageUtil;

@Controller
public class ImageController {

    private final FileStorageUtil fileStorageUtil;

    public ImageController(FileStorageUtil fileStorageUtil) {
        this.fileStorageUtil = fileStorageUtil;
    }

    @GetMapping("/image")
    public ResponseEntity<Resource> serveImage(@RequestParam(name = "fname", required = false) String fileName)
            throws IOException {
        Path filePath = fileStorageUtil.resolveFile(fileName);
        if (filePath == null || !Files.exists(filePath) || Files.isDirectory(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
