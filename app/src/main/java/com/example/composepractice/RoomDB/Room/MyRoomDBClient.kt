package com.example.composepractice.RoomDB.Room

import android.content.Context
import androidx.room.Room

class MyRoomDBClient private constructor(mCtx: Context) {

    private var myRoomDB: MyRoomDB =
        Room.databaseBuilder(mCtx.applicationContext, MyRoomDB::class.java, "SecretDB").build()

    fun getAppDatabase(): MyRoomDB {
        return myRoomDB
    }

    companion object {
        @Volatile
        private var mInstance: MyRoomDBClient? = null

        fun getInstance(context: Context): MyRoomDBClient {
            return mInstance ?: synchronized(this) {
                mInstance ?: MyRoomDBClient(context).also { mInstance = it }
            }
        }

        fun getDatabase(context: Context): MyRoomDB {
            return getInstance(context).getAppDatabase()
        }

        fun destroyInstance() {
            mInstance?.getAppDatabase()?.close()
            mInstance = null
        }
    }
}