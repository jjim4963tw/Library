package com.jjim4963tw.library.layout.library.room.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jjim4963tw.library.layout.library.room.MyDataBase
import com.jjim4963tw.library.layout.library.room.entity.UserEntity
import kotlinx.coroutines.launch

class UserHelper(context: Context): ViewModel() {
    private val database = MyDataBase.getInstance(context)
    private val userDao = database.userDao()

    val liveData = database.userDao().getUserInfo().asLiveData()

    fun addUserInfo(userEntity: UserEntity) {
        viewModelScope.launch {
            userDao.insertUserInfo(userEntity)
        }
    }

    fun updateUserInfo(userEntity: UserEntity) {
        viewModelScope.launch {
            userDao.updateUserInfo(userEntity)
        }
    }

    fun deleteUserInfo(userEntity: UserEntity) {
        viewModelScope.launch {
            userDao.deleteUserInfo(userEntity)
        }
    }
}