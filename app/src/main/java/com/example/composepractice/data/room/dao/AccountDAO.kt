package com.example.composepractice.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.composepractice.data.model.AccountModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDAO {
    @Query("SELECT * FROM account ORDER BY id ASC")
    fun getAllAccounts(): Flow<List<AccountModel>>

    @Query("SELECT * FROM account WHERE id = :id")
    fun getAccountById(id: Int): Flow<AccountModel?>

    @Query("SELECT * FROM account WHERE userName = :userName")
    suspend fun getAccountByUserName(userName: String): AccountModel?

    @Query("SELECT * FROM account WHERE userName = :userName AND password = :password")
    suspend fun checkLogin(userName: String, password: String): AccountModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountModel): Long

    @Query("UPDATE account SET password = :password WHERE id = :id")
    suspend fun updateAccount(id: Int, password: String)

    @Query("DELETE FROM account WHERE id = :id")
    suspend fun deleteAccount(id: Int): Int

    @Query("DELETE FROM account")
    suspend fun deleteAllAccounts()
}