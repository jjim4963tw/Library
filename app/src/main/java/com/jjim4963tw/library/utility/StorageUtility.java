package com.jjim4963tw.library.utility;

import android.content.Context;

import java.io.File;

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
}
