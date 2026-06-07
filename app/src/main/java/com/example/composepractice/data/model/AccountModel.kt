package com.example.composepractice.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userName: String,
    val password: String,
)
