package com.nuryadincjr.storyapp.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UsersPreference private constructor(private val dataStore: DataStore<Preferences>) {
    fun getUserSession(): Flow<Users> {
        return dataStore.data.map {
            Users(
                it[ID_KEY] ?: "",
                it[NAME_KEY] ?: "",
                it[EMAIL_KEY] ?: "",
                it[PASSWORD_KEY] ?: "",
                it[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun loginSession(user: Users) {
        user.apply {
            dataStore.edit {
                it[ID_KEY] = userId
                it[NAME_KEY] = name
                it[EMAIL_KEY] = email
                it[PASSWORD_KEY] = password
                it[TOKEN_KEY] = token
            }
        }
    }

    suspend fun logoutSession() {
        dataStore.edit {
            it[ID_KEY] = ""
            it[NAME_KEY] = ""
            it[EMAIL_KEY] = ""
            it[PASSWORD_KEY] = ""
            it[TOKEN_KEY] = ""
        }
    }

    suspend fun saveTheme(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    companion object {
        @Volatile
        private var instance: UsersPreference? = null

        private val ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")

        private val THEME_KEY = booleanPreferencesKey("theme_setting")

        fun getInstance(
            dataStore: DataStore<Preferences>
        ): UsersPreference = instance ?: synchronized(this) {
            instance ?: UsersPreference(dataStore)
        }.also { instance = it }
    }
}