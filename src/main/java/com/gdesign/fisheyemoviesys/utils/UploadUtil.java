package com.gdesign.fisheyemoviesys.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

/**
 * @author ycy
 */
@SuppressWarnings("all")
public class UploadUtil {
    public static String uploadImg(MultipartFile file) {
        Calendar calendar = Calendar.getInstance();
        String prePath = "/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1);
        File trueFile = new File("E:/upload" + prePath);
        if (!trueFile.exists()) {
            if (!trueFile.mkdirs()) {
                return null;
            }
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StrUtil.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        String filename = uuid + "." + extension;
        try {
            file.transferTo(new File("E:/upload" + prePath + "/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prePath + "/" + filename;
    }

    public static String uploadPoster(MultipartFile file) {
        Calendar calendar = Calendar.getInstance();
        String prePath = "/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1);
        File trueFile = new File("E:/poster" + prePath);
        if (!trueFile.exists()) {
            if (!trueFile.mkdirs()) {
                return null;
            }
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StrUtil.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        String filename = uuid + "." + extension;
        try {
            file.transferTo(new File("E:/poster" + prePath + "/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prePath + "/" + filename;
    }
}
