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
import com.khalsa.gurshabadlangar.entity.AudioFile;
import com.khalsa.gurshabadlangar.service.AudioFileService;

@Service
public class AudioFileServiceImpl implements AudioFileService {

    private static final Logger logger = LoggerFactory.getLogger(AudioFileServiceImpl.class);

    private final AudioFileRepository audioFileRepository;
    private final String uploadDir;

    public AudioFileServiceImpl(AudioFileRepository audioFileRepository,
            @Value("${audio.upload-dir}") String uploadDir) {
        this.audioFileRepository = audioFileRepository;
        this.uploadDir = uploadDir;
    }

    @Override
    public AudioFile save(MultipartFile file) {
        try {
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
            audioFile.setDescription("Audio uploaded on " + java.time.LocalDateTime.now());

            AudioFile savedEntity = audioFileRepository.save(audioFile);
            logger.info("Audio metadata saved to database for file: {}", cleanedFileName);
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
