package edu.du.campusflow.service;

import edu.du.campusflow.entity.UploadedFile;
import edu.du.campusflow.repository.UploadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadedFileService {
    
    private final UploadedFileRepository uploadedFileRepository;

    @Transactional
    public void createDummyFiles() {
        List<UploadedFile> dummyFiles = new ArrayList<>();
        
        // 각 사용자별로 프로필 이미지 생성
        for (int i = 1; i <= 30; i++) {
            String prefix;
            
            if (i <= 10) {
                prefix = "student" + i;
            } else if (i <= 20) {
                prefix = "professor" + (i-10);
            } else {
                prefix = "staff" + (i-20);
            }
            
            // 프로필 이미지
            dummyFiles.add(UploadedFile.builder()
                    .fileId(new FileId((long)i, 1L))
                    .fileUuid(UUID.randomUUID().toString())
                    .fileName(prefix + "_profile.jpg")
                    .fileType("image/jpeg")
                    .filePath("/uploads/profiles/" + prefix + "_profile.jpg")
                    .fileSize((int)(Math.random() * 900 + 100) + "KB")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());
        }

        uploadedFileRepository.saveAll(dummyFiles);
    }
} 