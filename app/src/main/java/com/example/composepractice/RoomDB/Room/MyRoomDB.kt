package com.example.composepractice.RoomDB.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.composepractice.RoomDB.DAO.AccountDAO
import com.example.composepractice.RoomDB.DAO.NotesDAO
import com.example.composepractice.RoomDB.Model.AccountModel
import com.example.composepractice.RoomDB.Model.NotesModel


@Database(
    entities = [AccountModel::class, NotesModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters()
abstract class MyRoomDB : RoomDatabase() {
    abstract fun accountDAO(): AccountDAO
    abstract fun notesDAO(): NotesDAO

    companion object {
        @Volatile
        private var INSTANCE: MyRoomDB? = null

        fun getDatabase(context: Context): MyRoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDB::class.java,
                    "my_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}