package com.backedrum.component.util;

import lombok.experimental.UtilityClass;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@UtilityClass
public class ImageUtils {
    public static final int RESIZE_TO_WIDTH = 640;
    public static final int RESIZE_TO_HEIGHT = 480;

    public static byte[] resizeToByteArray(BufferedImage originalImage, String format) throws IOException {
        int imageType = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

        BufferedImage resizedImage = new BufferedImage(RESIZE_TO_WIDTH, RESIZE_TO_HEIGHT, imageType);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, RESIZE_TO_WIDTH, RESIZE_TO_HEIGHT, null);
        g.dispose();

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, format, o);

        return o.toByteArray();
    }

    public static String getFormat(String filename) {
        if (filename.endsWith(".png")) {
            return "png";
        } else {
            return "jpg";
        }
    }

}
