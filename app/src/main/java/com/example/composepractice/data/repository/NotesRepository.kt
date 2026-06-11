package com.example.composepractice.data.repository

import com.example.composepractice.data.model.NotesModel
import com.example.composepractice.data.room.DatabaseHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepository @Inject constructor(private val dbHelper: DatabaseHelper) {
    fun getAllNotes(createdBy: Int): Flow<List<NotesModel>> = dbHelper.getAllNotes(createdBy)

    fun getFavoriteNotesByUser(createdBy: Int): Flow<List<NotesModel>> =
        dbHelper.getFavoriteNotesByUser(createdBy)

    suspend fun getNoteById(id: Int): NotesModel = dbHelper.getNoteById(id)

    suspend fun insertNote(notes: NotesModel): Long = dbHelper.insertNote(notes)

    suspend fun updateNote(note: NotesModel) = dbHelper.updateNote(note)

    suspend fun toggleFavorite(id: Int) = dbHelper.toggleFavorite(id)

    suspend fun deleteNote(notesId: Int): Int = dbHelper.deleteNote(notesId)
}