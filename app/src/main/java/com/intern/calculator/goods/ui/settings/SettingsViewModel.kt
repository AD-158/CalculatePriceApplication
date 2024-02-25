package com.intern.calculator.goods.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import java.util.Locale


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

    fun toLanguage(locale: String): Language {
        return when (locale) {
            ("en") -> Language.English
            ("ru") -> Language.Русский
            else -> Language.English
        }
    }

    suspend fun updateSelectedList(listNumber: Int) {
        repository.updateSelectedList(listNumber)
    }
}