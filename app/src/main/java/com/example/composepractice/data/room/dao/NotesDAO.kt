package com.example.composepractice.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.composepractice.data.model.NotesModel
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDAO {

    @Query("SELECT * FROM notes WHERE createdBy = :createdBy ORDER BY id ASC")
    fun getAllNotes(createdBy: Int): Flow<List<NotesModel>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NotesModel

    @Query("SELECT * FROM notes WHERE isFavorite = 1 AND createdBy = :createdBy ORDER BY id ASC")
    fun getFavoriteNotesByUser(createdBy: Int): Flow<List<NotesModel>>

    @Query("SELECT * FROM notes WHERE (title LIKE :searchQuery OR content LIKE :searchQuery) AND createdBy = :createdBy")
    fun searchNotesByUser(searchQuery: String, createdBy: Int): Flow<List<NotesModel>>

    @Query("SELECT * FROM notes WHERE title LIKE :searchQuery OR content LIKE :searchQuery")
    fun searchNotes(searchQuery: String): Flow<List<NotesModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotesModel): Long

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Int): Int

    @Update
    suspend fun updateNote(note: NotesModel)

    @Query("UPDATE notes SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Int)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    @Query("DELETE FROM notes WHERE createdBy = :createdBy")
    suspend fun deleteNotesByUser(createdBy: Int): Int
}