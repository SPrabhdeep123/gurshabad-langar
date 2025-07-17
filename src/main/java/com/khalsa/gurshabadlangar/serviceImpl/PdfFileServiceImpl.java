package com.khalsa.gurshabadlangar.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khalsa.gurshabadlangar.dao.PdfFileRepository;
import com.khalsa.gurshabadlangar.dto.PdfFileDTO;
import com.khalsa.gurshabadlangar.entity.PdfFile;
import com.khalsa.gurshabadlangar.service.PdfFileService;
import org.springframework.util.StringUtils;

@Service
public class PdfFileServiceImpl implements PdfFileService{


    @Autowired
    private PdfFileRepository pdfFileRepository;

// Set your desired upload directory
    private final String UPLOAD_DIR = "uploads";

     @Override
    public PdfFile save(MultipartFile file) {
        try {
            // Create uploads directory if it does not exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Clean file name
        String cleanedFileName = StringUtils.cleanPath(file.getOriginalFilename());

            // Build full file path
            Path filePath = Paths.get(UPLOAD_DIR, cleanedFileName);

            // Save file to the disk
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Save metadata to the database
            PdfFile pdfFile = new PdfFile();
            pdfFile.setFileName(cleanedFileName);
            pdfFile.setFilePath(filePath.toString());
            pdfFile.setTitle("Sample Title"); // You can make this dynamic later
            pdfFile.setDescription("Sample Description");

            return pdfFileRepository.save(pdfFile);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
public List<PdfFile> getAllPdfs() {
    return pdfFileRepository.findAll();
}
}