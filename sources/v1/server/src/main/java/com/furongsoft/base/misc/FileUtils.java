package com.furongsoft.base.misc;

import java.io.*;
import java.util.Base64;

/**
 * 文件工具类
 *
 * @author chenfuqian
 */
public class FileUtils {

    public static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            Tracker.error(e);
        } catch (IOException e) {
            Tracker.error(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Tracker.error(e);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    Tracker.error(e);
                }
            }
        }
        return buffer;
    }

    public static String file2Base64(String filePath) {
        return Base64.getEncoder().encodeToString(file2byte(filePath));
    }

    /**
     * 图片base64 生成文件
     *
     * @param base64   图片base64
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return 是否成功
     */
    public static Boolean base64ToFile(String base64, String filePath, String fileName) {
        if ((StringUtils.isNullOrEmpty(base64)) || (StringUtils.isNullOrEmpty(filePath))) {
            return false;
        }
        File file = null;
        // 创建文件目录

        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file = new File(filePath + "\\" + fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return (file != null) && (file.exists());
    }
}
