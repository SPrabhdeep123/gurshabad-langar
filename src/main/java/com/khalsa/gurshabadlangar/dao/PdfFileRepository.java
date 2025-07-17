package com.khalsa.gurshabadlangar.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khalsa.gurshabadlangar.entity.PdfFile;

@Repository
public interface PdfFileRepository extends JpaRepository<PdfFile, Long>{

}