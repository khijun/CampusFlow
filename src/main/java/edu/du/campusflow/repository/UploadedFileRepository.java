package edu.du.campusflow.repository;

import edu.du.campusflow.entity.UploadedFile;
import edu.du.campusflow.entity.FileId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, FileId> {
} 