package com.jjim4963tw.library.utility;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class StorageUtility {

    // return /data/user/0/package id/cache
    public static String getInternalCacheDir(Context context) {
        return context.getCacheDir().getAbsolutePath();
    }

    public static File getInternalCache(Context context) {
        return context.getCacheDir();
    }

    // return /data/user/0/package id/files
    public static String getInternalFileDir(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    public static File getInternalFile(Context context) {
        return context.getFilesDir();
    }

    // return /storage/emulated/0/Android/data/package id/cache
    public static String getExternalCacheDir(Context context) {
        return context.getExternalCacheDir().getAbsolutePath();
    }

    public static File getExternalCache(Context context) {
        return context.getExternalCacheDir();
    }

    // return /storage/emulated/0/Android/data/package id/files/type
    public static String getExternalFileDir(Context context, String type) {
        return context.getExternalFilesDir(type).getAbsolutePath();
    }

    public static File getExternalFile(Context context, String type) {
        return context.getExternalFilesDir(type);
    }

    /**
     * return database path
     * @param dbName : database name
     */
    public static String getDataBasePath(Context context, String dbName) {
        return context.getDatabasePath(dbName).getAbsolutePath();
    }

    // write data used FileChannel, 大檔案與多線程寫入使用此方法, 在複製只有幾KB的文件時，比BufferReader慢。
    public static boolean writeDataUsedFileChannel(FileOutputStream outputStream, FileInputStream inputStream) {
        FileChannel outChannel = outputStream.getChannel();
        FileChannel inChannel = inputStream.getChannel();

        boolean isSuccess = false;
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            isSuccess = true;
        } catch (Exception e) {
            Log.e("", "檔案傳輸異常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                inChannel.close();
                outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }

    // write data used BufferStream, 複製小檔案使用此方法。
    public static boolean writeDataUsedBufferStream(FileOutputStream outputStream, FileInputStream inputStream) {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        boolean isSuccess = false;
        try {
            byte[] buf = new byte[1024];
            int length;
            while((length = bufferedInputStream.read(buf)) != -1){
                bufferedOutputStream.write(buf, 0, length);
            }
            isSuccess = true;
        } catch (Exception e) {
            Log.e("", "檔案傳輸異常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }
}
