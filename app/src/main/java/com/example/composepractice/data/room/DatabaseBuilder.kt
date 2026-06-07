package com.example.composepractice.data.room

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    @Volatile
    private var mInstance: AppDatabase? = null

    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "local-saved-db")
            .fallbackToDestructiveMigration(true).build()

    fun getInstance(context: Context): AppDatabase {
        return mInstance ?: synchronized(this) {
            mInstance ?: buildRoomDB(context).also { mInstance = it }
        }
    }

    fun destroyInstance() {
        mInstance = null
    }
}
