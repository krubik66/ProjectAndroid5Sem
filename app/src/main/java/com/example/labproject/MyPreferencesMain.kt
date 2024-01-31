package com.example.labproject

import android.content.Context


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.myPreferencesMain: DataStore<Preferences> by preferencesDataStore(name = "myPreferences")

data class UserPreferences(
    val username: String,
    val textUnder1: String,
    val textUnder2: String,
    val profilePicture: String
    )


@Singleton
class MyPreferencesMain @Inject constructor(
    @ApplicationContext context: Context
){
    private val myPreferencesMain = context.myPreferencesMain

    private object PreferencesKeys {
        val TOP = stringPreferencesKey("top")
        val UNDER1 = stringPreferencesKey("under1")
        val UNDER2 = stringPreferencesKey("under2")
        val PROFILE_PICTURE = stringPreferencesKey("profilePicture")
    }

    val userFlow = myPreferencesMain.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val top = preferences[PreferencesKeys.TOP] ?: "top"
            val under1 = preferences[PreferencesKeys.UNDER1] ?: "under1"
            val under2 = preferences[PreferencesKeys.UNDER2] ?: "under2"
            val profilePicture = preferences[PreferencesKeys.PROFILE_PICTURE] ?: "1"
            UserPreferences(top, under1, under2, profilePicture)
        }

    suspend fun updateTopText(top: String) {
        myPreferencesMain.edit { preferences ->
            preferences[PreferencesKeys.TOP] = top
        }
    }

    suspend fun updateUnder1(under1: String) {
        myPreferencesMain.edit { preferences ->
            preferences[PreferencesKeys.UNDER1] = under1
        }
    }

    suspend fun updateUnder2(under2: String) {
        myPreferencesMain.edit { preferences ->
            preferences[PreferencesKeys.UNDER2] = under2
        }
    }

    suspend fun updateProfilePicture(profilePicture: String) {
        myPreferencesMain.edit { preferences ->
            preferences[PreferencesKeys.PROFILE_PICTURE] = profilePicture
        }
    }
}
