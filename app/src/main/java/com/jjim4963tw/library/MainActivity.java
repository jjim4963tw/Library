package com.jjim4963tw.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jjim4963tw.library.utility.PermissionUtility;
import com.jjim4963tw.library.utility.StorageUtility;

public class MainActivity extends AppCompatActivity {
    private PermissionUtility permissionUtility;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        showExternalStorageIndex();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtility.REQUEST_CODE) {
            permissionUtility.requestResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showExternalStorageIndex() {
        permissionUtility = new PermissionUtility() {
            @Override
            protected void requestPermissionComplete() {
                super.requestPermissionComplete();
                String text = String.format("getExternalCacheDir : %s \r\n getExternalDataDir (Null) : %s \r\n" +
                                "getInternalCacheDir : %s",
                        StorageUtility.getExternalCacheDir(MainActivity.this),
                        StorageUtility.getExternalDataDir(MainActivity.this, null),
                        StorageUtility.getInternalCacheDir(MainActivity.this)
                );

                textView.setText(text);
            }

            @Override
            protected void requestPermissionFailed() {
                super.requestPermissionFailed();
                Snackbar.make(findViewById(R.id.textView), "This is explanation: Please give us permission", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionUtility.goAppSettingPage(MainActivity.this);
                            }
                        }).show();
            }
        };
        permissionUtility.requestPermissions(this, PermissionUtility.storagePermission, PermissionUtility.REQUEST_CODE);
    }
}
