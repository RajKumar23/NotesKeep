package com.example.composepractice.RoomDB.DAO

import android.provider.ContactsContract
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.composepractice.RoomDB.Model.NotesModel
import kotlinx.coroutines.flow.Flow


@Dao
interface NotesDAO {
    @Query("SELECT * FROM notes ORDER BY id ASC")
    fun getAllNotes(): Flow<List<NotesModel>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NotesModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NotesModel)

    @Delete
    suspend fun deleteNote(note: NotesModel)

    @Update
    suspend fun updateNote(note: NotesModel)
}