package edu.du.campusflow.define;

import java.util.List;

public class FileDefine {
    public static String UPLOAD_PATH;

    static {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            UPLOAD_PATH = "c:/upload"; // Windows 경로
        } else {
            UPLOAD_PATH = "/upload"; // Linux 또는 MacOS 경로
        }
    }

    public static String IMAGE_PATH = UPLOAD_PATH + "/images";
    public static String PDF_PATH = UPLOAD_PATH + "/pdfs";
    public static String OTHER_PATH = UPLOAD_PATH + "/others";

    public static List<String> IMAGE_TYPES = List.of("jpg", "png", "jpeg", "gif", "webp");
}
