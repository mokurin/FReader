package com.feng.freader.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;

import com.feng.freader.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    /**
     * 将 Uri 转换为 file path
     */
    public static String uri2FilePath(Activity activity, Uri uri) {
        String filePath;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
        if (actualimagecursor == null) {
            filePath = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            filePath = actualimagecursor.getString(actual_image_column_index);
        }
        
        return filePath;
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    public static File uri2FileQ(Context context, Uri uri) {
        File file = null;
        try {
            //android10以上转换
            if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
                file = new File(uri.getPath());
            } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                //把文件复制到沙盒目录
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    try {
                        InputStream is = contentResolver.openInputStream(uri);
                        File cache = new File(context.getExternalCacheDir().getAbsolutePath(), Math.round((Math.random() + 1) * 1000) + displayName);
                        FileOutputStream fos = new FileOutputStream(cache);
                        FileUtils.copy(is, fos);
                        file = cache;
                        fos.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return file;
    }

    /**
     * 获取文件长度，以 M 为单位
     */
    public static double getFileSize(File file){
        long len = file.length();
        return (double) len / Math.pow(2, 20);
    }

    /**
     * 获取文件长度，以 M 为单位
     */
    public static double getFileSize(long len){
        return (double) len / Math.pow(2, 20);
    }

    /**
     * 通过图片的 filePath 加载本地图片
     */
    public static Bitmap loadLocalPicture(String filePath) {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取本地缓存文件的大小
     */
    public static String getLocalCacheSize() {
        File file = new File(Constant.EPUB_SAVE_PATH);
        double len = getFileSize(getTotalSizeOfFiles(file));

        return String.valueOf((int)(len)) + "M";
    }

    // 递归方式 计算文件的大小
    private static long getTotalSizeOfFiles(File file) {
        if (file.isFile())
            return file.length();
        File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (File child : children)
                total += getTotalSizeOfFiles(child);
        return total;
    }

    /**
     * 清除本地缓存
     */
    public static void clearLocalCache() {
        File file = new File(Constant.EPUB_SAVE_PATH);
        deleteFile(file);
    }

    /**
     * 删除文件夹或文件
     */
    public static void deleteFile(File file){
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()){
            return;
        }
        // 删除文件
        if (file.isFile()) {
            file.delete();
            return;
        }
        //取得这个目录下的所有子文件对象
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f: files){
            deleteFile(f);
        }
        //删除空文件夹  for循环已经把上一层节点的目录清空。
        file.delete();
    }
}
