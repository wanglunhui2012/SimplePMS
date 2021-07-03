package indi.simple.pms.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import indi.simple.pms.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:36
 * @Description:
 */
public class FileUtil extends cn.hutool.core.io.FileUtil {
    private static final int GB = 1073741824;
    private static final int MB = 1048576;
    private static final int KB = 1024;
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public FileUtil() {
    }

    public static File toFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String prefix = "." + getExtensionName(fileName);
        File file = null;

        try {
            file = File.createTempFile(IdUtil.simpleUUID(), prefix);
            multipartFile.transferTo(file);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return file;
    }

    public static void deleteFile(File... files) {
        File[] var1 = files;
        int var2 = files.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            File file = var1[var3];
            if (file.exists()) {
                file.delete();
            }
        }

    }

    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }

        return filename;
    }

    public static String getFileNameNoEx(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf(46);
            if (dot > -1 && dot < filename.length()) {
                return filename.substring(0, dot);
            }
        }

        return filename;
    }

    public static String getSize(int size) {
        String resultSize = "";
        if (size / 1073741824 >= 1) {
            resultSize = DF.format((double)((float)size / 1.07374182E9F)) + "GB   ";
        } else if (size / 1048576 >= 1) {
            resultSize = DF.format((double)((float)size / 1048576.0F)) + "MB   ";
        } else if (size / 1024 >= 1) {
            resultSize = DF.format((double)((float)size / 1024.0F)) + "KB   ";
        } else {
            resultSize = size + "B   ";
        }

        return resultSize;
    }

    public static File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        if (file.exists()) {
            return file;
        } else {
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[8192];

            int bytesRead;
            while((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            ins.close();
            return file;
        }
    }

    public static File upload(MultipartFile file, String filePath) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssS");
        String name = getFileNameNoEx(file.getOriginalFilename());
        String suffix = getExtensionName(file.getOriginalFilename());
        String nowStr = "-" + format.format(date);

        try {
            String fileName = name + nowStr + "." + suffix;
            String path = filePath + fileName;
            File dest = (new File(path)).getCanonicalFile();
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            file.transferTo(dest);
            return dest;
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public static String fileToBase64(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        String base64 = Base64.encode(buffer);
        return base64.replaceAll("[\\s*\t\n\r]", "");
    }

    public static void downloadExcel(List<Map<String, Object>> list, HttpServletResponse response) throws IOException {
        String tempPath = System.getProperty("java.io.tmpdir") + IdUtil.fastSimpleUUID() + ".xlsx";
        File file = new File(tempPath);
        BigExcelWriter writer = ExcelUtil.getBigWriter(file);
        writer.write(list, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=file.xlsx");
        ServletOutputStream out = response.getOutputStream();
        file.deleteOnExit();
        writer.flush(out, true);
        IoUtil.close(out);
    }

    public static String getFileType(String type) {
        String documents = "txt doc pdf ppt pps xlsx xls docx";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.contains(type)) {
            return "图片";
        } else if (documents.contains(type)) {
            return "文档";
        } else if (music.contains(type)) {
            return "音乐";
        } else {
            return video.contains(type) ? "视频" : "其他";
        }
    }

    public static String getFileTypeByMimeType(String type) {
        String mimeType = (new MimetypesFileTypeMap()).getContentType("." + type);
        return mimeType.split("/")[0];
    }

    public static void checkSize(long maxSize, long size) {
        if (size > maxSize * 1024L * 1024L) {
            throw new BadRequestException("文件超出规定大小");
        }
    }

    public static boolean check(File file1, File file2) {
        String img1Md5 = getMd5(file1);
        String img2Md5 = getMd5(file2);
        return img1Md5.equals(img2Md5);
    }

    public static boolean check(String file1Md5, String file2Md5) {
        return file1Md5.equals(file2Md5);
    }

    public static byte[] getByte(File file) {
        byte[] b = new byte[(int)file.length()];

        try {
            FileInputStream in = new FileInputStream(file);

            try {
                in.read(b);
            } catch (IOException var4) {
                var4.printStackTrace();
            }

            return b;
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static String getMd5(byte[] bytes) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(bytes);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            byte[] var7 = md;
            int var8 = md.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                byte byte0 = var7[var9];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var11) {
            var11.printStackTrace();
            return null;
        }
    }

    public static String getMd5(File file) {
        return getMd5(getByte(file));
    }

    public static String upload(String path, MultipartFile file) {
        String newFileName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        File newFile = new File(path, newFileName);
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }

        try {
            file.transferTo(newFile);
        } catch (IOException var5) {
            throw new BadRequestException("服务器文件写入失败!");
        }

        return path + newFileName;
    }
}

