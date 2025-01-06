package edu.du.campusflow.utils;

import edu.du.campusflow.entity.FileInfo;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    // 파일 이름 추출 메서드
    public static String getFileName(String filename){
        if(filename == null || !filename.contains(".")) return "";
        int index = filename.lastIndexOf(".");
        return filename.substring(0, index);
    }


    // 파일 확장자 추출 메서드
    public static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ""; // 확장자가 없는 경우
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    // 파일 저장 이름을 가져오는 메서드
    public static String getSaveName(String fileUuid, String fileType){
        return fileUuid + "." + fileType;
    }

    public static String getSaveName(FileInfo fileInfo){
        return getSaveName(fileInfo.getFileUuid(), fileInfo.getFileType());
    }

    // 파일
    public static Path getSavePath(FileInfo fileInfo){
        return fileInfo==null?null:Paths.get(fileInfo.getFilePath(), getSaveName(fileInfo));
    }
}
