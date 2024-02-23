package com.intern.calculator.goods.ui.settings

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.intern.calculator.goods.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException



data class UserPreferences(
    val theme:Theme,
    val language: Language,
    val selectedList: Int,
)

enum class Theme(@StringRes val title: Int) {
    Light(R.string.setting_theme_light),
    Dark(R.string.setting_theme_dark),
    System(R.string.setting_theme_system),
}

enum class Language() {
    English,
    Русский,
}

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>,
    context: Context
) {

    private val TAG: String = "settings"

    private object PreferencesKeys {
        val THEME_KEY = stringPreferencesKey("theme")
        val LANGUAGE_KEY = stringPreferencesKey("language")
        val SELECTED_LIST_KEY = intPreferencesKey("selectedList")
    }

    /**
     * Get the user preferences flow.
     */
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    suspend fun updateTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_KEY] = theme
        }
    }
    suspend fun updateLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_KEY] = language
        }
    }
    suspend fun updateSelectedList(listNumber: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_LIST_KEY] = listNumber
        }
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val theme = preferences[PreferencesKeys.THEME_KEY]?.let { Theme.valueOf(it) } ?: Theme.System
        val language = preferences[PreferencesKeys.LANGUAGE_KEY]?.let { Language.valueOf(it) } ?: Language.English
        val selectedList = preferences[PreferencesKeys.SELECTED_LIST_KEY] ?: 1
        val locale = when (language) {
            Language.English -> ("en")
            Language.Русский -> ("ru")
            else -> {("en")}
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
        return UserPreferences(theme = theme, language = language, selectedList = selectedList)
    }
}