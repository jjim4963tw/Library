package com.jjim4963tw.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.jjim4963tw.library.utility.PermissionUtility;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtility.requestPermissions(this, PermissionUtility.storagePermission, PermissionUtility.REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtility.REQUEST_CODE) {
            for (String permission : permissions) {
                if (PermissionUtility.shouldAskForPermission(this, permission)) {
                    Snackbar.make(findViewById(R.id.textView), "This is explanation: Please give us permission", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PermissionUtility.goAppSettingPage(MainActivity.this);
                                }
                            }).show();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
