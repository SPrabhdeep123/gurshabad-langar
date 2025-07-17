package com.khalsa.gurshabadlangar.controller;

/*
import javax.validation.Valid;
*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
        import com.khalsa.gurshabadlangar.service.PdfFileService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import com.khalsa.gurshabadlangar.entity.PdfFile;
import org.springframework.util.StringUtils;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/pdfs")
public class PdfController {

    @Autowired
    private PdfFileService pdfFileService;
    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);


    /*@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PdfFileDTO> uploadPdfFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") PdfFileDTO pdfFileDTO) {

        logger.debug("Uploading file: {}, with data: {}", file.getOriginalFilename(), pdfFileDTO);
        PdfFileDTO savedPdf = pdfFileService.createPDF(file, pdfFileDTO);
        return ResponseEntity.ok(savedPdf);
    }
*/

   @PostMapping("/upload")
public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
        return ResponseEntity.badRequest().body("File is empty");
    }

    try {
        pdfFileService.save(file); // Delegate to service
        return ResponseEntity.ok("File uploaded successfully");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Could not store file " + file.getOriginalFilename());
    }
}
    @GetMapping("/test")
    public String test() {
        return "Controller is working!";
    }
   
}