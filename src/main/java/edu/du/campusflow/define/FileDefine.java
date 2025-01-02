package edu.du.campusflow.define;

import java.util.List;

public class FileDefine {
    public static String UPLOAD_PATH = "c:/upload";

    public static String IMAGE_PATH = UPLOAD_PATH + "/images";
    public static String PDF_PATH = UPLOAD_PATH + "/pdfs";
    public static String OTHER_PATH = UPLOAD_PATH + "/others";

    public static List<String> IMAGE_TYPES = List.of("jpg", "png", "jpeg", "gif", "webp");
}
