package com.easypan.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScaleFilter {
    private static final Logger logger = LoggerFactory.getLogger(ScaleFilter.class);
    public static void createCover4Video(File sourceFIle,Integer width,File targetFile){
        try {
            String cmd = "ffmpeg -i %s -y -vframes 1 -vf scale=%d:%d/a %s";
        }catch (Exception e){
            logger.error("生成视频封面失败",e);
        }
    }

    public static Boolean createThumbnailWidthFFmpeg(File file,int thumbnailWidth,File targetFile,Boolean delSource){
        try {
            BufferedImage src = ImageIO.read(file);
            int scoreW = src.getWidth();
            int scoreH = src.getHeight();
            if (scoreW <= thumbnailWidth){
                return false;
            }
            compressImage(file, thumbnailWidth, targetFile, delSource);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void compressImage(File sourceFile,Integer width,File targetFile,Boolean delSource){
        try {
            String cmd = "ffmpeg -i %s -vf scale=%d:-1 %s -y";
            ProcessUtils.executeCommand(String.format(cmd, sourceFile.getAbsolutePath(),width,targetFile.getAbsolutePath()),false);
            if (delSource){
                FileUtils.forceDelete(sourceFile);
            }
        }catch (Exception e){
            logger.error("压缩图片失败");
        }
    }
}
