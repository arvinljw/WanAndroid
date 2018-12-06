package net.arvin.baselib.utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by arvinljw on 2018/11/2 15:20
 * Function：
 * Desc：
 */
public class FileUtil {

    public static boolean deleteFile(File file) {
        return delete(file);
    }

    public static boolean delete(File file) {
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteSingleFile(file);
            } else {
                return deleteDirectory(file);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteSingleFile(File file) {
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    private static boolean deleteDirectory(File dirFile) {
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                flag = deleteSingleFile(file);
                if (!flag) {
                    break;
                }
            } else if (file.isDirectory()) {
                flag = deleteDirectory(file);
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        return dirFile.delete();
    }

    public static long getFileSizes(File f) throws Exception {
        long size = 0;
        File[] fileList = f.listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                size = size + getFileSizes(file);
            } else {
                size = size + getFileSize(file);
            }
        }
        return size;
    }

    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            size = file.length();
        } else {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }
        return size;
    }

    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
