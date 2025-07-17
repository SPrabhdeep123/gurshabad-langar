package com.khalsa.gurshabadlangar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.khalsa.gurshabadlangar.dto.PdfFileDTO;
import com.khalsa.gurshabadlangar.entity.PdfFile;

public interface PdfFileService{

    List<PdfFile> getAllPdfs();
    PdfFile save(MultipartFile file);

}