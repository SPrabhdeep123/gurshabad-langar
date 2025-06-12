package com.khalsa.gurshabadlangar.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.khalsa.gurshabadlangar.dao.PdfFileRepository;
import com.khalsa.gurshabadlangar.dto.PdfFileDTO;
import com.khalsa.gurshabadlangar.entity.PdfFile;
import com.khalsa.gurshabadlangar.service.PdfFileService;

@Service
public class PdfFileServiceImpl implements PdfFileService{

    private static final String UPLOAD_DIR = "src/main/resources/static/pdfs/";


    @Autowired
    private PdfFileRepository pdfFileRepository;

    private final String uploadDir = "uploads/";


    @Override
    public String savePdf(MultipartFile file) {
        try {
            String filePath = UPLOAD_DIR + file.getOriginalFilename();
            File destFile = new File(filePath);
            file.transferTo(destFile);

            PdfFile pdfFile = new PdfFile();
            pdfFileRepository.save(pdfFile);

            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    @Override
    public Optional<PdfFile> getPdfByName(String fileName) {
        return pdfFileRepository.findByFileName(fileName);
    }

    @Override
    public List<PdfFile> getAllPdfs() {
        return pdfFileRepository.findAll();
    }

    @Override
    public PdfFileDTO createPDF(MultipartFile file, PdfFileDTO pdfFileDTO) {
        try {
            // Ensure the upload directory exists
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Save the uploaded file
            String filePath = uploadDir + file.getOriginalFilename();
            File destFile = new File(filePath);
            file.transferTo(destFile);

            // Save file details to the database
            PdfFile pdfFile = new PdfFile();
            pdfFile.setFileName(file.getOriginalFilename());
            pdfFile.setFilePath(filePath);
            pdfFile.setTitle(pdfFileDTO.getTitle());           // Optional fields
            pdfFile.setDescription(pdfFileDTO.getDescription());

            PdfFile savedPdfFile = pdfFileRepository.save(pdfFile);

            // Build and return DTO
            PdfFileDTO responseDTO = new PdfFileDTO();
            responseDTO.setId(savedPdfFile.getId());
            responseDTO.setFileName(savedPdfFile.getFileName());
            responseDTO.setFilePath(savedPdfFile.getFilePath());
            responseDTO.setTitle(savedPdfFile.getTitle());
            responseDTO.setDescription(savedPdfFile.getDescription());

            return responseDTO;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }


}