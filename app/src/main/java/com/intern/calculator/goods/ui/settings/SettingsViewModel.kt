package com.intern.calculator.goods.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow


class SettingsViewModel(private val repository: UserPreferencesRepository) : ViewModel() {
    val userPreferences: Flow<UserPreferences> = repository.userPreferencesFlow
    suspend fun updateTheme(theme: Theme) {
        repository.updateTheme(theme.name)
    }

    suspend fun updateLanguage(language: Language) {
        repository.updateLanguage(language.name)
        val locale = when (language) {
            Language.English -> ("en")
            Language.Русский -> ("ru")
            else -> {("en")}
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
    }

    suspend fun updateSelectedList(listNumber: Int) {
        repository.updateSelectedList(listNumber)
    }
}