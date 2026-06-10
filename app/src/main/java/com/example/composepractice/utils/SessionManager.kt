package com.example.composepractice.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val userNameKey = stringPreferencesKey("user_name")
    private val userIdKey = intPreferencesKey("user_id")

    // Get the username as a Flow (Reactive!)
    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[userNameKey] ?: ""
    }
    // Save username (Suspend function!)
    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[userNameKey] = userName
        }
    }

    // Get the userId as a Flow (Reactive!)
    val userIdFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[userIdKey] ?: -1
    }

    // Save userId (Suspend function!)
    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[userIdKey] = userId
        }
    }

    // Clear session data
    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}