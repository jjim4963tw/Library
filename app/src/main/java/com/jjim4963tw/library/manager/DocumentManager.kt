package com.jjim4963tw.library.manager

import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class DocumentManager(val activity: AppCompatActivity) {
    interface DocumentRequestListener {
        fun requestSuccess(uris: List<Uri>)
        fun requestFail()
    }

    private var listener: DocumentRequestListener? = null

    /**
     * 註冊 Activity Result API Callback
     */
    private val openDocLauncher = activity.registerForActivityResult(ActivityResultContracts.OpenDocument()) {
        if (it != null) {
            this.listener?.requestSuccess(arrayListOf(it))
        } else {
            this.listener?.requestFail()
        }
    }

    private val openDocsLauncher = activity.registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) {
        if (it != null && it.isNotEmpty()) {
            this.listener?.requestSuccess(it)
        } else {
            this.listener?.requestFail()
        }
    }

    private val openDocTreeLauncher = activity.registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
        if (it != null) {
            this.listener?.requestSuccess(arrayListOf(it))
        } else {
            this.listener?.requestFail()
        }
    }

    /**
     * 開啟檔案管理器並單選檔案
     */
    fun requestDocument(mimeType: Array<String> = arrayOf("*/*"), listener: DocumentRequestListener) {
        this.listener = listener
        this.openDocLauncher.launch(mimeType)
    }

    /**
     * 開啟檔案管理器並多選檔案
     */
    fun requestDocuments(mimeType: Array<String> = arrayOf("*/*"), listener: DocumentRequestListener) {
        this.listener = listener
        this.openDocsLauncher.launch(mimeType)
    }

    /**
     * 開啟檔案管理器並選擇資料夾路徑
     */
    fun requestDocumentTree(listener: DocumentRequestListener) {
        this.listener = listener
        this.openDocTreeLauncher.launch(null)
    }
}