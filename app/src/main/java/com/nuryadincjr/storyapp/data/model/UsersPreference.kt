package com.nuryadincjr.storyapp.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        dataStore.edit {
            it[ID_KEY] = user.userId
            it[NAME_KEY] = user.name
            it[EMAIL_KEY] = user.email
            it[PASSWORD_KEY] = user.password
            it[TOKEN_KEY] = user.token
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

    companion object {
        @Volatile
        private var INSTANCE: UsersPreference? = null

        private val ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UsersPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UsersPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}