package com.example.composepractice.data.room

import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.data.model.NotesModel
import kotlinx.coroutines.flow.Flow


interface DatabaseHelper {
    //account related functions
    fun getAllAccounts(): Flow<List<AccountModel>>

    suspend fun getAccountByUserName(userName: String): AccountModel?

    suspend fun insertAccount(account: AccountModel): Long

    suspend fun updateAccount(account: AccountModel)

    suspend fun deleteAccount(account: AccountModel)

    //notes related functions
    fun getAllNotes(): Flow<List<NotesModel>>

    suspend fun getNoteById(id: Int): NotesModel

    suspend fun insertNote(notes: NotesModel)

    suspend fun deleteNote(notesId: Int)

    suspend fun updateNote(note: NotesModel)
}