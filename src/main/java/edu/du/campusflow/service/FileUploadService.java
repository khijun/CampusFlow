package edu.du.campusflow.service;

import edu.du.campusflow.define.FileDefine;
import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.exception.EmptyFileException;
import edu.du.campusflow.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileInfoRepository fileInfoRepository;
    private final AuthService authService;

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
    public void saveFile(MultipartFile[] files) {
        for (MultipartFile file : files) {

        }
    }
    @Transactional
    public void saveFile(MultipartFile file){
        if (file.isEmpty()) throw new EmptyFileException("저장할 파일이 비어있습니다");
        try {
            String uuid = String.valueOf(System.nanoTime()); // 파일이 저장될 이름 생성
            String realName = file.getOriginalFilename(); // 파일의 실제 명을 db에 저장하기 위해 만든 변수
            String type = getFileExtension(file.getOriginalFilename());
            String path = getPathByExtension(type);
            createDirectoryIfNotExists(path);
            double fileSize = file.getSize(); // 파일의 크기
            LocalDateTime createAt = LocalDateTime.now(); // 파일의 저장 날짜

            FileInfo fileInfo = FileInfo.builder()
                    .member(authService.getCurrentMember())
                    .fileUuid(uuid)
                    .fileName(realName)
                    .fileType(type)
                    .filePath(path)
                    .fileSize(fileSize)
                    .createdAt(createAt)
                    .build();

            Path savePath = Paths.get(path, fileInfo.getSaveName()); // 파일의 저장 위치
            try{
                file.transferTo(savePath);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장에 실패함.");
            }
            fileInfoRepository.save(fileInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 입력한 경로에 폴더가 없을 시 폴더를 만드는 메서드
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
} 