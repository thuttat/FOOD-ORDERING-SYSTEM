package duckie.example.backend.controller;

import duckie.example.backend.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileUploadService.uploadFile(file);
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }

    @DeleteMapping("/upload")
    public ResponseEntity<Void> deleteFile(@RequestParam String imageUrl) {
        fileUploadService.deleteFileByUrl(imageUrl);
        return ResponseEntity.ok().build();
    }
}
