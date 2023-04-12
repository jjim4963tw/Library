package com.jjim4963tw.library.layout.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.jjim4963tw.library.R;
import com.jjim4963tw.library.manager.CameraManager;
import com.jjim4963tw.library.manager.DocumentManager;
import com.jjim4963tw.library.manager.PermissionManager;
import com.jjim4963tw.library.utility.MediaUtility;
import com.jjim4963tw.library.utility.StorageUtility;

import java.util.ArrayList;
import java.util.List;

public class StorageAndMediaActivity extends AppCompatActivity {
    private final PermissionManager permissionManager = new PermissionManager(this);
    private final CameraManager cameraManager = new CameraManager(this);
    private final DocumentManager documentManager = new DocumentManager(this);

    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_and_media);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String text = String.format("getExternalCacheDir : %s \r\n getExternalDataDir (Null) : %s \r\n" +
                            "getInternalCacheDir : %s",
                    StorageUtility.getExternalCacheDir(StorageAndMediaActivity.this),
                    StorageUtility.getExternalFileDir(StorageAndMediaActivity.this, null),
                    StorageUtility.getInternalCacheDir(StorageAndMediaActivity.this)
            );

            textView.setText(text);
        } else {
            permissionManager.requestPermission(PermissionManager.Companion.getOldStoragePermission(), new PermissionManager.PermissionRequestListener() {
                @Override
                public void requestSuccess() {
                    String text = String.format("getExternalCacheDir : %s \r\n getExternalDataDir (Null) : %s \r\n" +
                                    "getInternalCacheDir : %s",
                            StorageUtility.getExternalCacheDir(StorageAndMediaActivity.this),
                            StorageUtility.getExternalFileDir(StorageAndMediaActivity.this, null),
                            StorageUtility.getInternalCacheDir(StorageAndMediaActivity.this)
                    );

                    textView.setText(text);
                }

                @Override
                public void requestFail(@NonNull ArrayList<String> deniedPermissions) {
                    Snackbar.make(findViewById(R.id.textView), "This is explanation: Please give us permission", Snackbar.LENGTH_LONG)
                            .setAction("OK", view -> permissionManager.goAppSettingsPage()).show();
                }
            });
        }
    }
    //endregion

    //region Camera / video function
    private void showMediaFunction(final int type) {
        permissionManager.requestPermission(PermissionManager.Companion.getCameraPermission(), new PermissionManager.PermissionRequestListener() {
            @Override
            public void requestSuccess() {
                if (type == 0) {
                    cameraManager.requestCamera(new CameraManager.CameraRequestListener() {
                        @Override
                        public void requestSuccess(@NonNull Uri uri) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                imageView.setImageURI(MediaUtility.mediaPathUri);
                            } else {
                                // 使用图片路径加载
                                imageView.setImageBitmap(BitmapFactory.decodeFile(MediaUtility.mediaPathUri.toString()));
                            }
                        }

                        @Override
                        public void requestFail(int status) {
                            Snackbar.make(findViewById(R.id.textView), "get Image URI Error", Snackbar.LENGTH_LONG)
                                    .setAction("OK", null).show();
                        }
                    });
                } else if (type == 1) {
                    cameraManager.requestVideo(new CameraManager.CameraRequestListener() {
                        @Override
                        public void requestSuccess(@NonNull Uri uri) {
                            Intent intent = new Intent(StorageAndMediaActivity.this, VideoPlayerActivity.class);
                            intent.putExtra("video_uri", MediaUtility.mediaPathUri.toString());
                            startActivity(intent);
                        }

                        @Override
                        public void requestFail(int status) {
                            Snackbar.make(findViewById(R.id.textView), "get Video URI Error", Snackbar.LENGTH_LONG)
                                    .setAction("OK", null).show();
                        }
                    });
                }
            }

            @Override
            public void requestFail(@NonNull ArrayList<String> deniedPermissions) {
                Snackbar.make(findViewById(R.id.textView), "This is explanation: Please give us permission", Snackbar.LENGTH_LONG)
                        .setAction("OK", view -> permissionManager.goAppSettingsPage()).show();
            }
        });
    }
    //endregion

    private void usedSAFSelectItem(int type) {
        if (type == 0) {
            documentManager.requestDocument(new String[]{"*/*"}, new DocumentManager.DocumentRequestListener() {
                @Override
                public void requestSuccess(@NonNull List<? extends Uri> uris) {
                    Uri uri = uris.get(0);
                    // 應用程式「採用」系統提供的永久 URI 權限
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    textView.setText(uri.toString());
                }

                @Override
                public void requestFail() {
                    Snackbar.make(findViewById(R.id.textView), "get File URI Error", Snackbar.LENGTH_LONG)
                            .setAction("OK", null).show();
                }
            });
        } else if (type == 1) {
            documentManager.requestDocumentTree(new DocumentManager.DocumentRequestListener() {
                @Override
                public void requestSuccess(@NonNull List<? extends Uri> uris) {
                    Uri uri = uris.get(0);
                    // 應用程式「採用」系統提供的永久 URI 權限
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    textView.setText(uri.toString());
                }

                @Override
                public void requestFail() {
                    Snackbar.make(findViewById(R.id.textView), "get Folder URI Error", Snackbar.LENGTH_LONG)
                            .setAction("OK", null).show();
                }
            });
        }
    }
}
