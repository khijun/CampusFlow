package edu.du.campusflow.controller;

import edu.du.campusflow.repository.FileInfoRepository;
import edu.du.campusflow.service.FileUploadService;
import edu.du.campusflow.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.net.URLEncoder;
import java.nio.file.Paths;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import edu.du.campusflow.service.FileLoadService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileInfoRepository fileInfoRepository;
    private final FileUploadService fileUploadService;
    private final FileLoadService fileLoadService;

    // id값으로 파일의 주소를 반환하는 메서드
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id){
        Path path = FileUtils.getSavePath(fileInfoRepository.findById(id).orElse(null));
        return path==null?ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null): ResponseEntity.ok().body(new FileSystemResource(path));
    }

    // 파일을 저장하는 메서드
    @PostMapping
    public ResponseEntity<?> addFile(@RequestParam("file") MultipartFile file) {
        fileUploadService.saveFile(file);
        return ResponseEntity.ok().body("파일 업로드 성공");
    }
}
