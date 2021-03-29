package com.jjim4963tw.library.layout.library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jjim4963tw.library.layout.library.room.entity.UserEntity
import com.jjim4963tw.library.layout.library.room.helper.UserHelper

class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        UserHelper(this).addUserInfo(UserEntity("test", 18, null))

    }
}
