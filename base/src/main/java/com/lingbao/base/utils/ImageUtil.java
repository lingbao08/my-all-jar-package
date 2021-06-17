package com.lingbao.base.utils;

import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static com.lingbao.base.constants.ImageTypeEnum.JPEG;
import static com.lingbao.base.constants.ImageTypeEnum.PNG;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-09-27 10:56
 **/

public class ImageUtil {

    public static String imageToBase64(String imagePath) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(imagePath));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] base64ToImage(String imagePath) {
        byte[] bytes = imagePath.getBytes();
        return Base64.getDecoder().decode(bytes);
    }

    public static boolean isJPGImage(Object o) {
        return StringUtils.equals(JPEG.getType(),getFormatName(o));
    }

    public static boolean isPNGImage(byte[] bytes) {
        return StringUtils.equals(PNG.getType(),getFormatName(bytes));
    }


    private static String getFormatName(byte[] bytes) {
        return getFormatName(new ByteArrayInputStream(bytes));
    }

    private static String getFormatName(Object o) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(o)) {
            return ImageIO.getImageReaders(iis).next().getFormatName();

        } catch (Exception ignored) {
        }
        return null;
    }
}
