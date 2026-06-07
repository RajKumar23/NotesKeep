package com.example.composepractice.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.composepractice.data.model.NotesModel
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDAO {
    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllNotes(): Flow<List<NotesModel>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NotesModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotesModel)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Update
    suspend fun updateNote(note: NotesModel)
}