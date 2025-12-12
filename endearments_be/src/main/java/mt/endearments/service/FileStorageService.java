package mt.endearments.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public String upload(MultipartFile file) {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = root.resolve(filename);
            Files.copy(file.getInputStream(), filePath);
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + file.getOriginalFilename(), e);
        }
    }
}
