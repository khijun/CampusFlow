package edu.du.campusflow.controller;

import edu.du.campusflow.service.FileLoadService;
import edu.du.campusflow.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final FileLoadService fileLoadService;
    private final FileUploadService fileUploadService;

    @GetMapping
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/files")
    public String fileUpload(@RequestParam("file") MultipartFile file, Model model) {
        fileUploadService.saveFile(file);
        model.addAttribute("files", fileLoadService.getAllImageSavePaths());
        return "redirect:/test";
    }

}
