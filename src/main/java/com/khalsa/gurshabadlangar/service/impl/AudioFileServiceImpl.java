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

import com.khalsa.gurshabadlangar.dao.AudioFileRepository;
import com.khalsa.gurshabadlangar.dao.PdfFileRepository;
import com.khalsa.gurshabadlangar.entity.AudioFile;
import com.khalsa.gurshabadlangar.entity.PdfFile;
import com.khalsa.gurshabadlangar.service.AudioFileService;

@Service
public class AudioFileServiceImpl implements AudioFileService {

    private static final Logger logger = LoggerFactory.getLogger(AudioFileServiceImpl.class);

    private final AudioFileRepository audioFileRepository;
    private final PdfFileRepository pdfFileRepository;
    private final String uploadDir;

    public AudioFileServiceImpl(AudioFileRepository audioFileRepository,
            PdfFileRepository pdfFileRepository,
            @Value("${audio.upload-dir}") String uploadDir) {
        this.audioFileRepository = audioFileRepository;
        this.pdfFileRepository = pdfFileRepository;
        this.uploadDir = uploadDir;
    }

    @Override
    public AudioFile save(MultipartFile file, Long pdfId, Integer paragraphIndex) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Audio file is empty or null");
        }

        try {
            // Fetch the PdfFile to establish mapping
            PdfFile pdfFile = pdfFileRepository.findById(pdfId)
                    .orElseThrow(() -> new RuntimeException("PdfFile not found with id: " + pdfId));

            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (created) {
                    logger.info("Created audio upload directory: {}", uploadDir);
                }
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new IllegalArgumentException("File name cannot be null");
            }
            String cleanedFileName = StringUtils.cleanPath(originalFileName);
            Path filePath = Paths.get(uploadDir, cleanedFileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Audio file saved to disk: {}", filePath);

            AudioFile audioFile = new AudioFile();
            audioFile.setFileName(cleanedFileName);
            audioFile.setFilePath(filePath.toString());
            audioFile.setTitle(cleanedFileName);
            audioFile.setDescription(
                    "Audio uploaded on " + java.time.LocalDateTime.now() + " mapped to PDF ID: " + pdfId);
            audioFile.setPdfFile(pdfFile);
            audioFile.setParagraphIndex(paragraphIndex);

            AudioFile savedEntity = audioFileRepository.save(audioFile);
            logger.info("Audio metadata saved for file: {} mapped to PDF ID: {}, Paragraph: {}",
                    cleanedFileName, pdfId, paragraphIndex);
            return savedEntity;

        } catch (IOException e) {
            logger.error("Failed to upload audio file", e);
            throw new RuntimeException("Failed to upload audio file: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AudioFile> getAllAudioFiles() {
        return audioFileRepository.findAll();
    }
}
