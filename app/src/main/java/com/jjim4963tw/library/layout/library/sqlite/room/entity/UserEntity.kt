package com.jjim4963tw.library.layout.library.sqlite.room.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Entity
 * ColumnInfo：將column name對應到變數
 * PrimaryKey：設定主鍵，加上autoGenerate = true時代表會自動產生流水號
 */
@Entity(tableName = "user_info")
data class UserEntity(
    @ColumnInfo(name = "username")
    val name: String?,
    val age: Int?,
    val address: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
)

/**
 * CRUD 存取 DB
 * C：@Insert
 * R：@Query
 * U：@Update
 * D：@Delete
 */
@Dao
interface UserDao {
    @Insert
    suspend fun insertUserInfo(userInfo: UserEntity)

    @Update
    suspend fun updateUserInfo(userInfo: UserEntity)

    @Delete
    suspend fun deleteUserInfo(userInfo: UserEntity)

    @Query("SELECT * FROM user_info WHERE username = :name")
    suspend fun getUserInfo(name: String): UserEntity

    @Query("SELECT * FROM user_info WHERE id = :id")
    suspend fun getUserInfo(id: Int): UserEntity

    @Query("SELECT * FROM user_info")
    fun getUserInfo(): Flow<List<UserEntity>>
}
