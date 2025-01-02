package edu.du.campusflow.service;

import edu.du.campusflow.define.FileDefine;
import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.repository.FileInfoRepository;
import edu.du.campusflow.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileLoadService {

    private final FileInfoRepository fileInfoRepository;

    // 파일 정보 리스트를 받아 파일들의 저장 경로를 반환하는 메서드
    public List<Path> getSavePaths(List<FileInfo> fileInfoList){
        List<Path> paths = new ArrayList<>();
        for(FileInfo fileInfo : fileInfoList){
            paths.add(FileUtils.getSavePath(fileInfo));
        }
        return paths;
    }

//    public List<Path> getAllSavePaths() {
//        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
//        return getSavePaths(fileInfoList);
//    }

    public List<Path> getAllImageSavePaths() {
        List<FileInfo> fileInfoList = fileInfoRepository.findByFileTypeIn(FileDefine.IMAGE_TYPES);
        return getSavePaths(fileInfoList);
    }

    public List<Long> getAllImagesId(){
        List<Long> imageIds = new ArrayList<>();
        fileInfoRepository.findAll().stream().forEach(fi->{
            imageIds.add(fi.getId());
        });
        return imageIds;
    }

}
