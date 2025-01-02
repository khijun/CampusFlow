package edu.du.campusflow.controller;

import edu.du.campusflow.repository.FileInfoRepository;
import edu.du.campusflow.service.FileUploadService;
import edu.du.campusflow.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileInfoRepository fileInfoRepository;
    private final FileUploadService fileUploadService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Long id){
        Resource resource = new FileSystemResource(FileUtils.getSavePath(fileInfoRepository.findById(id).orElse(null)));
        return ResponseEntity.ok().body(resource);
    }

    @PostMapping
    public String addFile(@RequestParam("file") MultipartFile file) {
        fileUploadService.saveFile(file);
        return "redirect:/index";
    }
}
