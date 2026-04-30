package duckie.example.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {
    private final Cloudinary cloudinary;

    public FileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            return uploadResult.get("secure_url").toString();
        } catch (IOException ex) {
            throw new RuntimeException("Unable to upload files to Cloudinary", ex);
        }
    }

    public void deleteFileByUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) return;

        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex != -1) {
                String publicId = fileName.substring(0, dotIndex);
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                System.out.println(">> Cloudinary junk files have been successfully cleaned up: " + publicId);
            }
        } catch (Exception e) {
            System.err.println(">> Error cleaning up Cloudinary: " + e.getMessage());
        }
    }
}
