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
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        PermissionUtility.requestPermissions(this, PermissionUtility.storagePermission, PermissionUtility.REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtility.REQUEST_CODE) {
            for (String permission : permissions) {
                if (!PermissionUtility.hasPermission(this, permission)) {
                    Snackbar.make(findViewById(R.id.textView), "This is explanation: Please give us permission", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    PermissionUtility.goAppSettingPage(MainActivity.this);
                                }
                            }).show();
                } else {
                    showExternalStorageIndex();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void showExternalStorageIndex() {
        String text = String.format("getExternalCacheDir : %s \r\n getExternalDataDir (Null) : %s",
                StorageUtility.getExternalCacheDir(this),
                StorageUtility.getExternalDataDir(this, null)
                );

        textView.setText(text);

    }
}
