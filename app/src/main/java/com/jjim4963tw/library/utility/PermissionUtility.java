package com.jjim4963tw.library.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class PermissionUtility {

    public static final int REQUEST_CODE = 999;

    public static final String[] storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final String[] cameraPermission = new String[]{Manifest.permission.CAMERA};

    public PermissionUtility() {

    }

    private boolean needRequestPermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public boolean hasPermission(Activity activity, String permission) {
        if (needRequestPermissions()) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public void requestPermissions(Activity activity, String[] permission, int requestCode) {
        if (needRequestPermissions()) {
            activity.requestPermissions(permission, requestCode);
        }
    }

    public void requestPermissions(Fragment fragment, String[] permission, int requestCode) {
        if (needRequestPermissions()) {
            fragment.requestPermissions(permission, requestCode);
        }
    }

    public void requestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestPermissionComplete();
        } else {
            requestPermissionFailed();
        }
    }

    protected void requestPermissionComplete() {

    }

    protected void requestPermissionFailed() {

    }


    public static void goAppSettingPage(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.getPackageName(), null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
