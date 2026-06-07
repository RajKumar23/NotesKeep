package com.example.composepractice.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.composepractice.data.room.dao.AccountDAO
import com.example.composepractice.data.room.dao.NotesDAO
import com.example.composepractice.data.model.AccountModel
import com.example.composepractice.data.model.NotesModel


@Database(
    entities = [AccountModel::class, NotesModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters()
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDAO(): AccountDAO
    abstract fun notesDAO(): NotesDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}