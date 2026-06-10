package com.example.composepractice.data.repository

import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.data.room.DatabaseHelper
import javax.inject.Inject

class AccountRepository @Inject constructor(private val dbHelper: DatabaseHelper) {

    fun getAccounts() = dbHelper.getAllAccounts()

    suspend fun getAccountByUserName(userName: String) = dbHelper.getAccountByUserName(userName)

    suspend fun checkLogin(userName: String, password: String): AccountModel? =
        dbHelper.checkLogin(userName, password)

    fun getAccountById(id: Int) = dbHelper.getAccountById(id)

    suspend fun insertAccount(account: AccountModel): Long = dbHelper.insertAccount(account)

    suspend fun updateAccount(id: Int, password: String) = dbHelper.updateAccount(id, password)

    suspend fun deleteAccount(account: AccountModel) = dbHelper.deleteAccount(account)
}