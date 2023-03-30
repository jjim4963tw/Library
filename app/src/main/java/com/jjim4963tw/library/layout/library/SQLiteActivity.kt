package com.jjim4963tw.library.layout.library

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jjim4963tw.library.R
import com.jjim4963tw.library.layout.library.sqlite.room.entity.UserEntity
import com.jjim4963tw.library.layout.library.sqlite.room.helper.UserHelper
import com.jjim4963tw.library.layout.library.sqlite.sqliteopenhelper.entity.UserInfo
import com.jjim4963tw.library.layout.library.sqlite.sqliteopenhelper.helper.UserInfoHelper
import kotlinx.coroutines.launch

class SQLiteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_sqlite)
    }

    fun doSQLiteOpenHelperPage(view: View) {
        UserInfoHelper().insertUserInfo(UserInfo("test1", 18, "123"))
    }

    fun doRoomPage(view: View) {
        UserHelper(this@SQLiteActivity).addUserInfo(UserEntity(name = "test2", age = 20, address = "999"))
        UserHelper(this@SQLiteActivity).liveData.observe(this@SQLiteActivity, Observer {
            Log.e("User0", it[0].toString())
            Log.e("User1", it[1].toString())
        })
    }
}
