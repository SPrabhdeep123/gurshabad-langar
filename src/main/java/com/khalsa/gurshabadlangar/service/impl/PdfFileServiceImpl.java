package com.khalsa.gurshabadlangar.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.khalsa.gurshabadlangar.dao.PdfFileRepository;
import com.khalsa.gurshabadlangar.entity.PdfFile;
import com.khalsa.gurshabadlangar.service.PdfFileService;

@Service
public class PdfFileServiceImpl implements PdfFileService {

    private static final Logger logger = LoggerFactory.getLogger(PdfFileServiceImpl.class);

    private final PdfFileRepository pdfFileRepository;
    private final String uploadDir;

    public PdfFileServiceImpl(PdfFileRepository pdfFileRepository,
            @Value("${file.upload-dir}") String uploadDir) {
        this.pdfFileRepository = pdfFileRepository;
        this.uploadDir = uploadDir;
    }

    @Override
    public PdfFile save(MultipartFile file) {
        try {
            // Create uploads directory if it does not exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    logger.info("Created upload directory: {}", uploadDir);
                }
            }

            // Clean file name
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IllegalArgumentException("File name cannot be null");
            }
            String cleanedFileName = StringUtils.cleanPath(originalFileName);

            // Build full file path
            Path filePath = Paths.get(uploadDir, cleanedFileName);

            // Save file to the disk
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("File saved to disk: {}", filePath);

            // Save metadata to the database
            PdfFile pdfFile = new PdfFile();
            pdfFile.setFileName(cleanedFileName);
            pdfFile.setFilePath(filePath.toString());
            pdfFile.setTitle(cleanedFileName); // Default title to filename
            pdfFile.setDescription("Uploaded on " + java.time.LocalDateTime.now());

            PdfFile savedEntity = pdfFileRepository.save(pdfFile);
            logger.info("MetaData saved to database for file: {}", cleanedFileName);
            return savedEntity;

        } catch (IOException e) {
            logger.error("Failed to upload file", e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PdfFile> getAllPdfs() {
        return pdfFileRepository.findAll();
    }
}
