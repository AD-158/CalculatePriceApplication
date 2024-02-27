package com.intern.calculator.goods.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

// Define the SettingsViewModel class which extends ViewModel
class SettingsViewModel(private val repository: UserPreferencesRepository) : ViewModel() {

    // Flow to observe user preferences
    val userPreferences: Flow<UserPreferences> = repository.userPreferencesFlow

    // Function to update the theme
    suspend fun updateTheme(theme: Theme) {
        repository.updateTheme(theme.name)
    }

    // Function to update the language
    suspend fun updateLanguage(language: Language) {
        repository.updateLanguage(language.name)

        // Set application locale based on selected language
        val locale = when (language) {
            Language.English -> "en"
            Language.Russian -> "ru"
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
    }

    // Function to convert locale string to Language enum
    fun toLanguage(locale: String): Language {
        return when (locale) {
            "en" -> Language.English
            "ru" -> Language.Russian
            else -> Language.English
        }
    }

    // Function to update the selected list number
    suspend fun updateSelectedList(listNumber: Int) {
        repository.updateSelectedList(listNumber)
    }

    // Function to update the duration
    suspend fun updateDuration(duration: Long) {
        repository.updateDuration(duration)
    }
}
