package com.nuryadincjr.storyapp.data.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.util.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type

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
        dataStore.edit {
            it[THEME_KEY] = isDarkMode
        }
    }

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map {
            it[THEME_KEY] ?: false
        }
    }

    suspend fun saveWidgetList(list: List<StoryItem?>?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        dataStore.edit { preferences ->
            preferences[WIDGET_KEY] = json
        }
    }


    fun getWidgetList(): Flow<List<StoryItem?>?> {
        return dataStore.data.map {
            val json = it[WIDGET_KEY] ?: ""
            val gson = Gson()
            val type: Type = object : TypeToken<List<StoryItem?>?>() {}.type

            gson.fromJson(json, type)
        }
    }

    companion object {
        @Volatile
        private var instance: UsersPreference? = null

        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constant.PREF_SESSION)

        private val ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
        private val WIDGET_KEY = stringPreferencesKey("widget_list")

        fun getInstance(
            dataStore: DataStore<Preferences>
        ): UsersPreference = instance ?: synchronized(this) {
            instance ?: UsersPreference(dataStore)
        }.also { instance = it }
    }
}