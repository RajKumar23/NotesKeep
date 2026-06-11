package com.example.composepractice.data.room

import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.data.model.NotesModel
import kotlinx.coroutines.flow.Flow


interface DatabaseHelper {
    //account related functions
    fun getAllAccounts(): Flow<List<AccountModel>>

    suspend fun getAccountByUserName(userName: String): AccountModel?

    suspend fun checkLogin(userName: String, password: String): AccountModel?

    fun getAccountById(id: Int): Flow<AccountModel?>

    fun getFavoriteNotesByUser(createdBy: Int): Flow<List<NotesModel>>

    suspend fun insertAccount(account: AccountModel): Long

    suspend fun updateAccount(id: Int, password: String)

    suspend fun deleteAccount(id: Int): Int

    //notes related functions
    fun getAllNotes(createdBy: Int): Flow<List<NotesModel>>

    suspend fun getNoteById(id: Int): NotesModel

    suspend fun insertNote(notes: NotesModel): Long

    suspend fun updateNote(note: NotesModel)

    suspend fun toggleFavorite(id: Int)

    suspend fun deleteNote(notesId: Int): Int

    suspend fun deleteNotesByUser(createdBy: Int): Int
}