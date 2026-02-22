package com.khalsa.gurshabadlangar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.khalsa.gurshabadlangar.service.AudioFileService;

@RestController
@RequestMapping("/audio")
public class AudioController {

    private static final Logger logger = LoggerFactory.getLogger(AudioController.class);

    private final AudioFileService audioFileService;

    public AudioController(AudioFileService audioFileService) {
        this.audioFileService = audioFileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file,
            @RequestParam("pdfId") Long pdfId,
            @RequestParam("paragraphIndex") Integer paragraphIndex) {
        try {
            audioFileService.save(file, pdfId, paragraphIndex);
            return ResponseEntity.ok("Audio file uploaded and mapped successfully");
        } catch (Exception e) {
            logger.error("Error uploading audio file: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Audio Controller is working!");
    }
}
