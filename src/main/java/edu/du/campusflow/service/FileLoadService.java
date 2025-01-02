package edu.du.campusflow.service;

import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.repository.FileInfoRepository;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FileLoadService {

    private final FileInfoRepository fileInfoRepository;

    // 파일 정보 리스트를 받아 파일들의 저장 경로를 반환하는 메서드
    public List<Path> getSavePaths(List<FileInfo> fileInfoList){
        List<Path> paths = new ArrayList<>();
        for(FileInfo fileInfo : fileInfoList){
            paths.add(fileInfo.getSavePath());
        }
        return paths;
    }

    public List<Path> getAllSavePaths() {
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        return getSavePaths(fileInfoList);
    }

//    public List<Path> getAllImageSavePaths() {
//        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
//    }

//    public MultipartFile getFile(Long fileId) {
//        FileInfo fileInfo = fileInfoRepository.findById(fileId).orElseThrow();
//        Path path = Paths.get(fileInfo.getFilePath(), fileInfo.getFileUuid() + "." + fileInfo.getFileType());
//    }
}
