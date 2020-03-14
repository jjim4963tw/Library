package com.jjim4963tw.library.layout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.jjim4963tw.library.R;
import com.jjim4963tw.library.utility.MediaUtility;
import com.jjim4963tw.library.utility.PermissionUtility;
import com.jjim4963tw.library.utility.StorageUtility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private PermissionUtility permissionUtility;

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        findViewById(R.id.btn_storage).setOnClickListener(v -> showStorageIndex());
        findViewById(R.id.btn_camera).setOnClickListener(v -> showMediaFunction(0));
        findViewById(R.id.btn_video).setOnClickListener(v -> showMediaFunction(1));
        findViewById(R.id.btn_saf_file).setOnClickListener(v -> usedSAFSelectItem(0));
        findViewById(R.id.btn_saf_folder).setOnClickListener(v -> usedSAFSelectItem(1));
    }

    //region Storage function
    private void showStorageIndex() {
        permissionUtility = new PermissionUtility() {
            @Override
            protected void requestPermissionComplete() {
                super.requestPermissionComplete();
                String text = String.format("getExternalCacheDir : %s \r\n getExternalDataDir (Null) : %s \r\n" +
                                "getInternalCacheDir : %s",
                        StorageUtility.getExternalCacheDir(MainActivity.this),
                        StorageUtility.getExternalFileDir(MainActivity.this, null),
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtility.REQUEST_CODE) {
            permissionUtility.requestResult(requestCode, permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //endregion

    //region Camera / video function
    private void showMediaFunction(final int type) {
        permissionUtility = new PermissionUtility() {
            @Override
            protected void requestPermissionComplete() {
                super.requestPermissionComplete();

                if (type == 0) {
                    MediaUtility.requestCamera(MainActivity.this);
                } else if (type == 1){
                    MediaUtility.requestVideo(MainActivity.this);
                }
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
        permissionUtility.requestPermissions(this, PermissionUtility.cameraPermission, PermissionUtility.REQUEST_CODE);
    }
    //endregion

    private void usedSAFSelectItem(int type) {
        if (type == 0) {
            MediaUtility.selectStorageFilePath(this);
        } else if (type == 1) {
            MediaUtility.selectStorageFolderPath(this);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == MediaUtility.REQUEST_CAMERA_CODE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageView.setImageURI(MediaUtility.mediaPathUri);
                } else {
                    // 使用图片路径加载
                    imageView.setImageBitmap(BitmapFactory.decodeFile(MediaUtility.mediaPathUri.toString()));
                }

            } else if (requestCode == MediaUtility.REQUEST_VIDEO_CODE) {
                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra("video_uri", MediaUtility.mediaPathUri.toString());
                startActivity(intent);

            } else if (requestCode == MediaUtility.REQUEST_FILE_CODE || requestCode == MediaUtility.REQUEST_FOLDER_CODE) {
                Uri uri = data.getData();

                if (uri != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getContentResolver().takePersistableUriPermission(uri, data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && requestCode == MediaUtility.REQUEST_FOLDER_CODE) {
                            uri = DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
                        }
                        Log.e("uri", uri.toString());
                    }

                    textView.setText(uri.toString());
                }
            }
        } else {
            Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
        }
    }

}
