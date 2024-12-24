package edu.du.campusflow.service;

import edu.du.campusflow.define.FileDefine;
import edu.du.campusflow.entity.UploadedFile;
import edu.du.campusflow.repository.UploadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadedFileService {
    
    private final UploadedFileRepository uploadedFileRepository;

    // 파일 확장자에 따른 파일 경로를 얻는 메서드
    public String getPathByExtension(String extension) {
        if (extension == null) {
            return FileDefine.OTHER_PATH;
        }

        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return FileDefine.IMAGE_PATH;

            case "pdf":
                return FileDefine.PDF_PATH;

            default:
                return FileDefine.OTHER_PATH;
        }
    }

    // 파일 확장자 추출 메서드
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ""; // 확장자가 없는 경우
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Transactional
    public void saveFile(MultipartFile[] files){
        for(MultipartFile file : files){
            if (file.isEmpty()) continue;
            try{
                String name = file.getOriginalFilename();
                String path = getPathByExtension(file.getOriginalFilename());
                String type = getFileExtension(file.getOriginalFilename());
                String uuid = String.valueOf(System.nanoTime());
                double fileSize = file.getSize();
                LocalDateTime createAt = LocalDateTime.now();
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Transactional
    public void saveFile(MultipartFile file){
        saveFile(new MultipartFile[]{file});
    }

    private static void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directory created: " + path);
            } else {
                System.out.println("Failed to create directory: " + path);
            }
        } else {
            System.out.println("Directory already exists: " + path);
        }
    }

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
                    .id((long)i)
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