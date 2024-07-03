package com.thingslink.common.core.utils.file;

import cn.hutool.core.date.DateUtil;
import com.thingslink.common.core.exception.UtilException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.core.utils.uuid.Seq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 文件上传工具类
 *
 * @author wzkris
 */
@Slf4j
public class FileUtil extends cn.hutool.core.io.FileUtil {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};

    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf"};

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 返回上传成功的文件相对路径
     */
    public static String upload(String baseDir, MultipartFile file) {
        try {
            // 以日期作为子目录
            String fileName = DateUtil.format(DateUtil.date(), "/yyyy/MM/dd/") + extractFilename(file);

            writeBytes(file.getBytes(), baseDir + fileName);
            return fileName;
        }
        catch (IOException e) {
            log.error("捕获异常，异常信息：{}", e.getMessage(), e);
            throw new UtilException(e.getMessage());
        }
    }

    /**
     * 分片文件上传
     *
     * @param baseDir  相对应用的基目录
     * @param file     上传的文件
     * @param md5      切片文件摘要
     * @param fileName 指定的文件名
     * @return 返回上传的的绝对路径
     */
    public static String uploadChunk(String baseDir, MultipartFile file, String md5, String fileName) {
        try {
            // 以日期作为子目录，以文件的md5值作为存放文件的文件夹名
            String absPath = baseDir + DateUtil.format(DateUtil.date(), "/yyyy/MM/dd/") + md5 + File.separator + fileName;

            writeBytes(file.getBytes(), absPath);
            return absPath;
        }
        catch (Exception e) {
            log.error("捕获异常，异常信息：{}", e.getMessage(), e);
            throw new UtilException(e.getMessage());
        }
    }

    /**
     * 编码文件名
     */
    public static String extractFilename(MultipartFile file) {
        return StringUtil.format("{}_{}.{}", getBaseName(file), Seq.getId(Seq.uploadSeqType), getExtension(file));
    }

    /**
     * 获取扩展名
     */
    public static String getExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = null;

        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                fileExtension = originalFilename.substring(dotIndex + 1);
            }
        }
        return fileExtension;
    }

    /**
     * 获取不带扩展名的文件名称
     */
    private static String getBaseName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (StringUtils.hasText(originalFilename)) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                return originalFilename.substring(0, dotIndex);
            }
        }
        return originalFilename;
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension        上传文件类型
     * @param allowedExtension 允许上传文件类型
     * @return true/false
     */
    public static boolean isAllowedExtension(String extension, String[] allowedExtension) {
        return Arrays.stream(allowedExtension).anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }
}
