package com.khalsa.gurshabadlangar.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.khalsa.gurshabadlangar.entity.AudioFile;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFile, Long> {
}
