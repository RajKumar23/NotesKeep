package com.example.composepractice.data.room

import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.data.model.NotesModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseHelperImpl @Inject constructor(private val appDatabase: AppDatabase) :
    DatabaseHelper {

    //account related functions
    override fun getAllAccounts(): Flow<List<AccountModel>> =
        appDatabase.accountDAO().getAllAccounts()

    override suspend fun insertAccount(account: AccountModel) =
        appDatabase.accountDAO().insertAccount(account)

    override suspend fun deleteAccount(account: AccountModel) =
        appDatabase.accountDAO().deleteAccount(account)

    //notes related functions
    override fun getAllNotes(): Flow<List<NotesModel>> = appDatabase.notesDAO().getAllNotes()

    override suspend fun getNoteById(id: Int): NotesModel = appDatabase.notesDAO().getNoteById(id)

    override suspend fun insertNote(notes: NotesModel) = appDatabase.notesDAO().insertNote(notes)

    override suspend fun deleteNote(notesId: Int) = appDatabase.notesDAO().deleteNote(notesId)

    override suspend fun updateNote(note: NotesModel) = appDatabase.notesDAO().updateNote(note)
}