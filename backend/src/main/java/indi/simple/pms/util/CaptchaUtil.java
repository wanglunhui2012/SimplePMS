package indi.simple.pms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:34
 * @Description:
 */
@Component
public class CaptchaUtil {
    @Value("${captcha.size}")
    private int captchaSize;
    @Value("${captcha.ttl}")
    private int captchaTTL;
    @Value("${captcha.width}")
    private int captchaWidth;
    @Value("${captcha.height}")
    private int captchaHeight;
    private static final String CAPTCHA_CODES = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ"; // 0和O容易混淆，直接去掉
    private static Random random = new Random();

    public String getCaptcha(){
        return getCaptcha(captchaSize);
    }

    public String getCaptcha(int size) {
        return getCaptcha(size, CAPTCHA_CODES);
    }

    public String getCaptcha(int size, String sources) {
        if (sources == null || sources.length() == 0) {
            sources = CAPTCHA_CODES;
        }

        int sourcesLength = sources.length();
        StringBuilder captcha = new StringBuilder(size);

        for(int i = 0; i < size; ++i) {
            captcha.append(sources.charAt((new Random()).nextInt(sourcesLength - 1)));
        }

        return captcha.toString();
    }

    public void generateCaptcha(OutputStream os,String captcha) throws IOException{
        generateCaptcha(captchaWidth,captchaHeight,os,captcha);
    }

    public void generateCaptcha(int w, int h, OutputStream os, String captcha) throws IOException {
        int captchaSize = captcha.length();
        BufferedImage image = new BufferedImage(w, h, 1);
        Random rand = new Random();
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color[] colors = new Color[5];
        Color[] colorSpaces = new Color[]{Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW};
        float[] fractions = new float[colors.length];

        for(int i = 0; i < colors.length; ++i) {
            colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
            fractions[i] = rand.nextFloat();
        }

        Arrays.sort(fractions);
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, w, h);
        Color c = getRandColor(200, 250);
        g2.setColor(c);
        g2.fillRect(0, 2, w, h - 4);
        Random random = new Random();
        g2.setColor(getRandColor(160, 200));

        int area;
        int fontSize;
        int x;
        int y;
        for(int i = 0; i < 20; ++i) {
            area = random.nextInt(w - 1);
            fontSize = random.nextInt(h - 1);
            x = random.nextInt(6) + 1;
            y = random.nextInt(12) + 1;
            g2.drawLine(area, fontSize, area + x + 40, fontSize + y + 20);
        }

        float yawpRate = 0.05F;
        area = (int)(yawpRate * (float)w * (float)h);

        int i;
        for(fontSize = 0; fontSize < area; ++fontSize) {
            x = random.nextInt(w);
            y = random.nextInt(h);
            i = getRandomIntColor();
            image.setRGB(x, y, i);
        }

        shear(g2, w, h, c);
        g2.setColor(getRandColor(100, 160));
        fontSize = h - 4;
        Font font = new Font("Algerian", 2, fontSize);
        g2.setFont(font);
        char[] chars = captcha.toCharArray();

        for(i = 0; i < captchaSize; ++i) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(0.7853981633974483D * rand.nextDouble() * (double)(rand.nextBoolean() ? 1 : -1), (double)(w / captchaSize * i + fontSize / 2), (double)(h / 2));
            g2.setTransform(affine);
            g2.drawChars(chars, i, 1, (w - 10) / captchaSize * i + 5, h / 2 + fontSize / 2 - 10);
        }

        g2.dispose();
        ImageIO.write(image, "jpg", os);
    }

    private Color getRandColor(int fc, int bc) {
        if (fc > 255) {
            fc = 255;
        }

        if (bc > 255) {
            bc = 255;
        }

        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        int[] var2 = rgb;
        int var3 = rgb.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int c = var2[var4];
            color <<= 8;
            color |= c;
        }

        return color;
    }

    private int[] getRandomRgb() {
        int[] rgb = new int[3];

        for(int i = 0; i < 3; ++i) {
            rgb[i] = random.nextInt(255);
        }

        return rgb;
    }

    private void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }

    private void shearX(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for(int i = 0; i < h1; ++i) {
            double d = (double)(period >> 1) * Math.sin((double)i / (double)period + 6.283185307179586D * (double)phase / (double)frames);
            g.copyArea(0, i, w1, 1, (int)d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int)d, i, 0, i);
                g.drawLine((int)d + w1, i, w1, i);
            }
        }

    }

    private void shearY(Graphics g, int w1, int h1, Color color) {
        int period = random.nextInt(40) + 10;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;

        for(int i = 0; i < w1; ++i) {
            double d = (double)(period >> 1) * Math.sin((double)i / (double)period + 6.283185307179586D * (double)phase / (double)frames);
            g.copyArea(i, 0, 1, h1, 0, (int)d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int)d, i, 0);
                g.drawLine(i, (int)d + h1, i, h1);
            }
        }

    }

    private Font getRandomFont(int size) {
        Random random = new Random();
        Font[] font = new Font[]{new Font("Ravie", 0, size), new Font("Antique Olive Compact", 0, size), new Font("Fixedsys", 0, size), new Font("Wide Latin", 0, size), new Font("Gill Sans Ultra Bold", 0, size)};
        return font[random.nextInt(5)];
    }
}

