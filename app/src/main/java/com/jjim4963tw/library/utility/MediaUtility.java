package com.jjim4963tw.library.utility;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MediaUtility {

    public static int REQUEST_CAMERA_CODE = 0;
    public static int REQUEST_VIDEO_CODE = 1;
    public static int REQUEST_FILE_CODE = 2;
    public static int REQUEST_FOLDER_CODE = 3;

    public static Uri mediaPathUri;

    public static void requestCamera(Activity activity) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(activity.getPackageManager()) != null) {
            Uri photoUri = getStorageMediaPath(activity, REQUEST_CAMERA_CODE);
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                activity.startActivityForResult(captureIntent, REQUEST_CAMERA_CODE);
            }
        }
    }

    public static void requestVideo(Activity activity) {
        Intent captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(activity.getPackageManager()) != null) {
            Uri photoUri = getStorageMediaPath(activity, REQUEST_VIDEO_CODE);
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                activity.startActivityForResult(captureIntent, REQUEST_VIDEO_CODE);
            }
        }
    }

    // used MediaStore to create media file
    private static Uri getStorageMediaPath(Activity activity, int type) {
        File mediaFile;
        Uri mediaUri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 适配android 10
            mediaPathUri = mediaUri = createMediaUri(activity, type);
        } else {
            mediaFile = createMediaFile(activity);

            if (mediaFile != null) {
                mediaPathUri = Uri.parse(mediaFile.getAbsolutePath());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // FileProvider 創建一個content類型的Uri
                    mediaUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", mediaFile);
                } else {
                    mediaUri = Uri.fromFile(mediaFile);
                }
            }
        }

        return mediaUri;
    }

    // API >= Android Q
    private static Uri createMediaUri(Context context, int type) {
        // 判斷是否有SD卡,優先使用外部儲存空間
        if (type == REQUEST_CAMERA_CODE) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            } else {
                return context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
            }
        } else if (type == REQUEST_VIDEO_CODE) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new ContentValues());
            } else {
                return context.getContentResolver().insert(MediaStore.Video.Media.INTERNAL_CONTENT_URI, new ContentValues());
            }
        }
        return null;
    }

    // API < Android Q
    private static File createMediaFile(Context context) {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File photoFile = StorageUtility.getExternalFile(context, Environment.DIRECTORY_PICTURES);
        if (photoFile != null && !photoFile.exists()) {
            photoFile.mkdir();
        }
        File tempFile = new File(photoFile, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    // Storage Access Framework
    public static void selectStorageFilePath(Activity activity) {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        activity.startActivityForResult(intent, REQUEST_FILE_CODE);
    }

    // Storage Access Framework
    public static void selectStorageFolderPath(Activity activity) {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        activity.startActivityForResult(intent, REQUEST_FOLDER_CODE);
    }
}
