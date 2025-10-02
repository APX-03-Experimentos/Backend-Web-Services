package apx.inc.design_web_services_backend.shared.domain.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String resourceType = "raw"; // default

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                resourceType = "image";
            } else if (contentType.startsWith("video/")) {
                resourceType = "video";
            } else {
                resourceType = "raw"; // Word, Excel, PPT, PDF, etc.
            }
        }

        // Mantener extensión original
        String originalFilename = file.getOriginalFilename();
        String uniqueName = UUID.randomUUID().toString();

        if (originalFilename != null && originalFilename.contains(".")) {
            uniqueName += originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",  // <-- detecta pdf, docx, xlsx, etc
                        "public_id", uniqueName,
                        "use_filename", true,
                        "unique_filename", false,
                        "overwrite", true,
                        "access_mode", "public"
                )
        );

        return uploadResult.get("secure_url").toString();
    }

    public void deleteFile(String imageUrl) {
        try {
            // Extraer public_id desde la URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (Exception e) {
            // Log the error but don't throw to avoid breaking the main operation
            System.err.println("Error deleting file from Cloudinary: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String url) {
        // Lógica para extraer public_id de la URL de Cloudinary
        if (url == null) return null;
        String[] parts = url.split("/");
        return parts[parts.length - 1].split("\\.")[0];
    }

}
