package com.jjim4963tw.library.sqlite.sqliteopenhelper.helper

import android.database.Cursor
import com.jjim4963tw.library.sqlite.sqliteopenhelper.entity.UserInfo

class UserInfoHelper {

    fun insertUserInfo(userInfo: UserInfo) {
        val adapter = UserInfoAdapter()
        try {
            adapter.openDatabase()
            adapter.insertUserInfo(userInfo)
        } finally {
            adapter.closeDatabase()
        }
    }

    fun getUserInfo(): UserInfo? {
        val adapter = UserInfoAdapter()
        var userInfo: UserInfo? = null
        var cursor: Cursor? = null

        try {
            adapter.openDatabase()
            cursor = adapter.getUserInfo()
            if (cursor != null && cursor.moveToFirst()) {
                userInfo = UserInfo(cursor.getString(cursor.getColumnIndexOrThrow(UserInfoAdapter.COL_USERNAME)), cursor.getInt(cursor.getColumnIndexOrThrow(
                    UserInfoAdapter.COL_AGE
                )), cursor.getString(cursor.getColumnIndexOrThrow(UserInfoAdapter.COL_ADDRESS)), cursor.getInt(cursor.getColumnIndexOrThrow(
                    UserInfoAdapter.COL_ID
                )))
            }
        } finally {
            cursor?.close()
            adapter.closeDatabase()
        }

        return userInfo
    }
}