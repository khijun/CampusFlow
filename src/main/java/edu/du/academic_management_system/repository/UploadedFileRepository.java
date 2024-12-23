package edu.du.academic_management_system.repository;

import edu.du.academic_management_system.entity.UploadedFile;
import edu.du.academic_management_system.entity.FileId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, FileId> {
} 