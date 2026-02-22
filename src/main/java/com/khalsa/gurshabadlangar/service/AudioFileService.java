package com.khalsa.gurshabadlangar.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.khalsa.gurshabadlangar.entity.AudioFile;

public interface AudioFileService {
    List<AudioFile> getAllAudioFiles();

    AudioFile save(MultipartFile file, Long pdfId, Integer paragraphIndex);
}
