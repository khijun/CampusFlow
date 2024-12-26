package edu.du.campusflow.repository;

import edu.du.campusflow.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
} 