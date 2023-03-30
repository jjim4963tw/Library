package com.jjim4963tw.library.layout.library.sqlite.sqliteopenhelper.helper

import android.database.Cursor
import androidx.core.content.contentValuesOf
import com.jjim4963tw.library.layout.library.sqlite.sqliteopenhelper.entity.UserInfo

class UserInfoAdapter: BaseDBAdapter() {
    companion object {
        const val TABLE_USER_INFO = "user_info"

        const val COL_ID = "id"
        const val COL_USERNAME = "username"
        const val COL_AGE = "age"
        const val COL_ADDRESS = "address"
    }

    fun getUserInfo(): Cursor? {
        return db?.query(TABLE_USER_INFO, arrayOf(COL_ID, COL_USERNAME, COL_AGE, COL_ADDRESS), null, null, null, null, null)
    }

    fun insertUserInfo(userInfo: UserInfo) {
        val initialValues = contentValuesOf(
            COL_USERNAME to userInfo.name,
            COL_ADDRESS to userInfo.address,
            COL_AGE to userInfo.age
        )

        db?.insert(TABLE_USER_INFO, null, initialValues)
    }
}